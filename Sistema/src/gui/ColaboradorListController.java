package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Colaborador;
import model.services.ColaboradorServices;

public class ColaboradorListController implements Initializable{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de Colaboradores
	private ColaboradorServices service;
	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<Colaborador> obsList;
	
	
	@FXML
	private Button btnCadastrar;
	@FXML
	private Button btnConsultar;
	
	@FXML
	private TableView<Colaborador> tableViewColaborador;
	@FXML
	private TableColumn<Colaborador, Integer> tableCollumnId;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnName;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnEmail;
	@FXML
	private TableColumn<Colaborador, Integer> tableCollumnCPF_CNPJ;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnEnd;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnEndNum;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnTelefone;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnCelular;

	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	
	
	
	
	
/* ========================================================================
 * 			Metodos da classe
 * ========================================================================
*/	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		
	}

	private void initializeNode() {
		//setCellValueFactory() -> Define como os valores dacoluna sao obtidos dos objetos associados da tabela
		//PropertyValueFactory<>() -> Vinulaos dados de um objeto a coluna da tabela
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("idColab"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableCollumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableCollumnCPF_CNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj_cpf"));
		tableCollumnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
		tableCollumnEndNum.setCellValueFactory(new PropertyValueFactory<>("end_num"));
		tableCollumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableCollumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewColaborador.prefHeightProperty().bind(stage.heightProperty());

	}
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setColaboradorService(ColaboradorServices service) {
		this.service = service;
	}
	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ColaboradorServices
		List<Colaborador> list = service.findAll();
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewColaborador.setItems(obsList);
		
	}
}
