package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.EstoqueDao;
import model.entities.Estoque;
import model.entities.LocalDeEstoque;
import model.entities.Produto;
import model.entities.enums.LocalStatus;

public class EstoqueDaoJDBC implements EstoqueDao {

	private Connection conn = null;

	public EstoqueDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Estoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO " 
					+ "Estoque (qtd_Prod_Est, dt_Est, id_Local, id_Prod) "
					+ "VALUES " 
					+ "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getQt_Prod_Est());
			st.setDate(2, new java.sql.Date(obj.getDt_Est().getTime()));
			st.setInt(3, obj.getId_Local());
			st.setInt(4, obj.getId_Prod());


			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();// Recupera a chave primaria do objeto inserido

				while (rs.next()) {
					int id = rs.getInt(1);// Pega o valor do primeiro campo
					obj.setId_Est(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}// end insert

	@Override
	public void update(Estoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Estoque  "
					+ "SET qtd_Prod_Est = ?, dt_Est = ?, id_Local = ?, id_Prod = ? "
					+ "WHERE id_Est = ?");

			st.setInt(1, obj.getQt_Prod_Est());
			st.setDate(2, new java.sql.Date(obj.getDt_Est().getTime()));
			st.setInt(3, obj.getId_Local());
			st.setInt(4, obj.getId_Prod());
			
			st.setInt(5, obj.getId_Est());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM Estoque WHERE id_Est = ? ");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Estoque findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM Estoque "
					+ "INNER JOIN Local_Estoque "
					+ "ON Estoque.id_Local = Local_Estoque.id_Local "
					+ "INNER JOIN Produto "
					+ "ON Estoque.id_Prod = Produto.id_Prod "
					+ "WHERE id_Est = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Produto prod = instantiateProduto(rs);
				LocalDeEstoque local = instantiateLocal(rs);
				Estoque obj = instantiateEstoque(rs, prod, local);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Estoque> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT *  " 
					+ "FROM Estoque "
					+ "INNER JOIN Local_Estoque ON Estoque.id_Local = Local_Estoque.id_Local "
					+ "INNER JOIN Produto ON Estoque.id_Prod = Produto.id_Prod ");

			rs = st.executeQuery();

			List<Estoque> list = new ArrayList<>();

			while (rs.next()) {
				Produto prod = instantiateProduto(rs);
				LocalDeEstoque local = instantiateLocal(rs);
				Estoque obj = instantiateEstoque(rs, prod, local);

				list.add(obj);
			} // end while

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Produto instantiateProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();
		
		obj.setId_Prod(rs.getInt("id_Prod"));
		obj.setNome_Prod(rs.getString("nome_Prod"));
		obj.setDesc_Prod(rs.getString("desc_Prod"));
		obj.setPreco_Prod(rs.getDouble("preco_Cli"));

		return obj;
	}
	
	private LocalDeEstoque instantiateLocal(ResultSet rs) throws SQLException {
		LocalDeEstoque obj = new LocalDeEstoque();
		
		obj.setId_Local(rs.getInt("id_Local"));
		obj.setNome_Local(rs.getString("nome_Local"));
		obj.setDesc_Local(rs.getString("desc_Local"));
		obj.setSit_Local(LocalStatus.valueOf(rs.getString("sit_Local")));//Converte a string para enum
		
		return obj;
	}


	private Estoque instantiateEstoque(ResultSet rs, Produto prod, LocalDeEstoque local) throws SQLException {
		Estoque obj = new Estoque();

		obj.setId_Est(rs.getInt("id_Est"));
		obj.setQt_Prod_Est(rs.getInt("qtd_Prod_Est"));
		obj.setDt_Est(new java.util.Date(rs.getDate("dt_Est").getTime()));
		obj.setId_Local(rs.getInt("id_Local"));
		obj.setId_Prod(rs.getInt("id_Prod"));
		
		obj.setProduto(prod);
		obj.setLocalDeEstoque(local);

		return obj;

	}

}
