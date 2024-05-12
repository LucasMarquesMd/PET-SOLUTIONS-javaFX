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
import javafx.scene.control.TextField;
import model.entities.Fornecedor;
import model.entities.Endereco;
import model.exceptions.ValidationException;
import model.services.FornecedorServices;
import model.services.EnderecoService;

public class FornecedorFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private Fornecedor entityForn;//Entidade fornecedor, instanciada pela FornecedorListController
	private Endereco entityEnd;//Entidade endereco, instanciada pela FornecedorListController
	private FornecedorServices servicesColab;
	private EnderecoService servicesEnd;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do Fornecedor	
// =================================================================================
	
	@FXML
	private TextField txtIdCol;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtCnpj;
	@FXML
	private TextField txtTelefone;

	

// =================================================================================
//							Atibutos do Endereco	
// =================================================================================

	@FXML
	private TextField txtIdEnd;
	@FXML
	private TextField txtRua;
	@FXML
	private TextField txtBairro;
	@FXML
	private TextField txtCidade;
	@FXML
	private TextField txtCep;
	@FXML
	private TextField txtNumero;
	
	
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

	//Fornecedor
	@FXML
	private Label lblErrorNome;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorCPF;
	@FXML
	private Label lblErrorTelefone;
	
	
	//Endereco
	@FXML
	private Label lblErrorRua;
	@FXML
	private Label lblErrorBairro;
	@FXML
	private Label lblErrorCidade;
	@FXML
	private Label lblErrorCEP;
	@FXML
	private Label lblErrorNumero;
	@FXML
	private Label lblErrorLevel;
	
// =================================================================================
//							Funcoes dos controles	
// =================================================================================
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entityEnd == null) {
			throw new IllegalStateException("Entity (entityEnd) was null");
		}		
		if(entityForn == null) {
			throw new IllegalStateException("Entity (entityForn) was null");
		}
		try {
			fieldesValidation();//Validacao dos campos
			//Endereco
			entityEnd = getFormDataEnd();
			servicesEnd.saveOrUpdate(entityEnd);
			//Fornecedor
			entityForn = getFormDataColab(entityEnd);
			servicesColab.saveOrUpdate(entityForn);
			
			notifyDataChangeListeners();//Notofica a FornecedorListController para atualizar a tabela
			
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
	
	public void setFornecedor(Fornecedor entity) {
		this.entityForn = entity;
	}
	
	public void setEndereco(Endereco entity) {
		this.entityEnd = entity;
	}
	
	public void setFornecedorServices(FornecedorServices servicesColab) {
		this.servicesColab = servicesColab;
	}
	
	public void setEnderecoService(EnderecoService servicesEnd) {
		this.servicesEnd = servicesEnd;
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
		//Endereco
		Constraints.setTextFieldInteger(txtIdEnd);
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtCep);
		Constraints.setTextFieldMaxLength(txtRua, 45);
		Constraints.setTextFieldMaxLength(txtBairro, 45);
		Constraints.setTextFieldMaxLength(txtCidade, 45);
		
		//Fornecedor
		Constraints.setTextFieldInteger(txtIdCol);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Constraints.setTextFieldMaxLength(txtCnpj, 14);
		
	}
	
	//Adiciona os valores dos objetos no formulario
	//Se o objjeto nao contiver dados -> os campos ficam em branco
	public void updateFormData() {
		if(entityForn == null) {
			throw new IllegalStateException("Entity (Fornecedor) was null");
		}
		//Fornecedor
		txtIdCol.setText(String.valueOf(entityForn.getId_Forn()));
		txtNome.setText(entityForn.getNome_Forn());
		txtEmail.setText(entityForn.getEmail_Forn());
		txtCnpj.setText(entityForn.getCnpj_Forn());
		txtTelefone.setText(String.valueOf(entityForn.getTel_Forn()));;
		
		
		if(entityEnd == null) {
			throw new IllegalStateException("Entity (Endereco) was null");
		}
		//Endereco
		txtIdEnd.setText(String.valueOf(entityEnd.getId_End()));
		txtRua.setText(entityEnd.getRua_End());
		txtBairro.setText(entityEnd.getBairro_End());
		txtCidade.setText(entityEnd.getCidade_End());
		txtCep.setText(String.valueOf(entityEnd.getCep_End()));
		txtNumero.setText(String.valueOf(entityEnd.getNum_End()));
	}
	
	//Quais quer objetos que implementarem a inteface, podem se inscrever para receber o evento do controller
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	//Notifica
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();//Invoca o metodo nas classes que implementaram a interface
		}
	}
	
	//Preenche ou atualiza os dados do objeto colaborador
	private Fornecedor getFormDataColab(Endereco entity) {
		Fornecedor obj = new Fornecedor();

		obj.setId_Forn(Utils.tryParseToInt(txtIdCol.getText()));//O tryParseInt() ja faz a verificacao
		obj.setNome_Forn(txtNome.getText());
		obj.setEmail_Forn(txtEmail.getText());
		obj.setCnpj_Forn(txtCnpj.getText());
		obj.setTel_Forn(Utils.tryParseToInt(txtTelefone.getText()));
		
		obj.setId_End(entity.getId_End());//Adiciona o id do endereco
		obj.setEndereco(entity);//Adiciona adependencia do endereco
		
		return obj;
	}
	
	//Instancia o objeto endereco
	private Endereco getFormDataEnd() {
		Endereco obj = new Endereco();			
		
		obj.setId_End(Utils.tryParseToInt(txtIdEnd.getText()));
		obj.setRua_End(txtRua.getText());
		obj.setBairro_End(txtBairro.getText());
		obj.setCidade_End(txtCidade.getText());
		obj.setCep_End(Utils.tryParseToInt(txtCep.getText()));
		obj.setNum_End(Utils.tryParseToInt(txtNumero.getText()));
		
		return obj;
	}
	
	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", "Field can't be empty!");
		}

		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addErrors("Email", "Field can't be empty!");
		}

		
		if(txtCnpj.getText() == null || txtCnpj.getText().trim().equals("")) {
			exception.addErrors("CNPJ", "Field can't be empty!");
		}

		
		if(txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addErrors("Telefone", "Field can't be empty!");
		}
		
		
		if(txtRua.getText() == null || txtRua.getText().trim().equals("")) {
			exception.addErrors("Rua", "Field can't be empty!");
		}

		
		if(txtBairro.getText() == null || txtBairro.getText().trim().equals("")) {
			exception.addErrors("Bairro", "Field can't be empty!");
		}

		
		if(txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addErrors("Cidade", "Field can't be empty!");
		}

		
		if(txtCep.getText() == null || txtCep.getText().trim().equals("")) {
			exception.addErrors("CEP", "Field can't be empty!");
		}

		
		if(txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addErrors("Numero", "Field can't be empty!");
		}


		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorEmail.setText(fields.contains("Email") ? errors.get("Email") : "");
		lblErrorCPF.setText(fields.contains("CNPJ") ? errors.get("CNPJ") : "");
		lblErrorTelefone.setText(fields.contains("Telefone") ? errors.get("Telefone") : "");
		lblErrorLevel.setText(fields.contains("Nivel") ? errors.get("Usuario") : "");
		
		lblErrorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		lblErrorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		lblErrorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		lblErrorCEP.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		lblErrorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");
		
	}//

}













