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
import model.dao.EnderecoDao;
import model.entities.Endereco;

public class EnderecoDaoJDBC implements EnderecoDao{
	
	private Connection conn = null;
	
	
	public EnderecoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	

	@Override
	public void insert(Endereco obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Endereco (cep_End, num_End, rua_End, bairro_End, cidade_End) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);//retorna a chave primaria
			
			st.setInt(1, obj.getCep_End());
			st.setInt(2, obj.getNum_End());
			st.setString(3, obj.getRua_End());
			st.setString(4, obj.getBairro_End());
			st.setString(5, obj.getCidade_End());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				
				//Adiciona o id no objeto novamente
				if(rs.next()) {
					int id = rs.getInt(1);//pega o id
					obj.setId_End(id);
				}
				else {
					throw new DbException("Unexpected error! No rows affected!");
				}
			}

		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}//end insert()

	@Override
	public void update(Endereco obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Endereco "
					+ "SET cep_End = ?, num_End = ?, rua_End = ?, bairro_End = ?, cidade_End = ? "
					+ "WHERE id_End = ?");
			
			st.setInt(1, obj.getCep_End());
			st.setInt(2, obj.getNum_End());
			st.setString(3, obj.getRua_End());
			st.setString(4, obj.getBairro_End());
			st.setString(5, obj.getCidade_End());
			
			st.setInt(6, obj.getId_End());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Endereco findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *  "
					+ "FROM Endereco "
					+ "WHERE id_End = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Endereco obj = instantiateEndereco(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Endereco> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM endereco ");
			
			rs = st.executeQuery();
			
			List<Endereco> list = new ArrayList<>();
			
			while(rs.next()) {
				
				Endereco end = new Endereco();
				end.setId_End(rs.getInt("id_End"));
				end.setCep_End(rs.getInt("cep_End"));
				end.setNum_End(rs.getInt("num_End"));
				end.setRua_End(rs.getString("rua_End"));
				end.setBairro_End(rs.getString("bairro_End"));
				end.setCidade_End(rs.getString("cidade_End"));
				
				list.add(end);
				
			}
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException("Erro! " + e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}
	
	
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

}
