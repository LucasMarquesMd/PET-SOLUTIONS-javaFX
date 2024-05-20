package gui.util;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

/*
 * Classe utilitaria
 * Controle de validacoes para textfilds
 */

public class Constraints {


	//textProperty() -> representa o conteudo textual do campo de texto de um TextFild
	//addListener() -> Tipo Obsevable,reage a alteracoes automaticamente
	
	
//	public static void setTextFieldInteger(TextField txt) {
//		txt.textProperty().addListener((obs, oldValue, newValue) -> {
//	        if (newValue != null && !newValue.matches("\\d*")) //Se o valor nao for nulo e nao for inteiro
//	        {
//	        	txt.setText(oldValue);//volte para o valor antigo
//	        }
//	    });
//	}
//
//	public static void setTextFieldMaxLength(TextField txt, int max) {
//		txt.textProperty().addListener((obs, oldValue, newValue) -> {
//	        if (newValue != null && newValue.length() > max) //Se o valor exceder o numero maximo de carteres
//	        {
//	        	txt.setText(oldValue);//Volte ao valor original
//	        }
//	    });
//	}
//	
//	public static void setTextAreaMaxLength(TextArea txt, int max) {
//		txt.textProperty().addListener((obs, oldValue, newValue) -> {
//	        if (newValue != null && newValue.length() > max) //Se o valor exceder o numero maximo de carteres
//	        {
//	        	txt.setText(oldValue);//Volte ao valor original
//	        }
//	    });
//	}
//
//	public static void setTextFieldDouble(TextField txt) {
//		txt.textProperty().addListener((obs, oldValue, newValue) -> {
//		    	if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?"))//Se o novo valor nao for um tipo Double
//		    	{
//                    txt.setText(oldValue);//Volte ao valor original
//                }
//		    });
//	}
	
	
	private static void addTextListener(TextInputControl txt, int minLength, int maxLength, String regex) {
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                if ((minLength == -1 || newValue.length() >= minLength) &&
                    (maxLength == -1 || newValue.length() <= maxLength) &&
                    newValue.matches(regex)) {
                    // Entrada válida, não faça nada
                } else {
                    txt.setText(oldValue); // Reverte para o valor antigo
                }
            }
        });
    }

    public static void setTextFieldInteger(TextField txt) {
        addTextListener(txt, -1, -1, "\\d*");
    }

    public static void setTextFieldMaxLength(TextField txt, int max) {
        addTextListener(txt, -1, max, ".*");
    }

    public static void setTextAreaMaxLength(TextArea txt, int max) {
        addTextListener(txt, -1, max, ".*");
    }

    public static void setTextFieldMinLength(TextField txt, int min) {
        addTextListener(txt, min, -1, ".*");
    }

    public static void setTextAreaMinLength(TextArea txt, int min) {
        addTextListener(txt, min, -1, ".*");
    }

    public static void setTextFieldDouble(TextField txt) {
        addTextListener(txt, -1, -1, "\\d*([\\.]\\d*)?");
    }
    
    public static void setTextFieldOnlyLetters(TextField txt) {
        addTextListener(txt, -1, -1, "[a-zA-Z]*");
    }
    
    public static void setTextAreaOnlyLetters(TextArea txt) {
        addTextListener(txt, -1, -1, "[a-zA-Z]*");
    }
	
	
}