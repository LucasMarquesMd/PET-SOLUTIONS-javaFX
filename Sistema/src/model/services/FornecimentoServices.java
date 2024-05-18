package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.FornecimentoDao;
import model.entities.Fornecimento;

public class FornecimentoServices {
	
	private FornecimentoDao dao = DaoFactory.createFornecimentoDao();

	public void remove(Fornecimento obj) {
		dao.deleteById(obj.getId_Forne());
		
	}

	public List<Fornecimento> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Fornecimento entity) {
		if(entity.getId_Forne() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	public Fornecimento findById(Integer id) {
		return dao.findById(id);
	}
	
}
