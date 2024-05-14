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
import model.entities.LocalDeEstoque;
import model.services.LocalDeEstoqueServices;

public class LocalListController implements Initializable, DataChangeListener{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de LocalDeEstoquees
	private LocalDeEstoqueServices service;
	

	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<LocalDeEstoque> obsList;

	
	
	@FXML
	private Button btnCadastrar;
	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNome;
	
	@FXML
	private TableView<LocalDeEstoque> tableViewLocalDeEstoque = new TableView<>();
	@FXML
	private TableColumn<LocalDeEstoque, Integer> tableCollumnId;
	@FXML
	private TableColumn<LocalDeEstoque, String> tableCollumnName;
	@FXML
	private TableColumn<LocalDeEstoque, String> tableCollumnDescricao;
	@FXML
	private TableColumn<LocalDeEstoque, String> tableCollumnStatus;
	@FXML
	private TableColumn<LocalDeEstoque, LocalDeEstoque> tableCollumnEDIT;//Alterar colaboradores
	@FXML
	private TableColumn<LocalDeEstoque, LocalDeEstoque> tableColumnREMOVE;//Deletar colaboradores


	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	@FXML
	public void onBtnCadastrar(ActionEvent event) {
		//Stage parentStage = Utils.currentStage(event);
		
		LocalDeEstoque prod = new LocalDeEstoque();
		
		createDialogForm(prod,"/gui/LocalForm.fxml", Utils.currentStage(event));
	}
	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNome.getText());
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setLocalDeEstoqueService(LocalDeEstoqueServices service) {
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
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Local"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("nome_Local"));
		tableCollumnDescricao.setCellValueFactory(new PropertyValueFactory<>("desc_Local"));
		tableCollumnStatus.setCellValueFactory(new PropertyValueFactory<>("sit_Local"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewLocalDeEstoque.prefHeightProperty().bind(stage.heightProperty());

	}
	

	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service LocalDeEstoque was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo LocalDeEstoqueServices
		List<LocalDeEstoque> list = service.findAll();

		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewLocalDeEstoque.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();		
	}//End updateTableView
	
	public void updateTableViewConsult(String name) {
		if(service == null) {
			throw new IllegalStateException("Service LocalDeEstoque was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo LocalDeEstoqueServices
		List<LocalDeEstoque> list = service.consultName(name);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewLocalDeEstoque.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();	
	}//End updateTableViewConsult
	
	private void createDialogForm(LocalDeEstoque prod, String absolutePath,Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			//Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();
			
			LocalDeEstoqueFormController controller = loader.getController();//Pega o controlador da tela do formulario
			controller.setLocalDeEstoque(prod);
			controller.setLocalDeEstoqueServices(new LocalDeEstoqueServices());
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
		tableCollumnEDIT.setCellFactory(param -> new TableCell<LocalDeEstoque, LocalDeEstoque>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(LocalDeEstoque obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}


				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/LocalForm.fxml", Utils.currentStage(event)));
			}
		});
	}// End initEditButtons
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<LocalDeEstoque, LocalDeEstoque>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(LocalDeEstoque obj, boolean empty) {
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


	private void removeEntity(LocalDeEstoque obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar " + obj.getNome_Local() + " ?");
		
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				
				service.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null,e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	
}
