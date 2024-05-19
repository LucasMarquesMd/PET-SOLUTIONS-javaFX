package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.services.ColaboradorServices;
import model.services.EnderecoService;

public class ColaboradorListController implements Initializable, DataChangeListener{

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
	private TextField txtNome;
	
	@FXML
	private TableView<Colaborador> tableViewColaborador = new TableView<>();
	@FXML
	private TableColumn<Colaborador, Integer> tableCollumnId;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnName;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnEmail;
	@FXML
	private TableColumn<Colaborador, Integer> tableCollumnCPF_CNPJ;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnTelefone;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnCelular;
	@FXML
	private TableColumn<Colaborador, String> tableCollumnUserCol;
	@FXML
	private TableColumn<Colaborador, Colaborador> tableCollumnEDIT;//Alterar colaboradores
	@FXML
	private TableColumn<Colaborador, Colaborador> tableColumnREMOVE;//Deletar colaboradores


	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	@FXML
	public void onBtnCadastrar(ActionEvent event) {
		//Stage parentStage = Utils.currentStage(event);
		
		Colaborador colab = new Colaborador();
		Endereco end = new Endereco();
		
		createDialogForm(colab, end, "/gui/ColaboradorForm.fxml", Utils.currentStage(event));
	}
	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNome.getText());
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setColaboradorService(ColaboradorServices service) {
		this.service = service;
	}
	
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
		//PropertyValueFactory<>() -> Vincula os dados de um objeto a coluna da tabela
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("idColab"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableCollumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableCollumnCPF_CNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj_cpf"));
		tableCollumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableCollumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableCollumnUserCol.setCellValueFactory(new PropertyValueFactory<>("user_Col"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewColaborador.prefHeightProperty().bind(stage.heightProperty());

	}
	

	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service Colaborador was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ColaboradorServices
		List<Colaborador> list = service.findAll();

		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewColaborador.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();		
	}//End updateTableView
	
	public void updateTableViewConsult(String name) {
		if(service == null) {
			throw new IllegalStateException("Service Colaborador was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ColaboradorServices
		List<Colaborador> list = service.consultName(name);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewColaborador.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();	
	}//End updateTableViewConsult
	
	private void createDialogForm(Colaborador colab, Endereco end, String absolutePath,Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			//Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();
			
			ColaboradorFormController controller = loader.getController();//Pega o controlador da tela do formulario
			controller.setColaborador(colab);
			controller.setEndereco(end);
			controller.setColaboradorServices(new ColaboradorServices());
			controller.setEnderecoService(new EnderecoService());
			controller.subscribeDataChangeListener(this);//Incrissao para receber o evento do DataChangeListener
			controller.updateFormData();
			
			dialogStage.setTitle("Entre com os dados do colaborador: ");
			dialogStage.setScene(new Scene(pane));//Instanciar nova cena
			//Bloquear o redimensionamento da janela
			dialogStage.setResizable(false);
			//Definir o "Pai" da janela
			dialogStage.initOwner(parentStage);
			//Definir a janela como modal
			dialogStage.initModality(Modality.WINDOW_MODAL);
			//
			//Chamar a janela
			dialogStage.showAndWait();//Aguarda ser fechada pelo usuario
			
			
		}catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlerts("IOException", "Erro ao carragar a tela!", e.getMessage(), AlertType.ERROR);
		}
	}


	@Override
	public void onDataChanged() {
		updateTableView();
	}

	//Adiciota o botao de alteracao
	private void initEditButtons() {
		tableCollumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnEDIT.setCellFactory(param -> new TableCell<Colaborador, Colaborador>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Colaborador obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				EnderecoService service = new EnderecoService();
				Endereco end = service.findById(obj.getId_End());
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, end, "/gui/ColaboradorForm.fxml", Utils.currentStage(event)));
			}
		});
	}// End initEditButtons
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Colaborador, Colaborador>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Colaborador obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}//end initRemoveButtons


	private void removeEntity(Colaborador obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getName() + " ?");
		
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				EnderecoService serviceEnd = new EnderecoService();
				Endereco end = serviceEnd.findById(obj.getId_End());
				
				
				service.remove(obj);
				serviceEnd.remove(end);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null,e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	
}
