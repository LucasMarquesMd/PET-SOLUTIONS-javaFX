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
import java.util.stream.Collectors;

import application.Main;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Cliente;
import model.entities.Colaborador;
import model.entities.Pagamentos;
import model.entities.PedidoItems;
import model.entities.Pedidos;
import model.entities.Produto;
import model.entities.enums.PedidoStatus;
import model.entities.enums.TipoDePagamento;
import model.exceptions.ValidationException;
import model.services.ClienteServices;
import model.services.ColaboradorServices;
import model.services.PagamentosServices;
import model.services.PedidoItemsServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;

public class PedidosFormController implements Initializable {

// =================================================================================
//								Dependencias	
// =================================================================================	
	// Entidades
	private Pedidos entityPed;
	private Colaborador entityCol;
	private PedidoItems entityItems;
	private Pagamentos entityPag;
	private Integer id_Pag;

	// Servicos
	private PedidosServices servicesPed;
	private ColaboradorServices servicesCol;
	private PedidoItemsServices servicesItems;
	private ProdutoServices servicesProd;
	private ClienteServices servicesCli;
	private PagamentosServices servicePag;

	// Lista de produtos da tabela
	private List<Produto> produtosList;

	// Lista de items
	private List<PedidoItems> pedidoItemsList;

	// ObsLists
	private ObservableList<PedidoStatus> pedidoStatus = FXCollections.observableArrayList(PedidoStatus.values());// Pega
																													// a
																													// colecao
																													// de
																													// valores
																													// do
																													// enum
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

	// Pedidos
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
		if (entityCol == null) {
			throw new IllegalStateException("Entity (entityCol) was null");
		}
		if (entityPed == null) {
			throw new IllegalStateException("Entity (entityPed) was null");
		}
		try {

			fieldesValidation();
			
			entityPag = getFormDataPag();
			servicePag.saveOrUpdate(entityPag);

			pedidoItemsList = listItems();
			entityPed = getFormData(pedidoItemsList, entityPag);
			entityPag.setNro_Ped(entityPed.getId_Ped());
			servicePag.saveOrUpdate(entityPag);

			if(cboStatus.getSelectionModel().getSelectedItem() == PedidoStatus.CANCELADO) {
				retomarEstoque(pedidoItemsList);
			}
			
			if(cboStatus.getSelectionModel().getSelectedItem() == PedidoStatus.PAGO) {
				baixarEstoque(pedidoItemsList);
			}
			
			servicesPed.saveOrUpdate(entityPed);
			savePedidosItems(entityPed.getId_Ped());

			

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

	// Adiciona o produto na tabela
	public void onBtnAdicionarAction() {
		Produto prod = cboProduto.getSelectionModel().getSelectedItem();
		
		if(cboProduto.getSelectionModel().getSelectedItem() == null) {
			Alerts.showAlerts("Adicinar", "pedidos", "Nenhum produto selecionado!", AlertType.ERROR);
			return;
		}

		if (produtosList.contains(prod)) {
			Alerts.showAlerts("Aviso", "Pedido", "O produto ja consta na lista de items!", AlertType.WARNING);
			btnAdicionar.requestFocus();
		} else {

			if (txtQuantidade.getText().equals("")) {
				Alerts.showAlerts("Aviso", "Pedido", "Informe a quantidade!", AlertType.WARNING);
			} else {
				if((Utils.tryParseToInt(txtQuantidade.getText())) > prod.getQtd_Estocado()) {
					Alerts.showAlerts("Aviso", "Pedido", "Estoque insuficiente!\nQuantidade: "+ prod.getQtd_Estocado(), AlertType.WARNING);
					txtQuantidade.requestFocus();//Coloca o foco no controle
				}else {
					prod.setQt_Prod(Utils.tryParseToInt(txtQuantidade.getText()));
					
					produtosList.add(prod);

					total += prod.getPreco_Prod() * prod.getQt_Prod();
					txtPreco.setText(total.toString());
					updateTableView();
				}

			}
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

	public void setListPedidoItems(List<PedidoItems> pedidoItemsList) {
		this.pedidoItemsList = pedidoItemsList;
	}

	public void setPagamentos(Pagamentos pagamento) {
		this.entityPag = pagamento;
	}

	public void setPagamentosServices(PagamentosServices servicePag) {
		this.servicePag = servicePag;
	}

// =================================================================================
//								Funcoes da view
// =================================================================================

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		initializeDatePicker();

	}

	// Restricoes
	private void initializeNode() {
		// ComboBox
		cboStatus.setItems(pedidoStatus);
		cboProduto.setItems(obsListProd);
		cboProduto.getSelectionModel().selectFirst();
		cboStatus.getSelectionModel().selectFirst();

		// DataPicker
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		dpData.setValue(null);

		// TableView
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id_Prod"));
		tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_Prod"));
		tbcPreco.setCellValueFactory(new PropertyValueFactory<>("preco_Prod"));
		tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("qt_Prod"));

		// TextFields
		Integer n = 1;
		txtQuantidade.setText(n.toString());

		// Constraints
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldInteger(txtQuantidade);
	
		initializeComboBoxProduto();

		if (entityPag != null) {
			System.out.println(entityPag.getId_Pag());
			id_Pag = entityPag.getId_Pag();
		}

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPedidos.prefHeightProperty().bind(stage.heightProperty());

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
		if (servicesItems == null) {
			throw new IllegalStateException("Service Pedidos was null!");
		}

		ObservableList<Produto> list = FXCollections.observableArrayList(produtosList);

		tableViewPedidos.setItems(list);

		initRemoveButtons();
	}// End updateTableView

	public void updateFormData() {
		if(entityPed.getStatus_Ped() != null && entityPed.getStatus_Ped() == PedidoStatus.CANCELADO) {
			btnSalvar.setVisible(false);
		}
		
		if(entityPed.getStatus_Ped() != null && entityPed.getStatus_Ped() == PedidoStatus.PAGO) {
			btnSalvar.setVisible(false);
		}
		
		
		if (entityPed == null) {
			throw new IllegalStateException("Entity (Pedidos) was null");
		}

		txtId.setText(String.valueOf(entityPed.getId_Ped()));
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");

		// LocalDate.ofInstant() -> Converte a data do objeto para a data local
		// .toInstant() -> converte o valor para um instant
		// ZoneId -> Define o fuso-horario
		// .systemDefault() -> pega o fuso-horario da maquina
		if (entityPed.getDt_Ped() != null) {
			dpData.setValue(LocalDate.ofInstant(entityPed.getDt_Ped().toInstant(), ZoneId.systemDefault()));
		}
		if (entityCol == null) {
			throw new IllegalStateException("Entity (Colaborador) was null");
		}
		// Colaborador
		txtColaborador.setText(entityCol.getName());

		// Combobox
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

		// Carrega a lista de items do pedido
		if (entityPed.getId_Ped() != null) {
			pedidoItemsList = servicesItems.findItemsProd(entityPed.getId_Ped());// Retorna uma lista dos items
																					// associados ao pedido

			// Carrega a lista de produtos para visualizacao da tabela
			for (PedidoItems item : pedidoItemsList) {
				Produto prod = servicesProd.findById(item.getId_Prod());
				prod.setQt_Prod(item.getQt_PedIt());
				total += item.getPreco_PedIt() * item.getQt_PedIt();
				produtosList.add(prod);
				updateTableView();
			}

			txtPreco.setText(total.toString());
		}

	}

	// Carrega os dados nos combobox
	public void loadAssociatedObjects() {
		if (servicesCli == null) {
			throw new IllegalStateException("servicesCli was null!");
		}
		List<Cliente> listCli = servicesCli.findAll();
		clienteList = FXCollections.observableArrayList(listCli);

		if (servicesProd == null) {
			throw new IllegalStateException("servicesProd was null!");
		}
		List<Produto> listProd = servicesProd.findAll();
		obsListProd = FXCollections.observableArrayList(listProd);
		cboProduto.setItems(obsListProd);
	}



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

// =================================================================================
//								Metodos	
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

	// Preenche ou atualiza os dados do objeto colaborador
	private Pedidos getFormData(List<PedidoItems> pedidoItemsList, Pagamentos pag) {
		Pedidos obj = new Pedidos();

		ValidationException exception = new ValidationException("Erro ao validar os dados do pedido!");

		for (PedidoItems items : pedidoItemsList) {
			obj.addItem(items);
		}

		// Instant -> data independente de localidade
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

		obj.setId_Pag(pag.getId_Pag());
		obj.setPagamento(pag);

		if (exception.getErrors().size() > 0) {
			throw exception;// Lanca a excessao
		}

		return obj;
	}

	private Pagamentos getFormDataPag() {
		Pagamentos obj = new Pagamentos();

		obj.setId_Pag(id_Pag);
		obj.setPreco_Pag(Utils.tryParseToDouble(txtPreco.getText()));
		obj.setTipo_Pag(TipoDePagamento.DEBITO);

		LocalDate selectedDate = dpData.getValue();
		Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
		obj.setDt_Pag(Date.from(instant));

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
	}// end initRemoveButtons

	private void removeEntity(Produto obj) {
		// Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getId_Prod() + " ?");

		if (result.get() == ButtonType.OK) {
			if (servicesItems == null) {
				throw new IllegalStateException("Service was null");
			}
			try {

				total -= obj.getPreco_Prod() * obj.getQt_Prod();
				produtosList.remove(obj);
				txtPreco.setText(total.toString());

				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void savePedidosItems(Integer id) {
		// Lista dos produtos de um pedido já salvos no DB
		List<PedidoItems> list = servicesItems.findItemsProd(id);

		ValidationException exception = new ValidationException("Erro ao validar os dados do pedido!");

		// Map para facilitar a verificação de existência de itens
		Map<Integer, PedidoItems> dbItemsMap = list.stream()
				.collect(Collectors.toMap(PedidoItems::getId_Prod, item -> item));
		Map<Integer, PedidoItems> newItemsMap = pedidoItemsList.stream()
				.collect(Collectors.toMap(PedidoItems::getId_Prod, item -> item));

		// Atualizar e Adicionar
		for (PedidoItems newItem : pedidoItemsList) {
			newItem.setId_Ped(id);
			if (dbItemsMap.containsKey(newItem.getId_Prod())) {
				// Atualizar o item existente
				PedidoItems existingItem = dbItemsMap.get(newItem.getId_Prod());
				existingItem.setQt_PedIt(newItem.getQt_PedIt());
				existingItem.setPreco_PedIt(newItem.getPreco_PedIt());
				servicesItems.saveOrUpdate(existingItem);
			} else {
				// Adicionar o novo item
				servicesItems.saveOrUpdate(newItem);
			}
		}

		// Remover os itens que não estão na nova lista
		for (PedidoItems dbItem : list) {
			if (!newItemsMap.containsKey(dbItem.getId_Prod())) {
				servicesItems.remove(dbItem);
			}
		}

		// Verificar se a nova lista está vazia
		if (pedidoItemsList.isEmpty()) {
			exception.addErrors("Produto", "Lista vazia! Adicione produtos à lista!");
		}

		if (exception.getErrors().size() > 0) {
			throw exception; // Lança a exceção
		}
	}

	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do pedido!");

		if (produtosList.size() == 0) {
			exception.addErrors("Produto", "Lista de produtos vazia!");
		}

		if (exception.getErrors().size() > 0) {
			throw exception;// Lanca a excessao
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		lblProdutos.setText(fields.contains("Produto") ? errors.get("Produto") : "");
	}//

	private List<PedidoItems> listItems() {
		List<PedidoItems> list = new ArrayList<>();

		for (Produto prod : produtosList) {
			PedidoItems item = new PedidoItems();

			item.setId_Prod(prod.getId_Prod());
			item.setPreco_PedIt(prod.getPreco_Prod());
			item.setQt_PedIt(prod.getQt_Prod());
			item.setProduto(prod);
			list.add(item);
		}

		return list;
	}

	private void baixarEstoque(List<PedidoItems> items) {
		for (PedidoItems item : items) {
			Produto prod = servicesProd.findById(item.getId_Prod());
			prod.subtractProduct(item.getQt_PedIt());
			servicesProd.saveOrUpdate(prod);
		}
	}
	
	private void retomarEstoque(List<PedidoItems> items) {
		for (PedidoItems item : items) {
			Produto prod = servicesProd.findById(item.getId_Prod());
			prod.sumProduct(item.getQt_PedIt());
			servicesProd.saveOrUpdate(prod);
		}
	}

}
