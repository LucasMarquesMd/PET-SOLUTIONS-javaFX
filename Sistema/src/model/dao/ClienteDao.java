package model.dao;

import java.util.List;

import model.entities.Cliente;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface ClienteDao {
	
	void insert(Cliente obj);
	void update(Cliente obj);
	void deleteById(Integer id);
	Cliente findById(Integer id);
	List<Cliente> findAll();
	List<Cliente> findByName(String name);
}


 