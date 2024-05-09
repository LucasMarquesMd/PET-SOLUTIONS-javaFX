package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EnderecoDao;
import model.entities.Endereco;

public class EnderecoService {

	private EnderecoDao dao =  DaoFactory.createEnderecoDao();
	
	public List<Endereco> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Endereco obj) {
		if(obj.getId_End() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
}
