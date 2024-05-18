package model.dao;

import java.util.List;

import model.entities.Fornecimento;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface FornecimentoDao {
	
	void insert(Fornecimento obj);
	void update(Fornecimento obj);
	void deleteById(Integer id);
	Fornecimento findById(Integer id);
	List<Fornecimento> findAll();
	List<Fornecimento> findByName(String name);
}


 