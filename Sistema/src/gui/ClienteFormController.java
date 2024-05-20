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
import model.entities.Cliente;
import model.entities.Endereco;
import model.exceptions.ValidationException;
import model.services.ClienteServices;
import model.services.EnderecoService;

public class ClienteFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private Cliente entityCli;
	private Endereco entityEnd;
	private ClienteServices servicesColab;
	private EnderecoService servicesEnd;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do Cliente	
// =================================================================================
	
	private Integer id_Cli;
	
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtCpf;
	@FXML
	private TextField txtTelefone;
	@FXML
	private TextField txtCelular;
	

// =================================================================================
//							Atibutos do Endereco	
// =================================================================================

	private Integer id_End;
	
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

	//Cliente
	@FXML
	private Label lblErrorNome;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorCPF;
	@FXML
	private Label lblErrorTelefone;
	@FXML
	private Label lblErrorCelular;
	
	
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
	
// =================================================================================
//							Funcoes dos controles	
// =================================================================================
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entityEnd == null) {
			throw new IllegalStateException("Entity (entityEnd) was null");
		}		
		if(entityCli == null) {
			throw new IllegalStateException("Entity (entityCli) was null");
		}
		try {
			fieldesValidation();//Validacao dos campos
			//Endereco
			entityEnd = getFormDataEnd();
			servicesEnd.saveOrUpdate(entityEnd);
			//Cliente
			entityCli = getFormDataCli(entityEnd);
			servicesColab.saveOrUpdate(entityCli);
			
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
	
	@FXML
	public void onBtnSenhaAction() {
		System.out.println("onBtnSenhaAction - clik");
	}
	
// =================================================================================
//						Funcoes para injecao de dependencia	
// =================================================================================
	
	public void setCliente(Cliente entity) {
		this.entityCli = entity;
	}
	
	public void setEndereco(Endereco entity) {
		this.entityEnd = entity;
	}
	
	public void setClienteServices(ClienteServices servicesColab) {
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
		//Endeco
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtCep);
		Constraints.setTextFieldMaxLength(txtCep, 8);
		Constraints.setTextFieldMaxLength(txtRua, 45);
		Constraints.setTextFieldMaxLength(txtBairro, 45);
		Constraints.setTextFieldMaxLength(txtCidade, 45);
		
		//Cliente
		Constraints.setTextFieldInteger(txtCelular);
		Constraints.setTextFieldMaxLength(txtCelular, 11);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtTelefone, 10);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Constraints.setTextFieldInteger(txtCpf);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
		
	}
	
	
	public void updateFormData() {
		if(entityCli == null) {
			throw new IllegalStateException("Entity (Cliente) was null");
		}
		//Cliente
		if(entityCli.getId_Cli() != null) {
			id_Cli = entityCli.getId_Cli();
		}
		txtNome.setText(entityCli.getNome_Cli());
		txtEmail.setText(entityCli.getEmail_Cli());
		txtCpf.setText(entityCli.getCpf_Cli());
		txtTelefone.setText(String.valueOf(entityCli.getTel_Cli()));
		txtCelular.setText(String.valueOf(entityCli.getCel_Cli()));
		
		
		if(entityEnd == null) {
			throw new IllegalStateException("Entity (Endereco) was null");
		}
		//Endereco
		if(entityEnd.getId_End() != null) {
			id_Cli = entityEnd.getId_End();
		}
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
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();//Invoca o metodo nas classes que implementaram a interface
		}
	}
	
	//Preenche ou atualiza os dados do objeto colaborador
	private Cliente getFormDataCli(Endereco entity) {
		Cliente obj = new Cliente();

		obj.setId_Cli(id_Cli);//O tryParseInt() ja faz a verificacao
		obj.setNome_Cli(txtNome.getText());
		obj.setEmail_Cli(txtEmail.getText());
		obj.setCpf_Cli(txtCpf.getText());
		obj.setTel_Cli(txtTelefone.getText());
		obj.setCel_Cli(txtCelular.getText());
		
		obj.setId_End(entity.getId_End());//Adiciona o id do endereco
		obj.setEndereco(entity);//Adiciona adependencia do endereco
		
		return obj;
	}
	
	private Endereco getFormDataEnd() {
		Endereco obj = new Endereco();			
		
		obj.setId_End(id_End);
		obj.setRua_End(txtRua.getText());
		obj.setBairro_End(txtBairro.getText());
		obj.setCidade_End(txtCidade.getText());
		obj.setCep_End(Utils.tryParseToInt(txtCep.getText()));
		obj.setNum_End(Utils.tryParseToInt(txtNumero.getText()));
		
		return obj;
	}
	
	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do Cliente!");
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", " Informe nome do cliente!");
		}

		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addErrors("Email", "Informe o email!");
		}

		
		if(txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
			exception.addErrors("CPF", "Informe o CPF!");
		}

		
		if(txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addErrors("Telefone", "Informe o telefone!");
		}

		
		if(txtCelular.getText() == null || txtCelular.getText().trim().equals("")) {
			exception.addErrors("Celular", "Informe o celular!");
		}
		
		
		if(txtRua.getText() == null || txtRua.getText().trim().equals("")) {
			exception.addErrors("Rua", "Informe a rua!");
		}

		
		if(txtBairro.getText() == null || txtBairro.getText().trim().equals("")) {
			exception.addErrors("Bairro", "Informe o bairro!");
		}

		
		if(txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addErrors("Cidade", "Informe a cidade!");
		}

		
		if(txtCep.getText() == null || txtCep.getText().trim().equals("")) {
			exception.addErrors("CEP", "Digite o CEP!");
		}

		
		if(txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addErrors("Numero", "Digite o numero!");
		}


		if(exception.getErrors().size() > 0) {
			throw exception;//Lanca a excessao
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorEmail.setText(fields.contains("Email") ? errors.get("Email") : "");
		lblErrorCPF.setText(fields.contains("CPF") ? errors.get("CPF") : "");
		lblErrorTelefone.setText(fields.contains("Telefone") ? errors.get("Telefone") : "");
		lblErrorCelular.setText(fields.contains("Celular") ? errors.get("Celular") : "");
		
		lblErrorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		lblErrorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		lblErrorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		lblErrorCEP.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		lblErrorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");
		
	}//

}













