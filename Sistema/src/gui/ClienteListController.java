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
import model.entities.Cliente;
import model.entities.Endereco;
import model.services.ClienteServices;
import model.services.EnderecoService;

public class ClienteListController implements Initializable, DataChangeListener{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de Clientees
	private ClienteServices service;
	

	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<Cliente> obsList;

	
	
	@FXML
	private Button btnCadastrar;
	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNome;
	
	@FXML
	private TableView<Cliente> tableViewCliente = new TableView<>();
	@FXML
	private TableColumn<Cliente, Integer> tableCollumnId;
	@FXML
	private TableColumn<Cliente, String> tableCollumnName;
	@FXML
	private TableColumn<Cliente, String> tableCollumnEmail;
	@FXML
	private TableColumn<Cliente, Integer> tableCollumnCPF;
	@FXML
	private TableColumn<Cliente, String> tableCollumnTelefone;
	@FXML
	private TableColumn<Cliente, String> tableCollumnCelular;
	@FXML
	private TableColumn<Cliente, Cliente> tableCollumnEDIT;//Alterar colaboradores
	@FXML
	private TableColumn<Cliente, Cliente> tableColumnREMOVE;//Deletar colaboradores


	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	@FXML
	public void onBtnCadastrar(ActionEvent event) {
		//Stage parentStage = Utils.currentStage(event);
		
		Cliente colab = new Cliente();
		Endereco end = new Endereco();
		
		createDialogForm(colab, end, "/gui/ClienteForm.fxml", Utils.currentStage(event));
	}
	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNome.getText());
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setClienteService(ClienteServices service) {
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
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Cli"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("nome_Cli"));
		tableCollumnEmail.setCellValueFactory(new PropertyValueFactory<>("email_Cli"));
		tableCollumnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf_Cli"));
		tableCollumnTelefone.setCellValueFactory(new PropertyValueFactory<>("tel_Cli"));
		tableCollumnCelular.setCellValueFactory(new PropertyValueFactory<>("cel_Cli"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());

	}

	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service Cliente was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ClienteServices
		List<Cliente> list = service.findAll();

		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewCliente.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();		
	}//End updateTableView
	
	public void updateTableViewConsult(String name) {
		if(service == null) {
			throw new IllegalStateException("Service Cliente was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ClienteServices
		List<Cliente> list = service.consultName(name);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewCliente.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();	
	}//End updateTableViewConsult
	
	private void createDialogForm(Cliente colab, Endereco end, String absolutePath,Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			//Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();
			
			ClienteFormController controller = loader.getController();//Pega o controlador da tela do formulario
			controller.setCliente(colab);
			controller.setEndereco(end);
			controller.setClienteServices(new ClienteServices());
			controller.setEnderecoService(new EnderecoService());
			controller.subscribeDataChangeListener(this);//Incrissao para receber o evento do DataChangeListener
			controller.updateFormData();
			
			dialogStage.setTitle("Entre com os dados do cliente: ");
			dialogStage.setScene(new Scene(pane));//Instanciar nova cena
			//Bloquear o redimensionamento da janela
			dialogStage.setResizable(false);
			//Definir o "Pai" da janela
			dialogStage.initOwner(parentStage);
			//Definir a janela como modal
			dialogStage.initModality(Modality.WINDOW_MODAL);
			
			//Chamar a janela
			dialogStage.showAndWait();//Aguarda ser fechada pelo usuario
			
			
		}catch (IOException e) {
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
		tableCollumnEDIT.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				EnderecoService service = new EnderecoService();
				Endereco end = service.findById(obj.getId_End());
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, end, "/gui/ClienteForm.fxml", Utils.currentStage(event)));
			}
		});
	}// End initEditButtons
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
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


	private void removeEntity(Cliente obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getNome_Cli() + " ?");
		
		
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
