package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.entities.Cliente;

/*
 * Classe de servico responsavel por fornecer servicos ou
 * operacoes relacionadas ao Clientees
 */

public class ClienteServices {
	
	private ClienteDao dao = DaoFactory.createClienteDao();

	
	//Busca todos os colaboradores no banco de dados.
	public List<Cliente> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Cliente obj) {
		if(obj.getId_Cli() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
			
		}
	}

	public void remove(Cliente obj) {
		dao.deleteById(obj.getId_Cli());
	}
	
	public List<Cliente> consultName(String name) {
		return dao.findByName(name);
	}

}
