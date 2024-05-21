package model.dao;

import java.util.List;

import model.entities.LocalDeEstoque;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface LocalDeEstoqueDao {
	
	void insert(LocalDeEstoque obj);
	void update(LocalDeEstoque obj);
	void deleteById(Integer id);
	LocalDeEstoque findById(Integer id);
	List<LocalDeEstoque> findAll();
	List<LocalDeEstoque> findAllAtivos();
	List<LocalDeEstoque> findByName(String name);

}


 