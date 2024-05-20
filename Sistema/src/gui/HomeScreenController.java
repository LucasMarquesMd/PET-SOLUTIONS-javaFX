package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomeScreenController implements Initializable{

	@FXML
	private ImageView imageView; 
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		//Carrega a imagem do recuso
		Image image = new Image(getClass().getResourceAsStream("/resource/Risadog.png"));
		//Define a imagem no imageView
		imageView.setImage(image);
		
	}

}
