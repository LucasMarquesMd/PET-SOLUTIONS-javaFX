package model.dao;

import db.DB;
import model.dao.impl.ColaboradorDaoJDBC;

//Classe responsavel por instanciar a implentacao dos Dao
public class DaoFactory {

	public static ColaboradorDao createColaboradorDao() {
		return new ColaboradorDaoJDBC(DB.getConnection());
	}
}
