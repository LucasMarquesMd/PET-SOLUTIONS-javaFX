package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.entities.Estoque;
import model.entities.Fornecedor;
import model.entities.Fornecimento;
import model.entities.LocalDeEstoque;
import model.entities.NotaEstoque;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.EstoqueServices;
import model.services.FornecedorServices;
import model.services.FornecimentoServices;
import model.services.LocalDeEstoqueServices;
import model.services.NotaEstoqueServices;
import model.services.ProdutoServices;

/*
 * Passos faltantes:
 * 	Inicializar comboBox
 * 	Adicionar getFormDataProduto
 * 	Funcao para somar o estoque no produto
 */

public class NotasFiscaisFormController implements Initializable {

// =================================================================================
//								Dependencias	
// =================================================================================	

	// Entidades
	private NotaEstoque entityNota;
	private Fornecimento entityForne;
	private Fornecedor entityForn;
	private LocalDeEstoque entityLocal;
	private Produto entityProd;
	private Estoque entityEst;

	// Servicos
	private NotaEstoqueServices serviceNota;
	private ProdutoServices serviceProd;
	private LocalDeEstoqueServices serviceLocal;
	private FornecedorServices serviceForn;
	private FornecimentoServices serviceForne;
	private EstoqueServices serviceEst;

	// ObsLists
	private ObservableList<Produto> obsListProd;
	private ObservableList<Fornecimento> obsListForne;
	private ObservableList<Fornecedor> obsListForn;
	private ObservableList<LocalDeEstoque> obsListLocal;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// =================================================================================
// 						Atibutos do Colaborador	
// =================================================================================

	@FXML
	private TextField txtIdNota;
	@FXML
	private TextField txtNro;
	@FXML
	private TextField txtPreco;
	@FXML
	private TextField txtQuantidade;

	private Integer id_Forne;
	private Integer id_Est;

	// DataPicker
	@FXML
	private DatePicker dpData;

// =================================================================================
//							Atibutos do comboBox	
// =================================================================================

	@FXML
	private ComboBox cboProduto;
	@FXML
	private ComboBox cboFornecedor;
	@FXML
	private ComboBox cboLocal;

// =================================================================================
//							Atibutos controles genericos	
//=================================================================================

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;

// =================================================================================
//							Atributos dos Labels errors	
//=================================================================================

	// Colaborador
	@FXML
	private Label lblErrorNro;
	@FXML
	private Label lblErrorPreco;
	@FXML
	private Label lblErrorQuantidade;
	@FXML
	private Label lblErrorProduto;
	@FXML
	private Label lblErrorFornecedor;
	@FXML
	private Label lblErrorLocal;

// =================================================================================
//						Funcoes para injecao de dependencia	
// =================================================================================

	// Entidades
	public void setEntityNota(NotaEstoque entityNota) {
		this.entityNota = entityNota;
	}

	public void setEntityEst(Estoque entityEst) {
		this.entityEst = entityEst;
	}

	public void setEntityForn(Fornecedor entityForn) {
		this.entityForn = entityForn;
	}

	public void setEntityForne(Fornecimento entityForne) {
		this.entityForne = entityForne;
	}

	public void setEntityProd(Produto entityProd) {
		this.entityProd = entityProd;
	}

	public void setEntityLocal(LocalDeEstoque entityLocal) {
		this.entityLocal = entityLocal;
	}

	// Servicos
	public void setServiceNota(NotaEstoqueServices serviceNota) {
		this.serviceNota = serviceNota;
	}

	public void setServiceProd(ProdutoServices serviceProd) {
		this.serviceProd = serviceProd;
	}

	public void setServiceLocal(LocalDeEstoqueServices serviceLocal) {
		this.serviceLocal = serviceLocal;
	}

	public void setServiceForn(FornecedorServices serviceForn) {
		this.serviceForn = serviceForn;
	}

	public void setServiceForne(FornecimentoServices serviceForne) {
		this.serviceForne = serviceForne;
	}

	public void setServiceEst(EstoqueServices serviceEst) {
		this.serviceEst = serviceEst;
	}

// =================================================================================
//	Funcoes dos controles	
// =================================================================================

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entityForne == null) {
			throw new IllegalStateException("Entity (entityEnd) was null");
		}
		if (entityNota == null) {
			throw new IllegalStateException("Entity (entityNota) was null");
		}
		try {
			fieldesValidation();// Validacao dos campos

			// Estoque
			entityEst = getFormDataEstoque();
			serviceEst.saveOrUpdate(entityEst);

			// Fornecimento
			entityForne = getFormDataFornecimento(entityEst);
			serviceForne.saveOrUpdate(entityForne);

			// Nota
			entityNota = getFormDataNota(entityForne);
			serviceNota.saveOrUpdate(entityNota);

			notifyDataChangeListeners();

			Utils.currentStage(event).close();// Fecha o formulario

		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());// Envia a colecao de erros

		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlerts("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtnSenhaAction() {
		System.out.println("onBtnSenhaAction - clik");
	}

// =================================================================================
//								Inicializacao
// =================================================================================

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		initializeDatePicker();

	}

	// Restricoes
	private void initializeNode() {

		if(entityEst != null) {
			id_Est = entityEst.getId_Est();
		}
		if(entityForne != null) {
			id_Forne = entityForne.getId_Forne();
		}
		

		Constraints.setTextFieldInteger(txtIdNota);
		Constraints.setTextFieldInteger(txtNro);
		Constraints.setTextFieldInteger(txtQuantidade);
		Constraints.setTextFieldDouble(txtPreco);
		Constraints.setTextFieldMaxLength(txtNro, 45);
		Constraints.setTextFieldMaxLength(txtPreco, 50);
		Constraints.setTextFieldMaxLength(txtQuantidade, 50);

		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		dpData.setValue(null);

		cboFornecedor.setItems(obsListForn);
		cboProduto.setItems(obsListProd);
		cboLocal.setItems(obsListLocal);
		cboProduto.getSelectionModel().selectFirst();
		cboLocal.getSelectionModel().selectFirst();
		cboFornecedor.getSelectionModel().selectFirst();

		initializeComboBoxProduto();
		initializeComboBoxFornecedor();
		initializeComboBoxLocais();

	}

	private void initializeDatePicker() {
		// Obter a data atual
		LocalDate currentDate = LocalDate.now();

		// Configurar o DatePicker com a data atual
		dpData.setValue(currentDate);
	}

	// Carrega os dados nos combobox
	public void loadAssociatedObjects() {
		if (serviceForn == null) {
			throw new IllegalStateException("servicesForne was null!");
		}
		List<Fornecedor> listforne = serviceForn.findAll();
		obsListForn = FXCollections.observableArrayList(listforne);
		cboFornecedor.setItems(obsListForn);

		if (serviceProd == null) {
			throw new IllegalStateException("servicesProd was null!");
		}
		List<Produto> listProd = serviceProd.findAll();
		obsListProd = FXCollections.observableArrayList(listProd);
		cboProduto.setItems(obsListProd);

		if (serviceLocal == null) {
			throw new IllegalStateException("servicesLocal was null!");
		}
		List<LocalDeEstoque> listForn = serviceLocal.findAll();
		obsListLocal = FXCollections.observableArrayList(listForn);
		cboLocal.setItems(obsListLocal);
	}

	// Atualiza os valores do formulario com os dados da entidade
	public void updateFormData() {
		if (entityNota == null) {
			throw new IllegalStateException("Entity (Colaborador) was null");
		}
		// Nota
		txtIdNota.setText(String.valueOf(entityNota.getId_Nota()));
		txtNro.setText(String.valueOf(entityNota.getNro_Nota()));

		if (entityForn == null) {
			throw new IllegalStateException("Entity (Fornecedor) was null");
		}

		// Fornecimento
		if(entityForne != null) {
			txtPreco.setText(String.valueOf(entityForne.getPreco_Forne()));
		}
		
		if (entityForne.getProduto() != null) {
			txtQuantidade.setText(String.valueOf(entityForne.getProduto().getQtd_Estocado()));
		}
		
		if(entityProd == null) {
			cboProduto.getSelectionModel().selectFirst();
		}else {
			cboProduto.setValue(entityProd);
		}
		if(entityForn == null || entityForn.getNome_Forn() == null) {
			cboFornecedor.getSelectionModel().selectFirst();
		}else {
			cboFornecedor.setValue(entityForn);
		}
		if(entityLocal == null || entityLocal.getNome_Local() == null) {
			cboLocal.getSelectionModel().selectFirst();
		}else {
			cboLocal.setValue(entityLocal);
		}
		
	}
// =================================================================================
//							Inicializar comboBox
// =================================================================================

	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome_Prod());
			}
		};
		cboProduto.setCellFactory(factory);
		cboProduto.setButtonCell(factory.call(null));
	}// end initializeComboBoxProduto
	
	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome_Forn());
			}
		};
		
		cboFornecedor.setCellFactory(factory);
		cboFornecedor.setButtonCell(factory.call(null));
	}// end initializeComboBoxFornecedor
	
	private void initializeComboBoxLocais() {
		Callback<ListView<LocalDeEstoque>, ListCell<LocalDeEstoque>> factory = lv -> new ListCell<LocalDeEstoque>() {
			@Override
			protected void updateItem(LocalDeEstoque item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome_Local());
			}
		};
		
		cboLocal.setCellFactory(factory);
		cboLocal.setButtonCell(factory.call(null));
	}// end initializeComboBoxProduto

// =================================================================================
//							Data change listener
// =================================================================================	

	// Quais quer objetos que implementarem a inteface, podem se inscrever para
	// receber o evento do controller
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();// Invoca o metodo nas classes que implementaram a interface
		}
	}

// =================================================================================
//								Get form datas
// =================================================================================

	// Preenche ou atualiza os dados do objeto NotaEstoque
	private NotaEstoque getFormDataNota(Fornecimento forne) {
		NotaEstoque obj = new NotaEstoque();

		obj.setId_Nota(Utils.tryParseToInt(txtIdNota.getText()));
		obj.setValor(Utils.tryParseToDouble(txtPreco.getText()));
		obj.setNro_Nota(Utils.tryParseToInt(txtNro.getText()));
		obj.setId_Forne(forne.getId_Forne());

		obj.setFornecimento(forne);

		return obj;
	}

	private Estoque getFormDataEstoque() {
		Estoque obj = new Estoque();

		obj.setId_Est(id_Est);
		obj.setQt_Prod_Est(Utils.tryParseToInt(txtQuantidade.getText()));
		// Data
		LocalDate selectedDate = dpData.getValue();// Transforma a data em data local
		if (selectedDate != null) {
			Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
			obj.setDt_Est(Date.from(instant));
		}

		LocalDeEstoque selectedLocal = (LocalDeEstoque) cboLocal.getSelectionModel().getSelectedItem();
		if (selectedLocal != null) {
			obj.setId_Local(selectedLocal.getId_Local());
			obj.setLocalDeEstoque(selectedLocal);
		}

		Produto selectedProduct = (Produto) cboProduto.getSelectionModel().getSelectedItem();
		if (selectedProduct != null) {
			obj.setId_Prod(selectedProduct.getId_Prod());
			obj.setProduto(selectedProduct);
		}

		return obj;
	}

	private Fornecimento getFormDataFornecimento(Estoque entity) {
		Fornecimento obj = new Fornecimento();

		obj.setId_Forne(id_Forne);

		LocalDate selectedDate = dpData.getValue();
		if (selectedDate != null) {
			Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
			obj.setDt_Forne(Date.from(instant));
		}

		obj.setPreco_Forne(Utils.tryParseToDouble(txtPreco.getText()));

		// Pegar o id do fornecedor no combobox
		Fornecedor selectedFornecedor = (Fornecedor) cboFornecedor.getSelectionModel().getSelectedItem(); // Pega o																								// selecionado
		if (selectedFornecedor != null) {
			obj.setId_Forn(selectedFornecedor.getId_Forn());
			obj.setFornecedor(selectedFornecedor);
		}

		Produto selectedProduct = (Produto) cboProduto.getSelectionModel().getSelectedItem();
		if (selectedProduct != null) {
			obj.setId_Prod(selectedProduct.getId_Prod());
			obj.setProduto(selectedProduct);
		}

		obj.setEstoque(entity);

		return obj;
	}

	private Produto getFormDataProduto() {

		Produto obj = (Produto) cboProduto.getSelectionModel().getSelectedItem();
		return obj;
	}

// =================================================================================
//								Validacao dos campos
// =================================================================================

	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados da Nota!");

		if (txtNro.getText() == null || txtNro.getText().trim().equals("")) {
			exception.addErrors("Nro", "Field can't be empty!");
		}

		if (txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.addErrors("Preco", "Field can't be empty!");
		}

		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addErrors("Quantidade", "Field can't be empty!");
		}

		if (cboProduto.getSelectionModel().getSelectedItem() == null) {
			exception.addErrors("Produto", "Field can't be empty!");
		}

		if (cboFornecedor.getSelectionModel().getSelectedItem() == null) {
			exception.addErrors("Fornecedor", "Field can't be empty!");
		}

		if (cboLocal.getSelectionModel().getSelectedItem() == null) {
			exception.addErrors("Local", "Field can't be empty!");
		}

		if (exception.getErrors().size() > 0) {
			throw exception;// Lanca a excessao
		}
	}

	// Envia as mensagens de erros para os lables
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNro.setText(fields.contains("Nro") ? errors.get("Nro") : "");
		lblErrorPreco.setText(fields.contains("Preco") ? errors.get("Preco") : "");
		lblErrorQuantidade.setText(fields.contains("Quantidade") ? errors.get("Quantidade") : "");
		lblErrorProduto.setText(fields.contains("Produto") ? errors.get("Produto") : "");
		lblErrorFornecedor.setText(fields.contains("Fornecedor") ? errors.get("Fornecedor") : "");
		lblErrorLocal.setText(fields.contains("Local") ? errors.get("Local") : "");

	}//

}
