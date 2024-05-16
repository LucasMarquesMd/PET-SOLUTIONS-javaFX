package model.dao;

import java.util.List;

import model.entities.Pedidos;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface PedidosDao {
	
	void insert(Pedidos obj);
	void update(Pedidos obj);
	void deleteById(Integer id);
	Pedidos findById(Integer id);
	List<Pedidos> findAll();
	List<Pedidos> consultPed(String numero);
}


 