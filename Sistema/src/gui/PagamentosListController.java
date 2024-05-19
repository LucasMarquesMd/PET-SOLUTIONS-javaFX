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
import gui.util.Constraints;
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
import model.entities.Pagamentos;
import model.entities.PedidoItems;
import model.entities.Pedidos;
import model.entities.Produto;
import model.entities.enums.PedidoStatus;
import model.entities.enums.TipoDePagamento;
import model.services.PagamentosServices;
import model.services.PedidoItemsServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;


/*
 * Ajustar a data
 */
public class PagamentosListController implements Initializable, DataChangeListener{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de Pagamentoses
	private PagamentosServices service;
	

	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<Pagamentos> obsList;

	
	
	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNumero;
	
	@FXML
	private TableView<Pagamentos> tableViewPagamentos = new TableView<>();
	@FXML
	private TableColumn<Pagamentos, Integer> tableCollumnId;
	@FXML
	private TableColumn<Pagamentos, Date> tableCollumnData;
	@FXML
	private TableColumn<Pagamentos, Double> tableCollumnPreco;
	@FXML
	private TableColumn<Pagamentos, TipoDePagamento> tableCollumnTipo;
	@FXML
	private TableColumn<Pagamentos, Integer> tableCollumnNroPed;
	@FXML
	private TableColumn<Pagamentos, Pagamentos> tableCollumnEDIT;//Alterar colaboradores
	@FXML
	private TableColumn<Pagamentos, Pagamentos> tableColumnREMOVE;//Deletar colaboradores


	
	
	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/

	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNumero.getText());
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setPagamentosService(PagamentosServices service) {
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
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Pag"));
		tableCollumnData.setCellValueFactory(new PropertyValueFactory<>("dt_Pag"));
		tableCollumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco_Pag"));
		tableCollumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_Pag"));
		tableCollumnNroPed.setCellValueFactory(new PropertyValueFactory<>("nro_Ped"));
		
		Constraints.setTextFieldInteger(txtNumero);
		
//		initializeTbcNroPagamentos();
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewPagamentos.prefHeightProperty().bind(stage.heightProperty());

	}
	
//	private void initializeTbcNroPagamentos() {
//		// Configurar a célula para exibir o nome do colaborador
//				tableCollumnNroPed.setCellFactory(column -> {
//				    return new TableCell<Pagamentos, Pedidos>() {
//				        @Override
//				        protected void updateItem(Pedidos pedidos, boolean empty) {
//				            super.updateItem(pedidos, empty);
//
//				            if (pedidos == null || empty) {
//				                setText(null);
//				            } else {
//				                setText(String.valueOf(pedidos.getId_Ped()));
//				            }
//				        }
//				    };
//				});
//	}
//	

	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service Pagamentos was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo PagamentosServices
		List<Pagamentos> list = service.findAll();

		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewPagamentos.setItems(obsList);
		
		//initEditButtons();
		initRemoveButtons();
		
	}//End updateTableView
	
	public void updateTableViewConsult(String numero) {
		if(service == null) {
			throw new IllegalStateException("Service Pagamentos was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo PagamentosServices
		List<Pagamentos> list = service.consultPed(numero);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewPagamentos.setItems(obsList);
	
	}//End updateTableViewConsult
	
	private void createDialogForm(Pagamentos pag, String absolutePath,Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			//Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();
			
			PagamentosFormController controller = loader.getController();//Pega o controlador da tela do formulario
			//entidades
			controller.setPedidos(new Pedidos());
			controller.setPagamentos(pag);
			

			controller.setPedidosServices(new PedidosServices());
			controller.setPagamentosServices(new PagamentosServices());
			
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
			Alerts.showAlerts("IOException", "Erro ao carragar a tela de pagamentos!", e.getMessage(), AlertType.ERROR);
		}
	}


	@Override
	public void onDataChanged() {
		updateTableView();
	}

//	//Adiciota o botao de alteracao
//	private void initEditButtons() {
//		tableCollumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//		tableCollumnEDIT.setCellFactory(param -> new TableCell<Pagamentos, Pagamentos>() {
//			private final Button button = new Button("edit");
//
//			@Override
//			protected void updateItem(Pagamentos obj, boolean empty) {
//				super.updateItem(obj, empty);
//
//				if (obj == null) {
//					setGraphic(null);
//					return;
//				}
//
////				PedidosServices service = new PedidosServices();
////				Pedidos ped = service.findByPag(obj.getId_Pag());
//				
//				
//				setGraphic(button);
//				button.setOnAction(
//						event -> createDialogForm( obj, "/gui/PagamentosForm.fxml", Utils.currentStage(event)));
//			}
//		});
//	}// End initEditButtons
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Pagamentos, Pagamentos>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Pagamentos obj, boolean empty) {
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


	private void removeEntity(Pagamentos obj) {
		//Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar Pedido ?");
		
		
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
