package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Pedidos;
import model.entities.enums.PedidoStatus;
import model.services.ClienteServices;
import model.services.ColaboradorServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;

public class PedidosListController implements Initializable, DataChangeListener{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de Pedidoses
	private PedidosServices service;
	

	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<Pedidos> obsList;

	
	
	@FXML
	private Button btnNovo;
	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNumero;
	
	@FXML
	private TableView<Pedidos> tableViewPedidos = new TableView<>();
	@FXML
	private TableColumn<Pedidos, Integer> tableCollumnId;
	@FXML
	private TableColumn<Pedidos, Date> tableCollumnData;
	@FXML
	private TableColumn<Pedidos, Double> tableCollumnPreco;
	@FXML
	private TableColumn<Pedidos, PedidoStatus> tableCollumnStatus;
	@FXML
	private TableColumn<Pedidos, Colaborador> tableCollumnResponsavel;
	@FXML
	private TableColumn<Pedidos, Pedidos> tableCollumnEDIT;//Alterar colaboradores
	@FXML
	private TableColumn<Pedidos, Pedidos> tableColumnREMOVE;//Deletar colaboradores


	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	@FXML
	public void onBtnNovo(ActionEvent event) {
		//Stage parentStage = Utils.currentStage(event);
		
		Pedidos pedido = new Pedidos();
		Colaborador colab = new Colaborador(1, "a", "a", "a", 12, 12, "a", "a", 1, 1);
		
		createDialogForm(pedido, colab, "/gui/PedidosForm.fxml", Utils.currentStage(event));
	}
	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(Utils.tryParseToInt(txtNumero.getText()));
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setPedidosService(PedidosServices service) {
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
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Ped"));
		tableCollumnData.setCellValueFactory(new PropertyValueFactory<>("dt_Ped"));
		tableCollumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco_Ped"));
		tableCollumnStatus.setCellValueFactory(new PropertyValueFactory<>("status_Ped"));
		tableCollumnResponsavel.setCellValueFactory(new PropertyValueFactory<>("colaborador"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewPedidos.prefHeightProperty().bind(stage.heightProperty());

	}
	

	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service Pedidos was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo PedidosServices
		List<Pedidos> list = service.findAll();

		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewPedidos.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();		
	}//End updateTableView
	
	public void updateTableViewConsult(Integer numero) {
		if(service == null) {
			throw new IllegalStateException("Service Pedidos was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo PedidosServices
		List<Pedidos> list = service.consultPed(numero);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewPedidos.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();	
	}//End updateTableViewConsult
	
	private void createDialogForm(Pedidos ped, Colaborador col, String absolutePath,Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			//Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();
			
			PedidosFormController controller = loader.getController();//Pega o controlador da tela do formulario
			controller.setPedidos(ped);
			controller.setColaborador(col);
			controller.setColaboradorServices(new ColaboradorServices());
			controller.setServicesCli(new ClienteServices());
			controller.setServicesProd(new ProdutoServices());
			controller.setPedidosServices(new PedidosServices());
			
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);//Incrissao para receber o evento do DataChangeListener
			controller.updateFormData();
			
			dialogStage.setTitle("Entre com os dados do pedido: ");
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
		tableCollumnEDIT.setCellFactory(param -> new TableCell<Pedidos, Pedidos>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Pedidos obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				ColaboradorServices service = new ColaboradorServices();
				Colaborador col = service.findById(obj.getId_Col());
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, MainViewController.colaborador, "/gui/PedidosForm.fxml", Utils.currentStage(event)));
			}
		});
	}// End initEditButtons
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Pedidos, Pedidos>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Pedidos obj, boolean empty) {
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


	private void removeEntity(Pedidos obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar Pedido ?");
		
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				ColaboradorServices serviceEnd = new ColaboradorServices();
				Colaborador end = serviceEnd.findById(obj.getId_Col());
				
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
