package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Colaborador;

/*
 * Classe de servico responsavel por fornecer servicos ou
 * operacoes relacionadas ao Colaboradores
 */

public class ColaboradorServices {

	
	//Busca todos os colaboradores no banco de dados.
	public List<Colaborador> findAll(){
		//Dados mockados
		List<Colaborador> list = new ArrayList<>();
		list.add(new Colaborador(22213015, "Erica", "erica@gmail.com", "635.838.482-74", "Rua ruassa", 5682, 76485726,2939469));
		list.add(new Colaborador(19207099, "Juan", "juan@gmail.com", "633.838.482-74", "Rua ruassa", 5682, 76485726,2939469));
		list.add(new Colaborador(22206137, "Kemily", "CroassintDeFrango@gmail.com", "635.838.482-74", "Rua ruassa", 5682, 76485726,2939469));
		list.add(new Colaborador(22210040, "Lucas", "Gostosao@gmail.com", "635.838.482-74", "Rua ruassa", 5682, 76485726,2939469));
		list.add(new Colaborador(22214308, "Thiagao", "Birll@gmail.com", "635.838.482-74", "Rua ruassa", 5682, 76485726,2939469));
		return list;
		
	}
}
