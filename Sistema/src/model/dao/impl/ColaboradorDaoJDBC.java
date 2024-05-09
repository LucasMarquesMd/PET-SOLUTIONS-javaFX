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
import model.dao.ColaboradorDao;
import model.entities.Colaborador;

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
					+ "Colaborador (nome_Col, cpf_Col, tel_Col, cel_Col, email_Col, user_Col, user_Senha) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getCnpj_cpf());
			st.setInt(3, obj.getTelefone());
			st.setInt(4, obj.getCelular());
			st.setString(5, obj.getEmail());
			st.setString(6, obj.getUser_Col());
			st.setString(7, obj.getUser_Senha());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);//Pega o valor do primeiro campo
					obj.setId_End(id);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Colaborador findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Colaborador> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  "
					+ "FROM Colaborador "
					+ "ORDER BY nome_Col");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Colaborador> list = new ArrayList<>();
			
			while(rs.next()) {
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

}
