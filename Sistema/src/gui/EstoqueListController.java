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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Produto;
import model.services.ProdutoServices;

public class EstoqueListController implements Initializable{

/* ========================================================================
 * 			Declaracao das variaveis
 * ========================================================================
 */
	//Sera utilizado para auxiliar a manipulacao da classe de Produtoes
	private ProdutoServices service;
	

	
	//Observa a lista instanciada -> usada para atualizar a UI automaticamente de acordo com a mudanca dos dados na lista
	private ObservableList<Produto> obsList;

	

	@FXML
	private Button btnConsultar;
	@FXML
	private TextField txtNome;
	
	@FXML
	private TableView<Produto> tableViewProduto = new TableView<>();
	@FXML
	private TableColumn<Produto, Integer> tableCollumnId;
	@FXML
	private TableColumn<Produto, String> tableCollumnName;
	@FXML
	private TableColumn<Produto, String> tableCollumnDescricao;
	@FXML
	private TableColumn<Produto, Integer> tableCollumnQuantidade;

	
/* ========================================================================
 * 			Acoes dos controles
 * ========================================================================
*/
	
	@FXML
	public void onBtnConsultar() {
		updateTableViewConsult(txtNome.getText());
	}
	
	
// =================================================================================
//				Funcoes para injecao de dependencia	
// =================================================================================	
	
	//Inversao de controle - facilita a manutencao do codigo
	public void setProdutoService(ProdutoServices service) {
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
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id_Prod"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("nome_Prod"));
		tableCollumnDescricao.setCellValueFactory(new PropertyValueFactory<>("desc_Prod"));
		tableCollumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("qtd_Estocado"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para o priaryStage
		
		//O table view acompanha a janela
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());

	}
	

	
	//Metodo responsavel por acessar o servico -> carrgar os colaboradores e atualiza-los no ObservableList<>
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service Produto was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ProdutoServices
		List<Produto> list = service.findAll();

		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewProduto.setItems(obsList);		
	}//End updateTableView
	
	public void updateTableViewConsult(String name) {
		if(service == null) {
			throw new IllegalStateException("Service Produto was null!");
		}
		
		//Recebe a lista de colaboradores gerada pelo ProdutoServices
		List<Produto> list = service.consultName(name);
		
		//Associar a list ao ObservableList para verificar atualizacoes de dados.
		obsList = FXCollections.observableArrayList(list);//A classe e oriunda da javaFX
		
		//Carregar os dados no TableView
		tableViewProduto.setItems(obsList);
	}//End updateTableViewConsult
	


//	@Override
//	public void onDataChanged() {
//		updateTableView();
//	}


}
