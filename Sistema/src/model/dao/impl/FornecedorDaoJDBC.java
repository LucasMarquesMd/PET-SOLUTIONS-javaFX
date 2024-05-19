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
import model.dao.FornecedorDao;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.Endereco;

public class FornecedorDaoJDBC implements FornecedorDao{

	private Connection conn = null;
	
	public FornecedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Fornecedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Fornecedor (nome_Forn, cnpj_Forn, tel_Forn, email_Forn, id_End) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome_Forn());
			st.setString(2, obj.getCnpj_Forn());
			st.setString(3, obj.getTel_Forn());
			st.setString(4, obj.getEmail_Forn());
			st.setInt(5, obj.getId_End());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);//Pega o valor do primeiro campo
					obj.setId_Forn(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Fornecedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Fornecedor  "
					+ "SET nome_Forn = ?, cnpj_Forn = ?, tel_Forn = ?, email_Forn = ? "
					+ "WHERE id_Forn = ?");
			
			st.setString(1, obj.getNome_Forn());
			st.setString(2, obj.getCnpj_Forn());
			st.setString(3, obj.getTel_Forn());
			st.setString(4, obj.getEmail_Forn());
			
			st.setInt(5, obj.getId_Forn());
			
			st.executeUpdate();

			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM Fornecedor WHERE id_Forn = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Fornecedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Fornecedor.*, Endereco.* "
					+ "FROM Fornecedor "
					+ "INNER JOIN Endereco "
					+ "ON Fornecedor.id_End = Endereco.id_End "
					+ "WHERE id_Forn = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Fornecedor obj = instantiateFornecedor(rs, end);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	@Override
	public List<Fornecedor> findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT Fornecedor.*, Endereco.* "
					+ "FROM Fornecedor "
					+ "INNER JOIN Endereco "
					+ "ON Fornecedor.id_End = Endereco.id_End "
					+ "WHERE nome_Forn LIKE ?");
			
			st.setString(1, name + "%");
			rs = st.executeQuery();
			
			List<Fornecedor> list = new ArrayList<>();
			
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Fornecedor obj = instantiateFornecedor(rs, end);
				
				list.add(obj);
			}//end while
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}//End findAll
	

	@Override
	public List<Fornecedor> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT Fornecedor.*, Endereco.*  "
					+ "FROM Fornecedor "
					+ "INNER JOIN Endereco "
					+ "ON Fornecedor.id_End = Endereco.id_End "
					+ "ORDER BY nome_Forn");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Fornecedor> list = new ArrayList<>();
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Fornecedor obj = instantiateFornecedor(rs, end);
				
				list.add(obj);
			}//end while
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}//End findAll
	
	private Endereco instantiateEndereco(ResultSet rs) throws SQLException{
		Endereco obj = new Endereco();
		obj.setId_End(rs.getInt("id_End"));
		obj.setRua_End(rs.getString("rua_End"));
		obj.setBairro_End(rs.getNString("bairro_End"));
		obj.setCidade_End(rs.getString("cidade_End"));
		obj.setCep_End(rs.getInt("cep_End"));
		obj.setNum_End(rs.getInt("num_End"));

		
		return obj;
	}
	
	private Fornecedor instantiateFornecedor(ResultSet rs, Endereco end) throws SQLException{
		Fornecedor obj = new Fornecedor();
		
		obj.setId_Forn(rs.getInt("id_Forn"));
		obj.setNome_Forn(rs.getString("nome_Forn"));
		obj.setCnpj_Forn(rs.getString("cnpj_Forn"));
		obj.setTel_Forn(rs.getString("tel_Forn"));
		obj.setEmail_Forn(rs.getString("email_Forn"));
		obj.setId_End(rs.getInt("id_End"));

		obj.setEndereco(end);
		
		return obj;
	}


}
