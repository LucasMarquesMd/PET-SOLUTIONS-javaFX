package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EstoqueDao;
import model.entities.Estoque;

public class EstoqueServices {
	
	private EstoqueDao dao = DaoFactory.createEstoqueDao();

	public void remove(Estoque obj) {
		dao.deleteById(obj.getId_Est());
		
	}

	public List<Estoque> findAll() {
		return dao.findAll();
	}
	
	public Estoque findById(Integer id) {
		return dao.findById(id);
	}

	public void saveOrUpdate(Estoque entity) {
		if(entity.getId_Est() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
}
