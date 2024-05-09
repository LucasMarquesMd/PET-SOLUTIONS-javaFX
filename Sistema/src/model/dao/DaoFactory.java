package model.dao;

import db.DB;
import model.dao.impl.ColaboradorDaoJDBC;
import model.dao.impl.EnderecoDaoJDBC;

//Classe responsavel por instanciar a implentacao dos Dao
public class DaoFactory {

	public static ColaboradorDao createColaboradorDao() {
		return new ColaboradorDaoJDBC(DB.getConnection());
	}
	
	public static EnderecoDao createEnderecoDao() {
		return new EnderecoDaoJDBC(DB.getConnection());
	}
}
