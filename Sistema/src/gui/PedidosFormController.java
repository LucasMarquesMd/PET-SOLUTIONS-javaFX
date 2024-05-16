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
import gui.util.Constraints;
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
	
	//Lista de produtos da tabela 
	private List<Produto> produtosList;

	//Lista de items 
	private List<PedidoItems> pedidoItems;
	
	private ObservableList<PedidoStatus> pedidoStatus = FXCollections.observableArrayList(PedidoStatus.values());//Pega a colecao de valores do enum
	private ObservableList<Cliente> clienteList;
	private ObservableList<Produto> obsListProd;
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
	private TextField txtQuantidade;
	@FXML
	private ComboBox<PedidoStatus> cboStatus;
	@FXML
	private ComboBox<Cliente> cboCliente;
	@FXML
	private ComboBox<Produto> cboProduto;
	
	private Double total = 0.0;
	
// =================================================================================
//						Atibutos da tabela
// =================================================================================
	
	@FXML
	private TableView tableViewPedidos;
	@FXML
	private TableColumn<Produto, Integer> tbcId;
	@FXML
	private TableColumn<Produto, String> tbcNome;
	@FXML
	private TableColumn<Produto, Integer> tbcQuantidade;
	@FXML
	private TableColumn<Produto, Double> tbcPreco;
	@FXML
	private TableColumn<Produto, Produto> tableColumnREMOVE;
	
	

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
	@FXML
	private Label lblErrorData;
	
	
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
			
			fieldesValidation();
			
			entityPed = getFormData(pedidoItems);
			
			servicesPed.saveOrUpdate(entityPed);
			savePedidosItems(entityPed.getId_Ped());
			
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
	
	//Adiciona o produto na tabela
	public void onBtnAdicionarAction() {
		PedidoItems pedIt = new PedidoItems();
		Produto prod = cboProduto.getSelectionModel().getSelectedItem();
		
		if(produtosList.contains(prod)) {
			Alerts.showAlerts("Aviso", "Pedido", "O produto ja consta na lista de items!", AlertType.WARNING);
		}else {
			if(txtQuantidade.getText().equals("")) {
				pedIt.setQt_PedIt(1);
				prod.setQt_Prod(1);
			}else {
				pedIt.setQt_PedIt(Utils.tryParseToInt(txtQuantidade.getText()));
				prod.setQt_Prod(Utils.tryParseToInt(txtQuantidade.getText()));
			}
			
			pedIt.setPreco_PedIt(prod.getPreco_Prod());
			pedIt.setProduto(prod);
			pedIt.setId_Prod(prod.getId_Prod());
		
			pedidoItems.add(pedIt);
			produtosList.add(prod);
			
			total += pedIt.subTotal();
			txtPreco.setText(total.toString());
			updateTableView();
		}

		
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
	
	public void setColaboradorServices(ColaboradorServices servicesCol) {
		this.servicesCol = servicesCol;
	}
	
	public void setServicesProd(ProdutoServices servicesProd) {
		this.servicesProd = servicesProd;
	}

	public void setServicesCli(ClienteServices servicesCli) {
		this.servicesCli = servicesCli;
	}
	
	public void setPedidoItemsServices(PedidoItemsServices servicesItems) {
		this.servicesItems = servicesItems;
	}
	
	public void setListProdutosList(List<Produto> produtosList) {
		this.produtosList = produtosList;
	}

	public void setListPedidoItems(List<PedidoItems> pedidoItems) {
		this.pedidoItems = pedidoItems;
	}

	

// =================================================================================
//								Funcoes da view
// =================================================================================

	

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		initializeDatePicker();
		
	}
	
	//Restricoes
	private void initializeNode() {
		//ComboBox
		cboStatus.setItems(pedidoStatus);
		cboCliente.setItems(clienteList);
		cboProduto.setItems(obsListProd);
		
		//DataPicker
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		dpData.setValue(null);
		
		//TableView
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id_Prod"));
		tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_Prod"));
		tbcPreco.setCellValueFactory(new PropertyValueFactory<>("preco_Prod"));
		tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("qt_Prod"));
		
		//TextFields
		Integer n = 1;
		txtQuantidade.setText(n.toString());
		
		
		//Constraints
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldInteger(txtQuantidade);
		
		
		initializeComboBoxCliente();
		initializeComboBoxProduto();
		
		cboProduto.getSelectionModel().selectFirst();
		cboStatus.getSelectionModel().selectFirst();
		
	}
	
	private void initializeDatePicker() {
	    // Obter a data atual
	    LocalDate currentDate = LocalDate.now();
	    
	    // Configurar o DatePicker com a data atual
	    dpData.setValue(currentDate);
	}
	
// =================================================================================
//							Atualizacao dos dados	
// =================================================================================
	
	public void updateTableView() {
		if(servicesItems == null) {
			throw new IllegalStateException("Service Pedidos was null!");
		}
		
		
		ObservableList<Produto> list = FXCollections.observableArrayList(produtosList);

		 
	
		tableViewPedidos.setItems(list);
		

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
		
		//Combobox
		if (entityItems.getProduto() == null) {
			cboProduto.getSelectionModel().selectFirst();
		} else {
			cboProduto.setValue(entityItems.getProduto());
		}
		
		if (entityPed.getStatus_Ped() == null) {
			cboStatus.getSelectionModel().selectFirst();
		} else {
			cboStatus.setValue(entityPed.getStatus_Ped());
		}
		
		

		if(entityPed.getId_Ped() != null) {
			pedidoItems = servicesItems.findItemsProd(entityPed.getId_Ped());
			
			for(PedidoItems item : pedidoItems) {
				Produto prod = servicesProd.findById(item.getId_Prod());
				prod.setQt_Prod(item.getQt_PedIt());
				total += item.getPreco_PedIt() * item.getQt_PedIt();
				produtosList.add(prod);
				updateTableView();
			}
			
			txtPreco.setText(total.toString());
		}
		
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
		obsListProd = FXCollections.observableArrayList(listProd);
		cboProduto.setItems(obsListProd);
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
		
		ValidationException exception = new ValidationException("Erro ao validar os dados do pedido!");
		
		for(PedidoItems items : pedidoItems) {
			obj.addItem(items);
		}
		
		//Instant -> data independente de localidade
		LocalDate selectedDate = dpData.getValue();
		if (selectedDate != null) {
		    Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
		    obj.setDt_Ped(Date.from(instant));
		} else {
			exception.addErrors("Data", "informar a data");
		}
		
		obj.setId_Ped(Utils.tryParseToInt(txtId.getText()));
		obj.setPreco_Ped(Utils.tryParseToDouble(txtPreco.getText()));
		obj.setStatus_Ped(PedidoStatus.valueOf(cboStatus.getValue().toString()));
		obj.setId_Col(entityCol.getIdColab());
		obj.setColaborador(entityCol);
		
		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
		
		return obj;
	}
	
	
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
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
	
	
	private void removeEntity(Produto obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getId_Prod() + " ?");
		
		
		if(result.get() == ButtonType.OK) {
			if(servicesItems == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				
				PedidoItems item = servicesItems.findByIdPed(entityPed.getId_Ped(), obj.getId_Prod());
				
				total -= item.getPreco_PedIt() * item.getQt_PedIt();
				produtosList.remove(obj);
				txtPreco.setText(total.toString());
				
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null,e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	

	private void savePedidosItems(Integer id){
		List<PedidoItems> list = servicesItems.findItemsProd(id);//Lista dos produtos de um pedido
		
		ValidationException exception = new ValidationException("Erro ao validar os dados do pedido!");
		
		if(list == null) {
			for(PedidoItems item: pedidoItems) {
				item.setId_Ped(id);
				servicesItems.saveOrUpdate(item);
			}
		}else {
			//Remover os items atuais
			for(PedidoItems item: list) {
				servicesItems.remove(item);
			}
			if(pedidoItems != null) {
				//Adicionar nova listaa
				for(PedidoItems item: pedidoItems) {
					item.setId_Ped(id);
					servicesItems.saveOrUpdate(item);
				}
			}else {
				exception.addErrors("Produto", "Lista vazia! adicione produtos a lista!");
			}
			
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}

	}
	
	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");
		
		if(produtosList.size() == 0) {
			exception.addErrors("Produto", "Lista de produtos vazia!");
		}

		
		


		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		lblProdutos.setText(fields.contains("Produto") ? errors.get("Produto") : "");
	}//
}













