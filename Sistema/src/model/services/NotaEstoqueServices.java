package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.NotaEstoqueDao;
import model.entities.NotaEstoque;

public class NotaEstoqueServices {
	
	private NotaEstoqueDao dao = DaoFactory.createNotaEstoqueDao();

	public void remove(NotaEstoque obj) {
		dao.deleteById(obj.getId_Nota());
		
	}

	public List<NotaEstoque> findAll() {
		return dao.findAll();
	}
	
	public List<NotaEstoque> findByNum(String num) {
		return dao.findByNum(num);
	}

	public void saveOrUpdate(NotaEstoque entity) {
		if(entity.getId_Nota() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
}
