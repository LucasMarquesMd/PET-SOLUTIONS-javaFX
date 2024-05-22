package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import model.entities.Estoque;
import model.entities.Fornecedor;
import model.entities.Fornecimento;
import model.entities.LocalDeEstoque;
import model.entities.NotaEstoque;
import model.entities.Produto;
import model.services.EstoqueServices;
import model.services.FornecedorServices;
import model.services.FornecimentoServices;
import model.services.LocalDeEstoqueServices;
import model.services.NotaEstoqueServices;
import model.services.ProdutoServices;

public class NotasListController implements Initializable, DataChangeListener {

	/*
	 * ========================================================================
	 * Declaracao das variaveis
	 * ========================================================================
	 */
	// Sera utilizado para auxiliar a manipulacao da classe de NotaEstoquees
	private NotaEstoqueServices service;

	// Observa a lista instanciada -> usada para atualizar a UI automaticamente de
	// acordo com a mudanca dos dados na lista
	private ObservableList<NotaEstoque> obsListNota;

	@FXML
	private Button btnNovo;
	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNumero;

	@FXML
	private TableView<NotaEstoque> tableViewNotaEstoque = new TableView<>();
	@FXML
	private TableColumn<NotaEstoque, Integer> tableCollumnId;
	@FXML
	private TableColumn<NotaEstoque, Fornecimento> tableCollumnData;
	@FXML
	private TableColumn<NotaEstoque, Double> tableCollumnPreco;
	@FXML
	private TableColumn<NotaEstoque, Integer> tableCollumnNumero;
	@FXML
	private TableColumn<NotaEstoque, NotaEstoque> tableCollumnEDIT;
	@FXML
	private TableColumn<NotaEstoque, NotaEstoque> tableColumnREMOVE;

	/*
	 * ========================================================================
	 * Acoes dos controles
	 * ========================================================================
	 */

	@FXML
	public void onBtnNovo(ActionEvent event) {
		NotaEstoque est = new NotaEstoque();
		Fornecimento forne = new Fornecimento();
		Fornecedor forn = new Fornecedor();
		LocalDeEstoque local = new LocalDeEstoque();
		Produto prod = new Produto();

		createDialogForm(est, forne, forn, local, prod, "/gui/NotasFiscaisForm.fxml", Utils.currentStage(event));
	}

	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNumero.getText());
	}

// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	

	// Inversao de controle - facilita a manutencao do codigo
	public void setNotaEstoqueService(NotaEstoqueServices service) {
		this.service = service;
	}

	/*
	 * ========================================================================
	 * Metodos da classe
	 * ========================================================================
	 */

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();

	}

	private void initializeNode() {
		// setCellValueFactory() -> Define como os valores dacoluna sao obtidos dos
		// objetos associados da tabela
		// PropertyValueFactory<>() -> Vincula os dados de um objeto a coluna da tabela
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Nota"));
		tableCollumnData.setCellValueFactory(new PropertyValueFactory<>("fornecimento"));
		tableCollumnPreco.setCellValueFactory(new PropertyValueFactory<>("valor"));
		tableCollumnNumero.setCellValueFactory(new PropertyValueFactory<>("nro_Nota"));

		Constraints.setTextFieldInteger(txtNumero);

		initializeTbcData();

		Stage stage = (Stage) Main.getMainScene().getWindow();// Referencia para o priaryStage

		// O table view acompanha a janela
		tableViewNotaEstoque.prefHeightProperty().bind(stage.heightProperty());

	}

	private void initializeTbcData() {
	    // Configurar a célula para exibir o nome do colaborador
	    tableCollumnData.setCellFactory(column -> {
	        return new TableCell<NotaEstoque, Fornecimento>() {
	            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	            @Override
	            protected void updateItem(Fornecimento forne, boolean empty) {
	                super.updateItem(forne, empty);

	                if (forne == null || empty) {
	                    setText(null);
	                } else {
	                    // Formatar a data em uma string legível
	                    String formattedDate = dateFormat.format(forne.getDt_Forne());
	                    setText(formattedDate);
	                }
	            }
	        };
	    });
	}

	// Metodo responsavel por acessar o servico -> carrgar as notas e
	// atualiza-los no ObservableList<>
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service NotaEstoque was null!");
		}
		
		List<NotaEstoque> list = service.findAll();
		
		obsListNota = FXCollections.observableArrayList(list);

		tableViewNotaEstoque.setItems(obsListNota);

		initEditButtons();
		initRemoveButtons();
	}// End updateTableView

	public void updateTableViewConsult(String numero) {
		if (service == null) {
			throw new IllegalStateException("Service NotaEstoque was null!");
		}

		// Recebe a lista de colaboradores gerada pelo NotaEstoqueServices
		List<NotaEstoque> list = service.findByNum(numero);

		// Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsListNota = FXCollections.observableArrayList(list);// A classe e oriunda da javaFX

		// Carregar os dados no TableView
		tableViewNotaEstoque.setItems(obsListNota);

		initEditButtons();
		initRemoveButtons();
	}// End updateTableViewConsult

	private void createDialogForm(NotaEstoque nota, Fornecimento forne, Fornecedor forn, LocalDeEstoque local, Produto prod, 
			String absolutePath, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
			Pane pane = loader.load();
			// Instanciar um novo Stage (Palco)
			Stage dialogStage = new Stage();

			NotasFiscaisFormController controller = loader.getController();// Pega o controlador da tela do formulario

			//Entidades
			controller.setEntityForne(forne);
			controller.setEntityNota(nota);
			controller.setEntityForn(forn);
			controller.setEntityLocal(local);
			controller.setEntityProd(prod);
			
			//Servicos
			controller.setServiceEst(new EstoqueServices());
			controller.setServiceForn(new FornecedorServices());
			controller.setServiceForne(new FornecimentoServices());
			controller.setServiceLocal(new LocalDeEstoqueServices());
			controller.setServiceNota(new NotaEstoqueServices());
			controller.setServiceProd(new ProdutoServices());

			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);// Inscricao para receber o evento do DataChangeListener
			controller.updateFormData();

			dialogStage.setTitle("Entre com os dados da nota fiscal: ");
			dialogStage.setScene(new Scene(pane));// Instanciar nova cena
			// Bloquear o redimensionamento da janela
			dialogStage.setResizable(false);
			// Definir o "Pai" da janela
			dialogStage.initOwner(parentStage);
			// Definir a janela como modal
			dialogStage.initModality(Modality.WINDOW_MODAL);
			//
			// Chamar a janela
			dialogStage.showAndWait();// Aguarda ser fechada pelo usuario

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlerts("IOException", "Erro ao carragar a tela!", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	// Adiciota o botao de alteracao
	private void initEditButtons() {
		tableCollumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnEDIT.setCellFactory(param -> new TableCell<NotaEstoque, NotaEstoque>() {
			private final Button button = new Button("Visualizar");

			@Override
			protected void updateItem(NotaEstoque obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				FornecimentoServices service = new FornecimentoServices();
				Fornecimento forne = service.findById(obj.getId_Forne());
				
				LocalDeEstoqueServices serviceLocal = new LocalDeEstoqueServices();
				LocalDeEstoque local;
				if(forne != null) {
					local = serviceLocal.findById(forne.getEstoque().getLocalDeEstoque().getId_Local());//Pega o id do local de estoque

				}else {
					local = new LocalDeEstoque();
				}
				
				FornecedorServices serviceForn = new FornecedorServices();
				Fornecedor forn;
				if(forne != null) {
					forn = serviceForn.findById(forne.getId_Forn());//Pega o id do local de estoque

				}else {
					forn = new Fornecedor();
				}
				
				ProdutoServices serviceProd = new ProdutoServices();
				Produto prod = serviceProd.findById(forne.getId_Prod());
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, forne, forn, local, prod, "/gui/NotasFiscaisForm.fxml", Utils.currentStage(event)));
			}
		});
	}// End initEditButtons

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<NotaEstoque, NotaEstoque>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(NotaEstoque obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}// end initRemoveButtons
	
	/*
	 * Descontar o produto apos a remocao
	 */

	private void removeEntity(NotaEstoque obj) {
		// Optional<> -> objeto que carraga outro objeto dentro dele
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deletar Pedido ?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				
				FornecimentoServices serviceForne = new FornecimentoServices();
				EstoqueServices serviceEst = new EstoqueServices();
				ProdutoServices serviceProd = new ProdutoServices();
				Fornecimento forne = serviceForne.findById(obj.getId_Forne());
				Estoque est = serviceEst.findById(forne.getId_Est());
				Produto prod = serviceProd.findById(est.getId_Prod());
				
				prod.subtractProduct(est.getQt_Prod_Est());
				
				service.remove(obj);
				serviceForne.remove(forne);
				serviceEst.remove(est);

				
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
