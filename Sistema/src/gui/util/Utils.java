package gui.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {
	
	//getSource() -> retorna o objeto origem que iniciou o evento
	//(Node) -> casting devido o objeto retornado ser do tipo node (botoes, textfilds,...)
	//getScene() -> retorna a cena do objeto origem
	//getWindoe() -> Super-classe da Stage, retorna o objeto janela que a cena pertence
	//(Stage) -> Down-casting pois queremos o Stage 
	

	//Retorna o palco atual da chamada do evento
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	
	//Faz o tratamento da Excessao - caso invalido
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		}catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			
			{
				datePicker.setPromptText(format.toLowerCase());
			}
			
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				}else {
					return "";
				}
			}
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				}else {
					return null;
				}
			}
		});
	}//end
	
	public static String doubleToString(Double value) {
	    if (value == null) {
	        return "";
	    }
	    return String.format("%.2f", value);
	}
}
