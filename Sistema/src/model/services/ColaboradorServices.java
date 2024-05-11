package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.ColaboradorDao;
import model.dao.DaoFactory;
import model.entities.Colaborador;

/*
 * Classe de servico responsavel por fornecer servicos ou
 * operacoes relacionadas ao Colaboradores
 */

public class ColaboradorServices {
	
	private ColaboradorDao dao = DaoFactory.createColaboradorDao();

	
	//Busca todos os colaboradores no banco de dados.
	public List<Colaborador> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Colaborador obj) {
		if(obj.getIdColab() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
			System.out.println("Colab Alterado");
		}
	}
	
	
	public void remove(Colaborador obj) {
		dao.deleteById(obj.getIdColab());
	}
}
