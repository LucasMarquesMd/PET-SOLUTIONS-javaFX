package gui.util;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*
 * Classe utilitaria
 * Controle de validacoes para textfilds
 */

public class Constraints {
	
	//Otimizar...

	//textProperty() -> representa o conteudo textual do campo de texto de um TextFild
	//addListener() -> Tipo Obsevable,reage a alteracoes automaticamente
	
	
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && !newValue.matches("\\d*")) //Se o valor nao for nulo e nao for inteiro
	        {
	        	txt.setText(oldValue);//volte para o valor antigo
	        }
	    });
	}

	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && newValue.length() > max) //Se o valor exceder o numero maximo de carteres
	        {
	        	txt.setText(oldValue);//Volte ao valor original
	        }
	    });
	}
	
	public static void setTextAreaMaxLength(TextArea txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && newValue.length() > max) //Se o valor exceder o numero maximo de carteres
	        {
	        	txt.setText(oldValue);//Volte ao valor original
	        }
	    });
	}

	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
		    	if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?"))//Se o novo valor nao for um tipo Double
		    	{
                    txt.setText(oldValue);//Volte ao valor original
                }
		    });
	}
	
	
	/*
	 private static void addTextListener(TextField txt, int maxLength, String regex) {
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && (maxLength == -1 || newValue.length() <= maxLength)) {
                if (!newValue.matches(regex)) {
                    txt.setText(oldValue);
                }
            } else {
                txt.setText(oldValue);
            }
        });
    }
    
    public static void setTextFieldInteger(TextField txt) {
        addTextListener(txt, -1, "\\d*");
    }

    public static void setTextFieldMaxLength(TextField txt, int max) {
        addTextListener(txt, max, ".*");
    }

    public static void setTextFieldDouble(TextField txt) {
        addTextListener(txt, -1, "\\d*([\\.]\\d*)?");
    }
	 */
}