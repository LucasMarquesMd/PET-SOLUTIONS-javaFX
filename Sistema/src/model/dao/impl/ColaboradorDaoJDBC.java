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
import model.dao.ColaboradorDao;
import model.entities.Colaborador;
import model.entities.Endereco;

public class ColaboradorDaoJDBC implements ColaboradorDao{

	private Connection conn = null;
	
	public ColaboradorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Colaborador obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Colaborador (nome_Col, cpf_Col, tel_Col, cel_Col, email_Col, user_Col, user_Senha, level_Access, id_End) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getCnpj_cpf());
			st.setInt(3, obj.getTelefone());
			st.setInt(4, obj.getCelular());
			st.setString(5, obj.getEmail());
			st.setString(6, obj.getUser_Col());
			st.setString(7, obj.getUser_Senha());
			st.setInt(8, obj.getLevel_Access());
			st.setInt(9, obj.getId_End());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);//Pega o valor do primeiro campo
					//obj.setId_End(id);
					obj.setIdColab(id);
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
	public void update(Colaborador obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Colaborador  "
					+ "SET nome_Col = ?, cpf_Col = ?, tel_Col = ?, cel_Col = ?, email_Col = ?, user_Col = ?, user_Senha = ?, level_Access = ? "
					+ "WHERE id_Col = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getCnpj_cpf());
			st.setInt(3, obj.getTelefone());
			st.setInt(4, obj.getCelular());
			st.setString(5, obj.getEmail());
			st.setString(6, obj.getUser_Col());
			st.setString(7, obj.getUser_Senha());
			st.setInt(8, obj.getLevel_Access());
			
			st.setInt(9, obj.getIdColab());
			
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
					"DELETE FROM Colaborador WHERE id_Col = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Colaborador findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Colaborador.*, Endereco.* "
					+ "FROM Colaborador "
					+ "INNER JOIN Endereco "
					+ "ON Colaborador.id_End = Endereco.id_End "
					+ "WHERE id_Col = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Colaborador obj = instantiateColaborador(rs, end);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	@Override
	public List<Colaborador> findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT Colaborador.*, Endereco.* "
					+ "FROM Colaborador "
					+ "INNER JOIN Endereco "
					+ "ON Colaborador.id_End = Endereco.id_End "
					+ "WHERE nome_Col LIKE ?");
			
			st.setString(1, name + "%");
			rs = st.executeQuery();
			
			List<Colaborador> list = new ArrayList<>();
			
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Colaborador obj = instantiateColaborador(rs, end);
				
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
	public Colaborador ValidatingUser(String userName, String passworld) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT Colaborador.*, Endereco.* "
					+ "FROM Colaborador "
					+ "INNER JOIN Endereco "
					+ "ON Colaborador.id_End = Endereco.id_End "
					+ "WHERE user_Col = ? "
					+ "AND user_Senha = ? ");
			
			st.setString(1, userName);
			st.setString(2, passworld);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Colaborador obj = instantiateColaborador(rs, end);
				return obj;
			}
			return null;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}//End findAll

	@Override
	public List<Colaborador> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  "
					+ "FROM Colaborador "
					+ "INNER JOIN Endereco "
					+ "ON Colaborador.id_End = Endereco.id_End "
					+ "ORDER BY nome_Col");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Colaborador> list = new ArrayList<>();
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Colaborador obj = instantiateColaborador(rs, end);
				
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
	
	private Colaborador instantiateColaborador(ResultSet rs, Endereco end) throws SQLException{
		Colaborador obj = new Colaborador();
		
		obj.setIdColab(rs.getInt("id_Col"));
		obj.setName(rs.getString("nome_Col"));
		obj.setCnpj_cpf(rs.getString("cpf_Col"));
		obj.setTelefone(rs.getInt("tel_Col"));
		obj.setCelular(rs.getInt("cel_Col"));
		obj.setEmail(rs.getString("email_Col"));
		obj.setId_End(rs.getInt("id_End"));
		obj.setUser_Col(rs.getString("user_Col"));
		obj.setUser_Senha(rs.getString("user_Senha"));
		obj.setLevel_Access(rs.getInt("level_Access"));
		
		obj.setEndereco(end);
		
		return obj;
	}

}
