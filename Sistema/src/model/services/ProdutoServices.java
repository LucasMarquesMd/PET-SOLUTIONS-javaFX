package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Produto;

public class ProdutoServices {
	
	private ProdutoDao dao = DaoFactory.createProdutoDao();

	public void remove(Produto obj) {
		dao.deleteById(obj.getId_Prod());
		
	}

	public List<Produto> consultName(String name) {
		return dao.findByName(name);
	}

	public List<Produto> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Produto entity) {
		if(entity.getId_Prod() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	//Busca os produtos po id
	public Produto findById(Integer id) {
		return dao.findById(id);
	}
	
	

}
