package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PedidoItemsDao;
import model.entities.PedidoItems;

public class PedidoItemsServices {
	
	private PedidoItemsDao dao = DaoFactory.createPedidoItemsDao();

	public void remove(PedidoItems obj) {
		dao.deleteById(obj.getId_Prod());
		
	}

	public List<PedidoItems> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(PedidoItems entity) {
		if(entity.getId_Prod() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	

}
