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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.ProdutoServices;

public class ProdutoFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private Produto entity;
	private ProdutoServices services;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do Produto	
// =================================================================================
	
	@FXML
	private TextField txtIdProd;
	@FXML
	private TextField txtNome;
	@FXML
	private TextArea txtDescricao;
	@FXML
	private TextField txtPreco;

		
	
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

	//Produto
	@FXML
	private Label lblErrorNome;
	@FXML
	private Label lblErrorDescricao;
	@FXML
	private Label lblErrorPreco;

	
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
	
	public void setProduto(Produto entity) {
		this.entity = entity;
	}
	
	public void setProdutoServices(ProdutoServices services) {
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
		
		//Produto
		Constraints.setTextFieldInteger(txtIdProd);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextAreaMaxLength(txtDescricao, 50);
		Constraints.setTextFieldDouble(txtPreco);
		
		
	}
	
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity (Produto) was null");
		}
		//Produto
		txtIdProd.setText(String.valueOf(entity.getId_Prod()));
		txtNome.setText(entity.getNome_Prod());
		txtDescricao.setText(entity.getDesc_Prod());
		txtPreco.setText(String.valueOf(entity.getPreco_Prod()));
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
	private Produto getFormData() {
		Produto obj = new Produto();
		
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");
		
		obj.setId_Prod(Utils.tryParseToInt(txtIdProd.getText()));//O tryParseInt() ja faz a verificacao
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", "Field can't be empty!");
		}
		obj.setNome_Prod(txtNome.getText());
		
		if(txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addErrors("Descricao", "Field can't be empty!");
		}
		obj.setDesc_Prod(txtDescricao.getText());
		
		if(txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.addErrors("Preco", "Field can't be empty!");
		}
		obj.setPreco_Prod(Utils.tryParseToDouble(txtPreco.getText()));
		
		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
		
		return obj;
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorDescricao.setText(fields.contains("Descricao") ? errors.get("Nome") : "");
		lblErrorPreco.setText(fields.contains("Preco") ? errors.get("Nome") : "");
		
		
	}//

}













