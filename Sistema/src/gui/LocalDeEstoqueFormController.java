package gui;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.LocalDeEstoque;
import model.entities.enums.LocalStatus;
import model.exceptions.ValidationException;
import model.services.LocalDeEstoqueServices;

public class LocalDeEstoqueFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private LocalDeEstoque entity;
	private LocalDeEstoqueServices services;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do LocalDeEstoque	
// =================================================================================
	
	@FXML
	private TextField txtIdLocal;
	@FXML
	private TextField txtNome;
	@FXML
	private TextArea txtDescricao;
	@FXML
	private ComboBox<LocalStatus> cboStatus;//ComboBox 
	
	private ObservableList<LocalStatus> statusLis = FXCollections.observableArrayList(LocalStatus.values());//Pega a colecao de valores do enum 

		
	
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

	//LocalDeEstoque
	@FXML
	private Label lblErrorNome;
	@FXML
	private Label lblErrorDescricao;
	@FXML
	private Label lblErrorStatus;

	
// =================================================================================
//							Funcoes dos controles	
// =================================================================================
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {	
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		try {
			entity = getFormData();
			services.saveOrUpdate(entity);
			
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
	
	
// =================================================================================
//						Funcoes para injecao de dependencia	
// =================================================================================
	
	public void setLocalDeEstoque(LocalDeEstoque entity) {
		this.entity = entity;
	}
	
	public void setLocalDeEstoqueServices(LocalDeEstoqueServices services) {
		this.services = services;
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
		
		//LocalDeEstoque
		Constraints.setTextFieldInteger(txtIdLocal);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextAreaMaxLength(txtDescricao, 50);

		cboStatus.setItems(statusLis);//Define os items do comboBox de acordo com a observableList
		cboStatus.setValue(LocalStatus.ATIVADO);
		cboStatus.getSelectionModel().selectFirst();
		
	}
	
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity (LocalDeEstoque) was null");
		}
		//LocalDeEstoque
		txtIdLocal.setText(String.valueOf(entity.getId_Local()));
		txtNome.setText(entity.getNome_Local());
		txtDescricao.setText(entity.getDesc_Local());
		cboStatus.setValue(entity.getSit_Local());
	}
	
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
	private LocalDeEstoque getFormData() {
		LocalDeEstoque obj = new LocalDeEstoque();
		
		ValidationException exception = new ValidationException("Erro ao validar os dados do Local!");
		
		obj.setId_Local(Utils.tryParseToInt(txtIdLocal.getText()));//O tryParseInt() ja faz a verificacao
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", "Field can't be empty!");
		}
		obj.setNome_Local(txtNome.getText());
		
		if(txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addErrors("Descricao", "Field can't be empty!");
		}
		obj.setDesc_Local(txtDescricao.getText());
		
		if (cboStatus.getSelectionModel().getSelectedItem() == null) {//Verifica se e nulo
		    exception.addErrors("Status", "Field can't be empty!");
		} 
		obj.setSit_Local(cboStatus.getValue());
		 
		
		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
		
		return obj;
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorDescricao.setText(fields.contains("Descricao") ? errors.get("Descricao") : "");
		lblErrorStatus.setText(fields.contains("Status") ? errors.get("Status") : "");
		
		
	}//

}













