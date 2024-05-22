package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entities.Colaborador;
import model.services.ClienteServices;
import model.services.ColaboradorServices;
import model.services.FornecedorServices;
import model.services.LocalDeEstoqueServices;
import model.services.NotaEstoqueServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;

/*
 * Initializable - Permite a instanciacao do controlador no MainView.FXML
 * 
 * Permite o controle da inicializacao da cana antes que a mesma seja carragada na tela
 */

public class MainViewController implements Initializable {

// ========================================================================
//							Dependencias
// ========================================================================

	public ColaboradorServices service;

	public static Colaborador colaborador;

	/*
	 * ========================================================================
	 * Declaracao das variaveis
	 * ========================================================================
	 */

	@FXML
	private MenuBar mainMenuBar;

	

// ========================================================================
//						Menu Items
//========================================================================

	@FXML
	private MenuItem menuItemHome;
	@FXML
	private MenuItem menuItemColabList;
	@FXML
	private MenuItem menuItemProduto;
	@FXML
	private MenuItem menuItemFornecedor;
	@FXML
	private MenuItem menuItemCliente;
	@FXML
	private MenuItem menuItemLocal;
	@FXML
	private MenuItem menuItemPedidos;
	@FXML
	private MenuItem menuItemNotas;
	@FXML
	private MenuItem menuItemEstoque;
	@FXML
	private MenuItem menuItemSair;
	@FXML
	private MenuItem menuItemAjuda;

	/*
	 * ========================================================================
	 * Declaracao das variaveis de login
	 * ========================================================================
	 */

	@FXML
	private Button btnEntrar;

	@FXML
	private Button btnSair;


	/*
	 * ========================================================================
	 * Acoes dos controles
	 * ========================================================================
	 */

	public void onBtnEntrarAction() {	
		loadView("/gui/Login.fxml", (LoginController controller) -> {
			controller.setColaboradorServices(new ColaboradorServices());
		});

	}

	public void onBtnSairAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void onMenuItemColaboradoresAction() {
		loadView("/gui/ColaboradorList.fxml", (ColaboradorListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setColaboradorService(new ColaboradorServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}// end

	public void onMenuItemHomeAction() {
		loadView("/gui/HomeScreen.fxml", x -> {
		});// Lambda vazia
	}

	public void onMenuItemProdutoAction() {

		loadView("/gui/ProdutoList.fxml", (ProdutoListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setProdutoService(new ProdutoServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}

	public void onMenuItemFornecedorAction() {

		loadView("/gui/FornecedorList.fxml", (FornecedorListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setFornecedorService(new FornecedorServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}
	
	public void onMenuItemClienteAction() {

		loadView("/gui/ClienteList.fxml", (ClienteListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setClienteService(new ClienteServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}
	
	public void onMenuItemLocalAction() {

		loadView("/gui/LocalList.fxml", (LocalListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setLocalDeEstoqueService(new LocalDeEstoqueServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}
	
	public void onMenuItemPedidosAction() {

		loadView("/gui/PedidosList.fxml", (PedidosListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setPedidosService(new PedidosServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}
	
	public void onMenuItemNotasAction() {

		loadView("/gui/NotasList.fxml", (NotasListController controller) -> {
			// Injetar a dependencia da classe de servico
			controller.setNotaEstoqueService(new NotaEstoqueServices());// Instancia a classe de servico
			// Atualizar a tabela
			controller.updateTableView();
		});// Acao de inicializaco do controller
	}
	
	public void onMenuItemEstoqueAction() {

		loadView("/gui/EstoqueList.fxml", (EstoqueListController controller) -> {
			controller.setProdutoService(new ProdutoServices());
			controller.updateTableView();
		});
	}
	
	public void onMenuItemSairAction() {
		colaborador = null;
		mainMenuBar.setVisible(false);
		loadView("/gui/Login.fxml", (LoginController controller) -> {
			controller.setColaboradorServices(new ColaboradorServices());
		});
 
	}
	
	public void onMenuItemAjudaAction() {
		loadView("/gui/About.fxml", x -> {});
 
	}

	/*
	 * ========================================================================
	 * Metodos da classe
	 * ========================================================================
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainMenuBar.setVisible(false);

	}
	
	

	private synchronized <T> void loadView(String absolutPath, Consumer<T> initializingAction) {

		try {
			// Carrega o novo arquivo FXML que define a interface do usuário
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutPath));
			// Carrega e instancia o novo layout principal da cena a partir do arquivo FXML
			VBox newVBox = loader.load();

			// Obtém a cena principal da aplicação
			Scene mainScene = Main.getMainScene();

			// Obtém o VBox que contém o conteúdo da cena principal
			/*
			 * getRoot() - pega o primeiro elemento da cena O casting e necessario pois o
			 * compilador nao sabe qual o elemento (no caso ScrollPane) getContent() - pega
			 * o conteudo dentro do ScrollPane, ou seja o VBox (e tudo que esta dentro dele)
			 * O casting e necessario pois o compilador nao sabe qual o conteudo dentro do
			 * ScrollPane (no caso o VBox) Por fim, o VBox da cena do meinView fica
			 * armazenado dentro do mainVBox (e todo o que esta dentro dele)
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();// Ponteiro para o VBox dentrodo
																					// scrollPane

			// Obtém o nó do menu principal da cena principal
			Node mainMenu = mainVBox.getChildren().get(0);// Preservamos apenas o menubar para reaplica-la
			// Limpar oconteudo inteiro do VBox da tela principal
			mainVBox.getChildren().clear();
			// Adicionar o menuBar novamente na tela
			mainVBox.getChildren().add(mainMenu);
			// Adicionar os conetudos da nova tela no VBox principal
			mainVBox.getChildren().addAll(newVBox.getChildren());// Adiciona os filhos no newVBox

			// Acao de inicializacao
			T controller = loader.getController();// Retorna o controlador do tipo T
			initializingAction.accept(controller);// Executa a funcao passada como argumento

		} catch (IOException e) {
			e.printStackTrace();// Adicionar a classe Alerts
		}

	}// end loadView

	
}
