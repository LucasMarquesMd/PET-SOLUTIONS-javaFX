package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PedidosDao;
import model.entities.Pedidos;

public class PedidosServices {
	
	private PedidosDao dao = DaoFactory.createPedidosDao();

	public void remove(Pedidos obj) {
		dao.deleteById(obj.getId_Ped());
		
	}

	public List<Pedidos> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Pedidos entity) {
		if(entity.getId_Ped() == null) {
			dao.insert(entity);
		}else {
			dao.update(entity);

		}
	}
	
	public List<Pedidos> consultPed(Integer numero){
		return dao.consultPed(numero);
	}
	
}
