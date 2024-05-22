package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entities.Colaborador;
import model.entities.enums.NivelDeAcesso;
import model.services.ColaboradorServices;
import model.services.FornecedorServices;
import model.services.LocalDeEstoqueServices;
import model.services.NotaEstoqueServices;
import model.services.PedidosServices;
import model.services.ProdutoServices;

public class HomeScreenController implements Initializable {

//====================================================================
//						Variaveis de controles
//====================================================================	

	private boolean sair = true;
	
	@FXML
	private Button btnPedidos;
	@FXML
	private Button btnEstoque;
	@FXML
	private Button btnAjuda;
	@FXML
	private Button btnSair;
	@FXML
	private Button btnProdutos;
	@FXML
	private Button btnNotas;
	@FXML
	private Button btnFornecedores;
	@FXML
	private Button btnLocais;

//====================================================================
//							Inicializacao
//====================================================================

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		validarUsuario(MainViewController.colaborador);

	}

//====================================================================
//					    Funcoes dos controles
//====================================================================

	@FXML
	public void onBtnSairAction(ActionEvent event) {
		MainViewController.colaborador = null;
		sair = false;
		loadView("/gui/Login.fxml", (LoginController controller) -> {
			controller.setColaboradorServices(new ColaboradorServices());
		});
	}

	@FXML
	public void onBtnPedidosAction() {
		loadView("/gui/PedidosList.fxml", (PedidosListController controller) -> {
			controller.setPedidosService(new PedidosServices());
			controller.updateTableView();
		});
	}

	@FXML
	public void onBtnEstoqueAction() {
		loadView("/gui/EstoqueList.fxml", (EstoqueListController controller) -> {
			controller.setProdutoService(new ProdutoServices());
			controller.updateTableView();
		});
	}

	@FXML
	public void onBtnAjudaAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@FXML
	public void onBtnProdutosAction() {
		loadView("/gui/ProdutoList.fxml", (ProdutoListController controller) -> {
			controller.setProdutoService(new ProdutoServices());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onBtnNotasAction() {
		loadView("/gui/NotasList.fxml", (NotasListController controller) -> {
			controller.setNotaEstoqueService(new NotaEstoqueServices());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onBtnFornecedoresAction() {
		loadView("/gui/FornecedorList.fxml", (FornecedorListController controller) -> {
			controller.setFornecedorService(new FornecedorServices());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onBtnLocaisAction() {
		loadView("/gui/LocalList.fxml", (LocalListController controller) -> {
			controller.setLocalDeEstoqueService(new LocalDeEstoqueServices());
			controller.updateTableView();
		});
	}
	
	

//====================================================================
//    					LoadView
//====================================================================

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
			MenuBar menuBar = (MenuBar) mainMenu;
			menuBar.setVisible(sair);
			
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
	
//====================================================================
//							Validacoes
//====================================================================
	
	public void validarUsuario(Colaborador colaborador) {
		if(colaborador.getLevel_Access() == NivelDeAcesso.COLABORADOR) {
			btnProdutos.setVisible(false);
			btnNotas.setVisible(false);
			btnFornecedores.setVisible(false);
			btnLocais.setVisible(false);
		}
	}

}
