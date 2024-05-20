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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Pagamentos;
import model.entities.PedidoItems;
import model.entities.Pedidos;
import model.entities.Produto;
import model.entities.enums.PedidoStatus;
import model.entities.enums.TipoDePagamento;
import model.exceptions.ValidationException;
import model.services.PagamentosServices;
import model.services.PedidoItemsServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;

public class PagamentosFormController implements Initializable {

// =================================================================================
//								Dependencias	
// =================================================================================	

	// Entidades
	private Pagamentos entityPag;
	private Pedidos entityPed;

	// Servicos
	private PedidosServices servicePed;
	private PagamentosServices servicePag;

	private ObservableList<TipoDePagamento> obsListTipo = FXCollections.observableArrayList(TipoDePagamento.values());

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// =================================================================================
// 							Atibutos do pagamento	
// =================================================================================

	@FXML
	private TextField txtId;
	@FXML
	private DatePicker dpData;
	@FXML
	private TextField txtPreco;
	@FXML
	private TextField txtNroPedido;
	@FXML
	private ComboBox<TipoDePagamento> cboTipo;

// =================================================================================
//							Atibutos controles genericos	
// =================================================================================

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;

// =================================================================================
//							Atributos dos Labels errors	
//=================================================================================

	// Pedidos
	@FXML
	private Label lblErrorTipo;
	@FXML
	private Label lblErrorNroPed;
	@FXML
	private Label lblErrorData;
	@FXML
	private Label lblErrorPreco;

// =================================================================================
//							Funcoes dos controles	
// =================================================================================

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entityPag == null) {
			throw new IllegalStateException("Entity (entityPag) was null");
		}
		if (entityPed == null) {
			throw new IllegalStateException("Entity (entityPed) was null");
		}
		try {

			fieldesValidation();
			
			if(entityPed.getStatus_Ped() == PedidoStatus.CANCELADO) {
				Alerts.showAlerts("Aviso", "Pagamentos", "Pedido Cancelado!\n Operacao interrompida!", AlertType.WARNING);
				Utils.currentStage(event).close();
				return;
			}
			if(entityPed.getStatus_Ped() == PedidoStatus.PAGO) {
				Alerts.showAlerts("Aviso", "Pagamentos", "Pedido Pago!\n Operacao interrompida!", AlertType.WARNING);
				Utils.currentStage(event).close();
				return;
			}
			
			if(entityPed.getStatus_Ped() == PedidoStatus.PAGO) {
				Utils.currentStage(event).close();

			}else {
				entityPed.setStatus_Ped(PedidoStatus.PAGO);
				entityPag = getFormDataPagamentos(entityPed);
				
				List<PedidoItems> list = getFormDataItems(entityPed);
				baixarEstoque(list);
				
				servicePag.saveOrUpdate(entityPag);
				entityPed.setId_Pag(entityPag.getId_Pag());
				
				servicePed.saveOrUpdate(entityPed);

				notifyDataChangeListeners();

				Utils.currentStage(event).close();// Fecha o formulario
			}
			

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

// =================================================================================
//						Funcoes para injecao de dependencia	
// =================================================================================

	// Pedidos
	public void setPedidos(Pedidos entity) {
		this.entityPed = entity;
	}

	public void setPedidosServices(PedidosServices servicePed) {
		this.servicePed = servicePed;
	}

	// Pagamentos
	public void setPagamentos(Pagamentos entity) {
		this.entityPag = entity;
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
		cboTipo.setItems(obsListTipo);
		cboTipo.getSelectionModel().selectFirst();

		// DataPicker
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		dpData.setValue(null);

		// Constraints
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldDouble(txtPreco);
		Constraints.setTextFieldInteger(txtNroPedido);

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


	public void updateFormData() {
		if (entityPag == null) {
			throw new IllegalStateException("Entity (Pagamento) was null");
		}
		if (entityPed == null) {
			throw new IllegalStateException("Entity (Pedidos) was null");
		}
		
		txtId.setText(String.valueOf(entityPag.getId_Pag()));
		txtNroPedido.setText(entityPed.getId_Ped().toString());
		txtPreco.setText(entityPed.getPreco_Ped().toString());
		
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");
		// LocalDate.ofInstant() -> Converte a data do objeto para a data local
		// .toInstant() -> converte o valor para um instant
		// ZoneId -> Define o fuso-horario
		// .systemDefault() -> pega o fuso-horario da maquina
		
		if (entityPag.getDt_Pag() != null) {
			dpData.setValue(LocalDate.ofInstant(entityPag.getDt_Pag().toInstant(), ZoneId.systemDefault()));
		}
		
		if(entityPag.getTipo_Pag() != null) {
			cboTipo.setValue(entityPag.getTipo_Pag());
		}else {
			cboTipo.getSelectionModel().selectFirst();
		}
	}

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


	private Pagamentos getFormDataPagamentos(Pedidos ped) {
		Pagamentos obj = new Pagamentos();

		obj.setId_Pag(Utils.tryParseToInt(txtId.getText()));
		obj.setPreco_Pag(Utils.tryParseToDouble(txtPreco.getText()));

		// Instant -> data independente de localidade
		LocalDate selectedDate = dpData.getValue();
		Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
		obj.setDt_Pag(Date.from(instant));
		
		obj.setNro_Ped(entityPed.getId_Ped());

		obj.setTipo_Pag(TipoDePagamento.valueOf(cboTipo.getValue().toString()));

		return obj;
	}

	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");


		if (exception.getErrors().size() > 0) {
			throw exception;// Lanca a excessao
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		lblErrorNroPed.setText(fields.contains("Produto") ? errors.get("Produto") : "");
	}//
	
	private List<PedidoItems> getFormDataItems(Pedidos ped){
		List<PedidoItems> list = new ArrayList<>();
		
		PedidoItemsServices serviceItems = new PedidoItemsServices();
		list = serviceItems.findItemsProd(ped.getId_Ped());
		
		return list;
	}
	
	
	private void baixarEstoque(List<PedidoItems> items) {
		for (PedidoItems item : items) {
			ProdutoServices servicesProd = new ProdutoServices();
			Produto prod = servicesProd.findById(item.getId_Prod());
			prod.subtractProduct(item.getQt_PedIt());
			servicesProd.saveOrUpdate(prod);
		}
	}
	
	private void retomarEstoque(List<PedidoItems> items) {
		for (PedidoItems item : items) {
			ProdutoServices servicesProd = new ProdutoServices();
			Produto prod = servicesProd.findById(item.getId_Prod());
			prod.sumProduct(item.getQt_PedIt());
			servicesProd.saveOrUpdate(prod);
		}
	}
}
