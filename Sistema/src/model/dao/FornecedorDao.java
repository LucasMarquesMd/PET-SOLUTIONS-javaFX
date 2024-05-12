package model.dao;

import java.util.List;

import model.entities.Fornecedor;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface FornecedorDao {
	
	void insert(Fornecedor obj);
	void update(Fornecedor obj);
	void deleteById(Integer id);
	Fornecedor findById(Integer id);
	List<Fornecedor> findAll();
	List<Fornecedor> findByName(String name);
}


 