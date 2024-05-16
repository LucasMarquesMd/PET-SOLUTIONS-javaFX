package model.dao;

import java.util.List;

import model.entities.Produto;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface ProdutoDao {
	
	void insert(Produto obj);
	void update(Produto obj);
	void deleteById(Integer id);
	Produto findById(Integer id);
	List<Produto> findAll();
	List<Produto> findByName(String name);
	//List<Produto> listfindById(Integer id);
}


 