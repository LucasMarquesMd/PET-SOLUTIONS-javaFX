package model.dao;

import java.util.List;

import model.entities.Estoque;



public interface EstoqueDao {
	
	void insert(Estoque obj);
	void update(Estoque obj);
	void deleteById(Integer id);
	Estoque findById(Integer id);
	List<Estoque> findAll();

}


 