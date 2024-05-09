package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.services.ColaboradorServices;
import model.services.EnderecoService;

public class ColaboradorFormController implements Initializable{

// =================================================================================
//								Dependencias	
// =================================================================================	
	
	private Colaborador entityColab;
	private Endereco entityEnd;
	private ColaboradorServices servicesColab;
	private EnderecoService servicesEnd;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
// =================================================================================
// 						Atibutos do Colaborador	
// =================================================================================
	
	@FXML
	private TextField txtIdCol;
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
	@FXML
	private Button btnSenha;

	
// =================================================================================
//							Funcoes dos controles	
// =================================================================================
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		entityEnd = getFormDataEnd();
		servicesEnd.saveOrUpdate(entityEnd);
		
		entityColab = getFormDataColab(entityEnd);
		servicesColab.saveOrUpdate(entityColab);
		
		Utils.currentStage(event).close();
		
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
	
	//Restricoes
	private void initializeNode() {
		//Endereco
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtCep);
		Constraints.setTextFieldMaxLength(txtRua, 45);
		Constraints.setTextFieldMaxLength(txtBairro, 45);
		Constraints.setTextFieldMaxLength(txtCidade, 45);
		
		//Colaborador
		Constraints.setTextFieldInteger(txtIdCol);
		Constraints.setTextFieldInteger(txtCelular);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Constraints.setTextFieldMaxLength(txtCpf, 14);
		Constraints.setTextFieldMaxLength(txtUsuario, 20);
		
		
	}
	
	
	public void updateFormData() {
		if(entityColab == null) {
			throw new IllegalStateException("Entity (Colaborador) was null");
		}
		//Colaborador
		txtIdCol.setText(String.valueOf(entityColab.getIdColab()));
		txtNome.setText(entityColab.getName());
		txtEmail.setText(entityColab.getEmail());
		txtCpf.setText(entityColab.getCnpj_cpf());
		txtTelefone.setText(String.valueOf(entityColab.getTelefone()));
		txtCelular.setText(String.valueOf(entityColab.getCelular()));
		txtUsuario.setText(entityColab.getUser_Col());
		
		if(entityEnd == null) {
			throw new IllegalStateException("Entity (Endereco) was null");
		}
		//Endereco
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
	private Colaborador getFormDataColab(Endereco entity) {
		Colaborador obj = new Colaborador();
		
		obj.setIdColab(Utils.tryParseToInt(txtIdCol.getText()));
		obj.setName(txtNome.getText());
		obj.setEmail(txtEmail.getText());
		obj.setCnpj_cpf(txtCpf.getText());
		obj.setTelefone(Utils.tryParseToInt(txtTelefone.getText()));
		obj.setCelular(Utils.tryParseToInt(txtCelular.getText()));
		obj.setUser_Col(txtUsuario.getText());
		obj.setUser_Senha("1234567@D");
		
		obj.setId_End(entity.getId_End());//Adiciona o id do endereco
		
		return obj;
	}
	
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
}
