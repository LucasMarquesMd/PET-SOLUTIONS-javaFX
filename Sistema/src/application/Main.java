package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/*
 * primaryStage - Palco onde sera implantada a cena
 * Parent - Super classe de layouts (Scroolpane, Boderpane...)
 * Scene - Cena onde ocorre a interacao com o usuario 
 * O metodo start e um metodo abstrato da classe Application.
 */


//A classe principal (main) que herda a classe aplication e inicializa a aplicacao javaFX
public class Main extends Application {

	private static Scene mainScene;//Sera utilizada no carregamento de outras telas


	// Metodo abstrato que precisa ser implementado para inicializar a aplicação javaFX
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// Carrega o arquivo FXML que define a interface do usuário
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));//Permite a manipulacao da tela antes de carrega-la
			// Carrega e instancia o layout principal da cena a partir do arquivo FXML
			ScrollPane scrollPane = loader.load();//Instancia o layout do palco
			//Cria uma cena com o layout principal definido no ScrollPane
			mainScene = new Scene(scrollPane);//Cria uma cena dentro do layout definido no ScrollPane (parent)
			
			//Ajusta o ScrollPane para se ajustar à largura e à altura da cena
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			// Define a cena no palco principal (Stage)
			primaryStage.setScene(mainScene);//Define a cena dentro do palco (A cena e do tipo ScrollPane)
			// Define o título da janela
			primaryStage.setTitle("PET SOLUTIONS");//Adiciona um titulo
			 // Exibe a interface gráfica
			primaryStage.show();
		} catch (IOException e) {
			
			//Adicionar alerta na tela.
			e.printStackTrace();
		}
		
	}//end start
	
	public static void main(String[] args) {
		launch(args);// Metodo estatico que inicai a aplicacao do javaFX
	}//end main
	
	public static Scene getMainScene(){
		return mainScene;
	}
}
