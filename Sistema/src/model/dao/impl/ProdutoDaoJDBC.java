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
import model.dao.ProdutoDao;
import model.entities.Colaborador;
import model.entities.Endereco;
import model.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDao {

	private Connection conn = null;

	public ProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO " + "Produto (nome_Prod, desc_Prod, preco_Forn, preco_Cli) "
					+ "VALUES " + "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome_Prod());
			st.setString(2, obj.getDesc_Prod());
			st.setDouble(3, 0.0);
			st.setDouble(4, obj.getPreco_Prod());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();// Recupera a chave primaria do objeto inserido

				while (rs.next()) {
					int id = rs.getInt(1);// Pega o valor do primeiro campo
					obj.setId_Prod(id);
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
	public void update(Produto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Produto  "
					+ "SET nome_Prod = ?, desc_Prod = ?, preco_Forn = ?, preco_Cli = ? " + "WHERE id_Prod = ?");

			st.setString(1, obj.getNome_Prod());
			st.setString(2, obj.getDesc_Prod());
			st.setDouble(3, 0.0);
			st.setDouble(4, obj.getPreco_Prod());

			st.setInt(5, obj.getId_Prod());

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
			st = conn.prepareStatement("DELETE FROM Produto WHERE id_Prod = ? ");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Produto findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM Produto "
					+ "WHERE id_Prod = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Produto obj = instantiateProduct(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Produto> findAll() {
		// PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira
		// vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  " + "FROM Produto " + "ORDER BY nome_Prod");

			// Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();// Cria uma tabela, apaontando nenhum valor

			List<Produto> list = new ArrayList<>();

			while (rs.next()) {
				Produto obj = instantiateProduct(rs);

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
	public List<Produto> findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM Produto "
					+ "WHERE nome_Prod LIKE ?");
			
			st.setString(1, name + "%");
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			
			
			while(rs.next()) {
				Produto obj = instantiateProduct(rs);
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



	private Produto instantiateProduct(ResultSet rs) throws SQLException {
		Produto obj = new Produto();

		obj.setId_Prod(rs.getInt("id_Prod"));
		obj.setNome_Prod(rs.getString("nome_Prod"));
		obj.setDesc_Prod(rs.getString("desc_Prod"));
		obj.setPreco_Prod(rs.getDouble("preco_Cli"));

		return obj;

	}




}
