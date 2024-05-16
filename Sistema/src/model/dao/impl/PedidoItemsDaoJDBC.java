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
import model.dao.PedidoItemsDao;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.entities.PedidoItems;

public class PedidoItemsDaoJDBC implements PedidoItemsDao {

	private Connection conn = null;

	public PedidoItemsDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(PedidoItems obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO " + "PedidoItems (qt_PedIt, preco_PedIt, id_Prod, id_Ped) "
					+ "VALUES " + "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getQt_PedIt());
			st.setDouble(2, obj.getPreco_PedIt());
			st.setInt(3, obj.getId_Prod());
			st.setInt(4, obj.getId_Ped());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();// Recupera a chave primaria do objeto inserido

				while (rs.next()) {
					int id = rs.getInt(1);// Pega o valor do primeiro campo
					obj.setId_PedIt(id);
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
	public void update(PedidoItems obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE PedidoItems  "
					+ "SET id_PedIt = ?, qt_PedIt = ?, preco_PedIt = ? " + "WHERE id_PedIt = ?");

			st.setInt(1, obj.getId_PedIt());
			st.setInt(2, obj.getQt_PedIt());
			st.setDouble(3, obj.getPreco_PedIt());

			st.setInt(4, obj.getId_PedIt());

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
			st = conn.prepareStatement("DELETE FROM PedidoItems WHERE id_PedIt = ? ");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public PedidoItems findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM PedidoItems "
					+ "WHERE id_PedIt = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				PedidoItems obj = instantiatePedItuct(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<PedidoItems> findAll() {
		// PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira
		// vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  " + "FROM PedidoItems " + "ORDER BY id_PedIt");

			// Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();// Cria uma tabela, apaontando nenhum valor

			List<PedidoItems> list = new ArrayList<>();

			while (rs.next()) {
				PedidoItems obj = instantiatePedItuct(rs);

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


	private PedidoItems instantiatePedItuct(ResultSet rs) throws SQLException {
		PedidoItems obj = new PedidoItems();

		obj.setId_Ped(rs.getInt("id_Ped"));
		obj.setId_PedIt(rs.getInt("id_PedIt"));
		obj.setId_Prod(rs.getInt("id_Prod"));
		obj.setQt_PedIt(rs.getInt("qt_PedIt"));
		obj.setPreco_PedIt(rs.getDouble("preco_PedIt"));

		return obj;

	}

	@Override
	public List<PedidoItems> findItemsProd(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *  "
				+ "FROM PedidoItems "
				+ "INNER JOIN Pedido "
				+ "ON PedidoItems.id_Ped = Pedido.id_Ped " 
				+ "WHERE Pedido.id_Ped = ?");
			
			st.setInt(1, id);

			rs = st.executeQuery();

			List<PedidoItems> list = new ArrayList<>();

			while (rs.next()) {
				PedidoItems obj = instantiatePedItuct(rs);

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

	@Override
	public PedidoItems findByIdPed(Integer id_Ped, Integer id_Prod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM PedidoItems "
					+ "WHERE id_Ped = ? "
					+ "AND id_Prod = ? ");
			
			st.setInt(1, id_Ped);
			st.setInt(2, id_Prod);
			rs = st.executeQuery();
			
			if(rs.next()) {
				PedidoItems obj = instantiatePedItuct(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

}
