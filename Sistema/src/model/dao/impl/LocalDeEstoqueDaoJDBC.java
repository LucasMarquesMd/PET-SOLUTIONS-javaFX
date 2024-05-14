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
import model.dao.LocalDeEstoqueDao;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.entities.LocalDeEstoque;
import model.entities.enums.LocalStatus;

public class LocalDeEstoqueDaoJDBC implements LocalDeEstoqueDao {

	private Connection conn = null;

	public LocalDeEstoqueDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(LocalDeEstoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO " 
					+ "Local_Estoque (nome_Local, sit_Local, desc_Local) "
					+ "VALUES " 
					+ "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome_Local());
			st.setString(2, obj.getSit_Local().toString());//Converte o enum para string
			st.setString(3, obj.getDesc_Local());


			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();// Recupera a chave primaria do objeto inserido

				while (rs.next()) {
					int id = rs.getInt(1);// Pega o valor do primeiro campo
					obj.setId_Local(id);
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
	public void update(LocalDeEstoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Local_Estoque  "
					+ "SET nome_Local = ?, desc_Local = ?, sit_Local = ? "
					+ "WHERE id_Local = ?");

			st.setString(1, obj.getNome_Local());
			st.setString(2, obj.getDesc_Local());
			st.setString(3, obj.getSit_Local().toString());
			st.setDouble(4, obj.getId_Local());

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
			st = conn.prepareStatement("DELETE FROM Local_Estoque WHERE id_Local = ? ");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public LocalDeEstoque findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM Local_Estoque "
					+ "WHERE id_Local = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				LocalDeEstoque obj = instantiateLocalDeEstoque(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<LocalDeEstoque> findAll() {
		// PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira
		// vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  " + "FROM Local_Estoque " + "ORDER BY nome_Local");

			// Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();// Cria uma tabela, apaontando nenhum valor

			List<LocalDeEstoque> list = new ArrayList<>();

			while (rs.next()) {
				LocalDeEstoque obj = instantiateLocalDeEstoque(rs);

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
	public List<LocalDeEstoque> findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM Local_Estoque "
					+ "WHERE nome_Local LIKE ?");
			
			st.setString(1, name + "%");
			rs = st.executeQuery();
			
			List<LocalDeEstoque> list = new ArrayList<>();
			
			
			while(rs.next()) {
				LocalDeEstoque obj = instantiateLocalDeEstoque(rs);
				list.add(obj);
			}//end while
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private LocalDeEstoque instantiateLocalDeEstoque(ResultSet rs) throws SQLException {
		LocalDeEstoque obj = new LocalDeEstoque();

		obj.setId_Local(rs.getInt("id_Local"));
		obj.setNome_Local(rs.getString("nome_Local"));
		obj.setDesc_Local(rs.getString("desc_Local"));
		obj.setSit_Local(LocalStatus.valueOf(rs.getString("sit_Local")));

		return obj;

	}

}
