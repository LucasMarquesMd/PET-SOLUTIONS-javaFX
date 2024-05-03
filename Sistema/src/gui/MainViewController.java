package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


/*
 * Initializable - Permite a instanciacao do controlador no MainView.FXML
 * 
 * Permite o controle da inicializacao da cana antes que a mesma seja carragada na tela
 */



public class MainViewController implements Initializable{

	@FXML
	private MenuItem file;
	
	
	
	public void onFileAction() {
		loadView("/gui/Login.fxml");
	}
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	
	private synchronized void loadView(String absolutPath) {

		try {
			// Carrega o novo arquivo FXML que define a interface do usuário
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutPath));
			// Carrega e instancia o novo layout principal da cena a partir do arquivo FXML
			VBox newVBox = loader.load();
			
			// Obtém a cena principal da aplicação
			Scene mainScene = Main.getMainScene();
			
			// Obtém o VBox que contém o conteúdo da cena principal
			/*
			 * getRoot() - pega o primeiro elemento da cena
			 * O casting e necessario pois o compilador nao sabe qual o elemento (no caso ScrollPane)
			 * getContent() - pega o conteudo dentro do ScrollPane, ou seja o VBox (e tudo que esta dentro dele)
			 * O casting e necessario pois o compilador nao sabe qual o conteudo dentro do ScrollPane (no caso o VBox)
			 * Por fim, o VBox da cena do meinView fica armazenado dentro do mainVBox (e todo o que esta dentro dele)
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			// Obtém o nó do menu principal da cena principal
			Node mainMenu = mainVBox.getChildren().get(0);//Preservamos apenas o menubar para reaplica-la
			//Limpar oconteudo inteiro do VBox da tela principal
			mainVBox.getChildren().clear();
			//Adicionar o menuBar novamente na tela
			mainVBox.getChildren().add(mainMenu);
			//Adicionar os conetudos da nova tela no VBox principal
			mainVBox.getChildren().addAll(newVBox.getChildren());//Adiciona os filhos no newVBox
			
			
			
			
			
			
			
		}
		catch(IOException e) {
			e.printStackTrace();//Adicionar a classe Alerts
		}
		
		
	}//end loadView

	
}
