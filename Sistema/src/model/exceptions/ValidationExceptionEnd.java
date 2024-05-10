package model.exceptions;

import java.util.HashMap;
import java.util.Map;

//Os erros dos formularios serao guardados para retornar ao usuario
//Ao inves de tratalos com interrupcoes - serao notificados para a correcao pelo usuario

public class ValidationExceptionEnd extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	
	//Map -> Colecao de pares chave valor (Dicionario)
	//Sera utilizado para armazenar os erros gerados
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationExceptionEnd(String msg) {
		super(msg);
	}
	
	//Retorna a colecao de erros
	public Map<String, String> getErrors(){
		return errors;
	}
	
	public void addErrors(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}

}
