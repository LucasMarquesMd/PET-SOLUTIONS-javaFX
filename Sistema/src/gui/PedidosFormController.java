package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.entities.Cliente;
import model.entities.Colaborador;
import model.entities.PedidoItems;
import model.entities.Pedidos;
import model.entities.Produto;
import model.entities.enums.PedidoStatus;
import model.exceptions.ValidationException;
import model.services.ClienteServices;
import model.services.ColaboradorServices;
import model.services.PedidoItemsServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;


public class PedidosFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private Pedidos entityPed;
	private Colaborador entityCol;
	private PedidoItems entityItems;
	private PedidosServices servicesPed;
	private ColaboradorServices servicesCol;
	private PedidoItemsServices servicesItems;
	private ProdutoServices servicesProd;
	private ClienteServices servicesCli;
	
	
	private List<PedidoItems> pedidoItems = new ArrayList<>();
	
	private ObservableList<PedidoStatus> pedidoStatus = FXCollections.observableArrayList(PedidoStatus.values());//Pega a colecao de valores do enum
	private ObservableList<Cliente> clienteList;
	private ObservableList<Produto> produtoList;
	private ObservableList<PedidoItems> itemsList;

	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do Pedido	
// =================================================================================
	
	@FXML
	private TextField txtId;
	@FXML
	private DatePicker dpData;
	@FXML
	private TextField txtPreco;
	@FXML
	private TextField txtColaborador;
	@FXML
	private ComboBox<PedidoStatus> cboStatus;
	@FXML
	private ComboBox<Cliente> cboCliente;
	@FXML
	private ComboBox<Produto> cboProduto;
	
// =================================================================================
//						Atibutos da tabela
// =================================================================================
	
	@FXML
	private TableView tableViewPedidos;
	@FXML
	private TableColumn<PedidoItems, Integer> tbcId;
	@FXML
	private TableColumn<Produto, String> tbcNome;
	@FXML
	private TableColumn<PedidoItems, Integer> tbcQuantidade;
	@FXML
	private TableColumn<PedidoItems, Double> tbcPreco;
	@FXML
	private TableColumn<PedidoItems, PedidoItems> tableColumnREMOVE;
	
	

// =================================================================================
//							Atibutos controles genericos	
// =================================================================================
	
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnAdicionar;
	
// =================================================================================
//							Atributos dos Labels errors	
//=================================================================================

	//Pedidos
	@FXML
	private Label lblErrorStatus;
	@FXML
	private Label lblProdutos;
	
	
// =================================================================================
//							Funcoes dos controles	
// =================================================================================
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entityCol == null) {
			throw new IllegalStateException("Entity (entityCol) was null");
		}		
		if(entityPed == null) {
			throw new IllegalStateException("Entity (entityPed) was null");
		}
		try {
			fieldesValidation();//Validacao dos campos
			
			entityPed = getFormData(pedidoItems);
			servicesPed.saveOrUpdate(entityPed);
			
			notifyDataChangeListeners();
			
			Utils.currentStage(event).close();//Fecha o formulario
			
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());//Envia a colecao de erros

		}
		catch(DbException e) {
			e.printStackTrace();
			Alerts.showAlerts("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void onBtnAdicionarAction() {
		PedidoItems obj = new PedidoItems();
		
		obj.setQt_PedIt(Utils.tryParseToInt(tbcQuantidade.getText()));
		obj.setPreco_PedIt(Utils.tryParseToDouble(tbcPreco.getText()));
		obj.setProduto(cboProduto.getSelectionModel().getSelectedItem());
		//obj.setId_Prod(obj.getProduto().getId_Prod());
		
		pedidoItems.add(obj);
		updateTableView();
	}
	
	
// =================================================================================
//						Funcoes para injecao de dependencia	
// =================================================================================
	
	public void setPedidos(Pedidos entity) {
		this.entityPed = entity;
	}
	
	public void setPedidoItems(PedidoItems entity) {
		this.entityItems = entity;
	}
	
	public void setColaborador(Colaborador entity) {
		this.entityCol = entity;
	}
	
	public void setPedidosServices(PedidosServices servicesPed) {
		this.servicesPed = servicesPed;
	}
	public void setPedidoItemsServices(PedidoItemsServices servicesItems) {
		this.servicesItems = servicesItems;
	}
	
	public void setColaboradorServices(ColaboradorServices servicesCol) {
		this.servicesCol = servicesCol;
	}
	
	public void setServicesProd(ProdutoServices servicesProd) {
		this.servicesProd = servicesProd;
	}

	public void setServicesCli(ClienteServices servicesCli) {
		this.servicesCli = servicesCli;
	}

// =================================================================================
//								Funcoes da view
// =================================================================================

	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		
	}
	
	//Restricoes
	private void initializeNode() {
		
		cboStatus.setItems(pedidoStatus);
		cboCliente.setItems(clienteList);
		cboProduto.setItems(produtoList);
		
		
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		dpData.setValue(null);
		
		//PedidoItems
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id_PedIt"));
		tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_Prod"));
		tbcPreco.setCellValueFactory(new PropertyValueFactory<>("preco_PedIt"));
		tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("qt_PedIt"));
		
		
		initializeComboBoxCliente();
		initializeComboBoxProduto();
		
	}
	
// =================================================================================
//							Atualizacao dos dados	
// =================================================================================
	
	public void updateTableView() {
		if(servicesItems == null) {
			throw new IllegalStateException("Service Pedidos was null!");
		}
		
		List<PedidoItems> list = servicesItems.findAll();

		itemsList = FXCollections.observableArrayList(list);
	
		tableViewPedidos.setItems(itemsList);

		initRemoveButtons();		
	}//End updateTableView
	
	
	public void updateFormData() {
		if(entityPed == null) {
			throw new IllegalStateException("Entity (Pedidos) was null");
		}
		txtId.setText(String.valueOf(entityPed.getId_Ped()));
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		
		
		//LocalDate.ofInstant() -> Converte a data do objeto para a data local
		//.toInstant() -> converte o valor para um instant
		//ZoneId -> Define o fuso-horario
		//.systemDefault() -> pega o fuso-horario da maquina
		if(entityPed.getDt_Ped() != null) {
			dpData.setValue(LocalDate.ofInstant(entityPed.getDt_Ped().toInstant(), ZoneId.systemDefault()));
		}
		if(entityCol == null) {
			throw new IllegalStateException("Entity (Colaborador) was null");
		}
		//Colaborador
		txtColaborador.setText(entityCol.getName());
		
		
		
		
		
	}
	
	//Carrega os dados nos combobox
	public void loadAssociatedObjects() {
		if(servicesCli == null) {
			throw new IllegalStateException("servicesCli was null!");
		}
		List<Cliente> listCli = servicesCli.findAll();
		clienteList = FXCollections.observableArrayList(listCli);
		cboCliente.setItems(clienteList);
		
		if(servicesProd == null) {
			throw new IllegalStateException("servicesProd was null!");
		}
		List<Produto> listProd = servicesProd.findAll();
		produtoList = FXCollections.observableArrayList(listProd);
		cboProduto.setItems(produtoList);
	}
	
	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome_Cli());
			}
		};
		cboCliente.setCellFactory(factory);
		cboCliente.setButtonCell(factory.call(null));
	}//end initializeComboBoxCliente
	
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
	}//end initializeComboBoxProduto
	
// =================================================================================
//								Metodos	
// =================================================================================
	
	
	//Quais quer objetos que implementarem a inteface, podem se inscrever para receber o evento do controller
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();//Invoca o metodo nas classes que implementaram a interface
		}
	}
	
	//Preenche ou atualiza os dados do objeto colaborador
	private Pedidos getFormData(List<PedidoItems> pedidoItems) {
		Pedidos obj = new Pedidos();
		for(PedidoItems items : pedidoItems) {
			obj.addItem(items);
		}
		
		//Instant -> data independente de localidade
		Instant instant = Instant.from(dpData.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDt_Ped(Date.from(instant));
		obj.setPreco_Ped(Utils.tryParseToDouble(txtPreco.getText()));
		obj.setStatus_Ped(PedidoStatus.valueOf(cboStatus.getValue().toString()));
		obj.setColaborador(entityCol);
		
		return obj;
	}
	
	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");

		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorStatus.setText(fields.contains("Status") ? errors.get("Status") : "");
		lblProdutos.setText(fields.contains("Produto") ? errors.get("Produto") : "");
		
		
	}//
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<PedidoItems, PedidoItems>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(PedidoItems obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}//end initRemoveButtons
	
	
	private void removeEntity(PedidoItems obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getId_PedIt() + " ?");
		
		
		if(result.get() == ButtonType.OK) {
			if(servicesItems == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				
				servicesItems.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null,e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	

}













