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
	
	private Integer id_Prod;
	private Integer qt_Min = 0;
	private Double preco_Forn = 0.0;
	
	@FXML
	private TextField txtNome;
	@FXML
	private TextArea txtDescricao;
	@FXML
	private TextField txtPreco;
	@FXML
	private TextField txtEstoque;

		
	
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
	@FXML
	private Label lblErrorEstoque;

	
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
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextAreaMaxLength(txtDescricao, 50);
		Constraints.setTextFieldDouble(txtPreco);
		Constraints.setTextFieldMaxLength(txtPreco, 10);
		Constraints.setTextFieldInteger(txtEstoque);
		Constraints.setTextFieldMaxLength(txtEstoque, 10);
		
		
	}
	
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity (Produto) was null");
		}
		//Produto
		if(entity.getId_Prod() != null) {
			id_Prod = entity.getId_Prod();
		}
		txtNome.setText(entity.getNome_Prod());
		txtDescricao.setText(entity.getDesc_Prod());
		txtPreco.setText(String.valueOf(entity.getPreco_Prod()));
		if(entity.getQtd_Estocado() == null) {
			Integer qt = 0;
			txtEstoque.setText(String.valueOf(qt));
		}
		txtEstoque.setText(String.valueOf(entity.getQtd_Estocado()));
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
		
		ValidationException exception = new ValidationException("Erro ao validar os dados do Produto!");
		
		obj.setId_Prod(id_Prod);//O tryParseInt() ja faz a verificacao
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", "Preencha o nome do produto!");
		}
		obj.setNome_Prod(txtNome.getText());
		
		if(txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addErrors("Descricao", "Insira uma descicao!");
		}
		obj.setDesc_Prod(txtDescricao.getText());
		
		if(txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.addErrors("Preco", "Informe o preco!");
		}
		obj.setPreco_Prod(Utils.tryParseToDouble(txtPreco.getText()));
		
		if(txtEstoque.getText() == null || txtEstoque.getText().trim().equals("")) {
			exception.addErrors("Estoque", "Informe um valor (zero ou +)!");
		}
		obj.setQtd_Estocado(Utils.tryParseToInt(txtEstoque.getText()));
		
		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
		
		return obj;
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorDescricao.setText(fields.contains("Descricao") ? errors.get("Descricao") : "");
		lblErrorPreco.setText(fields.contains("Preco") ? errors.get("Preco") : "");
		lblErrorEstoque.setText(fields.contains("Estoque") ? errors.get("Estoque") : "");
		
	}//

}













