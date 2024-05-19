package model.dao;

import java.util.List;

import model.entities.Pagamentos;

//Interface para contrato de implementacao de acesso de dados do colaborador

public interface PagamentosDao {
	
	void insert(Pagamentos obj);
	void update(Pagamentos obj);
	void deleteById(Integer id);
	Pagamentos findById(Integer id);
	List<Pagamentos> findAll();
	List<Pagamentos> consultPag(String numero);
}


 