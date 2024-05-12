package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.FornecedorDao;
import model.dao.DaoFactory;
import model.entities.Fornecedor;

/*
 * Classe de servico responsavel por fornecer servicos ou
 * operacoes relacionadas ao Fornecedores
 */

public class FornecedorServices {
	
	private FornecedorDao dao = DaoFactory.createFornecedorDao();

	
	//Busca todos os colaboradores no banco de dados.
	public List<Fornecedor> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Fornecedor obj) {
		if(obj.getId_Forn() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
			
		}
	}

	public void remove(Fornecedor obj) {
		dao.deleteById(obj.getId_Forn());
	}
	
	public List<Fornecedor> consultName(String name) {
		return dao.findByName(name);
	}

}
