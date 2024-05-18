package model.dao;

import java.util.List;

import model.entities.NotaEstoque;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface NotaEstoqueDao {
	
	void insert(NotaEstoque obj);
	void update(NotaEstoque obj);
	void deleteById(Integer id);
	NotaEstoque findById(Integer id);
	List<NotaEstoque> findAll();
	List<NotaEstoque> findByNum(String num);
}


 