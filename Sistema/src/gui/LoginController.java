package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.entities.enums.NivelDeAcesso;
import model.services.ColaboradorServices;

public class LoginController implements Initializable{

	
//======================================================================
//							Dependencias
//======================================================================
	
	private ColaboradorServices serviceCol;
	
	
	
//======================================================================
//						Entidades
//======================================================================
	
	//Botoes
	@FXML
	private Button btnEntrar;
	@FXML
	private Button btnSair;

	//TextFields
	@FXML
	private TextField txtUsuario;
	@FXML
	private TextField txtSenha;
	
	
//======================================================================
//							Inicializacoes
//======================================================================	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
//======================================================================
//					Inicializadores de dependencias
//======================================================================	
	
	public void setColaboradorServices(ColaboradorServices serviceCol) {
		this.serviceCol = serviceCol;
	}
	
	
//======================================================================
//						Funcoes dos controlles
//======================================================================	
	
	@FXML
	public void onBtnEntrarAction() {
		if(validarUsuario(txtUsuario.getText(), txtSenha.getText())) {
			loadView("/gui/HomeScreen.fxml", x -> {});//Lambda vazia 
			
	
		}else {
			Alerts.showAlerts("Erro", "Usuario e ou senha invalidos!", null, AlertType.ERROR);
		}
		
	}
	
	
//======================================================================
//					loadView
//======================================================================
	
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
			mainMenu.setVisible(true);
			
			MenuBar menuBar = (MenuBar) mainMenu; // Converte o Node em MenuBar

			validarColaborador(menuBar);
			// Acessa os menus dentro do MenuBar (supondo que menuItemColab esteja em algum menu)
			
	
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
	
	
//======================================================================
//						Validar
//======================================================================
	
	private boolean validarUsuario(String userName, String UserSenha) {
		serviceCol = new ColaboradorServices();
		if (serviceCol == null) {
			throw new IllegalStateException("Service (colaborador) was null");
		}
		MainViewController.colaborador = serviceCol.validatingUser(userName, UserSenha);

		if (MainViewController.colaborador == null) {
			return false;
		}

		return true;
	}
	
	
	private void validarColaborador(MenuBar menuBar) {
		if(MainViewController.colaborador.getLevel_Access() == NivelDeAcesso.COLABORADOR) {
			esconderCampos(menuBar);
		}
	}
	
	private void esconderCampos(MenuBar menuBar) {
		for (Menu menu : menuBar.getMenus()) {
		    // Procura pelo item de menu menuItemColab dentro de cada menu
		    for (MenuItem menuItem : menu.getItems()) {
		        if (menuItem.getText().equals("Colaborador")) {
		            menuItem.setVisible(false); // Torna o item de menu 'menuItemColab' invisível
		      
		        }
		        else if (menuItem.getText().equals("Produto")) {
		            menuItem.setVisible(false); // Torna o item de menu 'menuItemColab' invisível
		      
		        }
		        else if (menuItem.getText().equals("Fornecedor")) {
		            menuItem.setVisible(false); // Torna o item de menu 'menuItemColab' invisível
		      
		        }
		        else if (menuItem.getText().equals("Notas")) {
		            menuItem.setVisible(false); // Torna o item de menu 'menuItemColab' invisível
		      
		        }
		        else if (menuItem.getText().equals("Local")) {
		            menuItem.setVisible(false); // Torna o item de menu 'menuItemColab' invisível
		      
		        }
		        
		    }
		}
	}
	

}
