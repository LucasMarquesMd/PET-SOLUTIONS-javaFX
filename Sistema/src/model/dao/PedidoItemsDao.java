package model.dao;

import java.util.List;

import model.entities.PedidoItems;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface PedidoItemsDao {
	
	void insert(PedidoItems obj);
	void update(PedidoItems obj);
	void deleteById(Integer id);
	PedidoItems findById(Integer id);
	List<PedidoItems> findAll();
}


 