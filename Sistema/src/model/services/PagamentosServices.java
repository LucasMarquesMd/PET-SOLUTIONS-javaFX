package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PagamentosDao;
import model.entities.Pagamentos;

public class PagamentosServices {
	
	private PagamentosDao dao = DaoFactory.createPagamentosDao();

	public void remove(Pagamentos obj) {
		dao.deleteById(obj.getId_Pag());
		
	}

	public List<Pagamentos> findAll() {
		return dao.findAll();
	}
	
	public Pagamentos findById(Integer id) {
		return dao.findById(id);
	}

	public void saveOrUpdate(Pagamentos entity) {
		if(entity.getId_Pag() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	public List<Pagamentos> consultPed(String numero){
		return dao.consultPag(numero);
	}
	
}
