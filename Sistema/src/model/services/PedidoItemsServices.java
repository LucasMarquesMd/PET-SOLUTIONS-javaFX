package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PedidoItemsDao;
import model.entities.PedidoItems;

public class PedidoItemsServices {
	
	private PedidoItemsDao dao = DaoFactory.createPedidoItemsDao();

	public void remove(PedidoItems obj) {
		dao.deleteById(obj.getId_PedIt());
		
	}

	public List<PedidoItems> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(PedidoItems entity) {
		if(entity.getId_PedIt() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	public List<PedidoItems> findItemsProd(Integer id) {
		return dao.findItemsProd(id);
	}
	
	public PedidoItems findByIdPed(Integer id_Ped, Integer id_Prod) {
		return dao.findByIdPed(id_Ped, id_Prod);
	}
	
	public PedidoItems findById(Integer id) {
		return dao.findById(id);
	}

}
