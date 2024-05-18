package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LocalDeEstoqueDao;
import model.entities.LocalDeEstoque;

public class LocalDeEstoqueServices {
	
	private LocalDeEstoqueDao dao = DaoFactory.createLocalDeEstoqueDao();

	public void remove(LocalDeEstoque obj) {
		dao.deleteById(obj.getId_Local());
		
	}

	public List<LocalDeEstoque> consultName(String name) {
		return dao.findByName(name);
	}

	public List<LocalDeEstoque> findAll() {
		return dao.findAll();
	}
	
	public LocalDeEstoque findById(Integer id) {
		return dao.findById(id);
	}

	public void saveOrUpdate(LocalDeEstoque entity) {
		if(entity.getId_Local() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	

}
