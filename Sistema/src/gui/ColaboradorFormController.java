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
import javafx.scene.control.TextField;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.entities.enums.NivelDeAcesso;
import model.exceptions.ValidationException;
import model.services.ColaboradorServices;
import model.services.EnderecoService;

public class ColaboradorFormController implements Initializable {

// =================================================================================
//								Dependencias	
// =================================================================================	

	private Colaborador entityColab;
	private Endereco entityEnd;
	private ColaboradorServices servicesColab;
	private EnderecoService servicesEnd;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private ObservableList<NivelDeAcesso> obsListNivel = FXCollections.observableArrayList(NivelDeAcesso.values());
// =================================================================================
// 						Atibutos do Colaborador	
// =================================================================================

	private Integer id_Col;

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
	@FXML
	private TextField txtUsuario;
	@FXML
	private TextField txtSenha;
	@FXML
	private ComboBox cboLevel;

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
	@FXML
	private Button btnSenha;

// =================================================================================
//							Atributos dos Labels errors	
//=================================================================================

	// Colaborador
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
	@FXML
	private Label lblErrorUsuario;
	@FXML
	private Label lblErrorLevel;
	@FXML
	private Label lblErrorSenha;

	// Endereco
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
		if (entityEnd == null) {
			throw new IllegalStateException("Entity (entityEnd) was null");
		}
		if (entityColab == null) {
			throw new IllegalStateException("Entity (entityColab) was null");
		}
		try {
			fieldesValidation();// Validacao dos campos
			// Endereco
			entityEnd = getFormDataEnd();
			servicesEnd.saveOrUpdate(entityEnd);
			// Colaborador
			entityColab = getFormDataColab(entityEnd);
			servicesColab.saveOrUpdate(entityColab);

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
//						Funcoes para injecao de dependencia	
// =================================================================================

	public void setColaborador(Colaborador entity) {
		this.entityColab = entity;
	}

	public void setEndereco(Endereco entity) {
		this.entityEnd = entity;
	}

	public void setColaboradorServices(ColaboradorServices servicesColab) {
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

	// Restricoes
	private void initializeNode() {
		// Endereco
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldMaxLength(txtNumero, 10);
		Constraints.setTextFieldInteger(txtCep);
		Constraints.setTextFieldMaxLength(txtCep, 8);
		Constraints.setTextFieldMaxLength(txtRua, 45);
		Constraints.setTextFieldMaxLength(txtBairro, 45);
		Constraints.setTextFieldMaxLength(txtCidade, 45);

		// Colaborador
		Constraints.setTextFieldInteger(txtCelular);
		Constraints.setTextFieldMaxLength(txtCelular, 11);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtTelefone, 10);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		//Constraints.setTextFieldOnlyLetters(txtNome);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Constraints.setTextFieldInteger(txtCpf);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
		Constraints.setTextFieldMaxLength(txtUsuario, 20);
		Constraints.setTextFieldMaxLength(txtSenha, 20);
		
		cboLevel.setItems(obsListNivel);
		cboLevel.getSelectionModel().selectFirst();

	}

	public void updateFormData() {
		if (entityColab == null) {
			throw new IllegalStateException("Entity (Colaborador) was null");
		}
		// Colaborador
		if (entityColab.getIdColab() != null) {
			id_Col = entityColab.getIdColab();
		}
		
		if (entityColab.getLevel_Access() == null) {
			cboLevel.getSelectionModel().selectFirst();
		} else {
			cboLevel.setValue(entityColab.getLevel_Access() );
		}

		txtNome.setText(entityColab.getName());
		txtEmail.setText(entityColab.getEmail());
		txtCpf.setText(entityColab.getCnpj_cpf());
		txtTelefone.setText(String.valueOf(entityColab.getTelefone()));
		txtCelular.setText(String.valueOf(entityColab.getCelular()));
		txtUsuario.setText(entityColab.getUser_Col());
		txtSenha.setText(entityColab.getUser_Senha());

		if (entityEnd == null) {
			throw new IllegalStateException("Entity (Endereco) was null");
		}
		// Endereco
		if (entityEnd.getId_End() != null) {
			id_End = entityEnd.getId_End();
		}
		txtRua.setText(entityEnd.getRua_End());
		txtBairro.setText(entityEnd.getBairro_End());
		txtCidade.setText(entityEnd.getCidade_End());
		txtCep.setText(entityEnd.getCep_End());
		txtNumero.setText(String.valueOf(entityEnd.getNum_End()));
	}

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
	private Colaborador getFormDataColab(Endereco entity) {
		Colaborador obj = new Colaborador();

		obj.setIdColab(id_Col);// O tryParseInt() ja faz a verificacao
		obj.setName(txtNome.getText());
		obj.setEmail(txtEmail.getText());
		obj.setCnpj_cpf(txtCpf.getText());
		obj.setTelefone(txtTelefone.getText());
		obj.setCelular(txtCelular.getText());
		obj.setUser_Col(txtUsuario.getText());
		obj.setLevel_Access(NivelDeAcesso.valueOf(cboLevel.getValue().toString()));

		obj.setUser_Senha(txtSenha.getText());

		obj.setId_End(entity.getId_End());// Adiciona o id do endereco
		obj.setEndereco(entity);// Adiciona adependencia do endereco

		return obj;
	}

	private Endereco getFormDataEnd() {
		Endereco obj = new Endereco();

		obj.setId_End(id_End);
		obj.setRua_End(txtRua.getText());
		obj.setBairro_End(txtBairro.getText());
		obj.setCidade_End(txtCidade.getText());
		obj.setCep_End(txtCep.getText());
		obj.setNum_End(Utils.tryParseToInt(txtNumero.getText()));

		return obj;
	}

	private void fieldesValidation() {
		ValidationException exception = new ValidationException("Erro ao validar os dados do colaborador!");

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("Nome", "Informe o nome do colaborador!");
		}

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addErrors("Email", "Informe o email do colaborador!");
		}

		if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
			exception.addErrors("CPF", "Informe o CPF!");
		}

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addErrors("Telefone", "Informe o telefone!");
		}

		if (txtCelular.getText() == null || txtCelular.getText().trim().equals("")) {
			exception.addErrors("Celular", "Informe o celular!");
		}

		if (txtUsuario.getText() == null || txtUsuario.getText().trim().equals("")) {
			exception.addErrors("Usuario", "Informe um usuário!");
		}

		if (txtSenha.getText() == null || txtSenha.getText().trim().equals("")) {
			exception.addErrors("Senha", "Informe a senha!");
		}

		if (cboLevel.getSelectionModel().getSelectedItem() == null) {
			exception.addErrors("Nivel", "Selecione um nivel!");
		}

		if (txtRua.getText() == null || txtRua.getText().trim().equals("")) {
			exception.addErrors("Rua", "Informe a rua!");
		}

		if (txtBairro.getText() == null || txtBairro.getText().trim().equals("")) {
			exception.addErrors("Bairro", "Informe o bairro!");
		}

		if (txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addErrors("Cidade", "Informe a cidade!");
		}

		if (txtCep.getText() == null || txtCep.getText().trim().equals("")) {
			exception.addErrors("CEP", "Informe o CEP!");
		}

		if (txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addErrors("Numero", "Informe o número!");
		}

		if (exception.getErrors().size() > 0) {
			throw exception;// Lanca a excessao
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		lblErrorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		lblErrorEmail.setText(fields.contains("Email") ? errors.get("Email") : "");
		lblErrorCPF.setText(fields.contains("CPF") ? errors.get("CPF") : "");
		lblErrorTelefone.setText(fields.contains("Telefone") ? errors.get("Telefone") : "");
		lblErrorCelular.setText(fields.contains("Celular") ? errors.get("Celular") : "");
		lblErrorUsuario.setText(fields.contains("Usuario") ? errors.get("Usuario") : "");
		lblErrorLevel.setText(fields.contains("Nivel") ? errors.get("Nivel") : "");
		lblErrorSenha.setText(fields.contains("Senha") ? errors.get("Senha") : "");

		lblErrorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		lblErrorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		lblErrorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		lblErrorCEP.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		lblErrorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");

	}//

}
