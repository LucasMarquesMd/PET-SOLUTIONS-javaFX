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
import model.dao.ClienteDao;
import model.entities.Cliente;
import model.entities.Produto;
import model.entities.Endereco;

public class ClienteDaoJDBC implements ClienteDao{

	private Connection conn = null;
	
	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Cliente (nome_Cli, cpf_Cli, tel_Cli, cel_Cli, email_Cli, id_End) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome_Cli());
			st.setString(2, obj.getCpf_Cli());
			st.setInt(3, obj.getTel_Cli());
			st.setInt(4, obj.getCel_Cli());
			st.setString(5, obj.getEmail_Cli());
			st.setInt(6, obj.getId_End());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);//Pega o valor do primeiro campo
					obj.setId_Cli(id);
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
	public void update(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Cliente  "
					+ "SET nome_Cli = ?, cpf_Cli = ?, cel_Cli = ?, tel_Cli = ?, email_Cli = ? "
					+ "WHERE id_Cli = ?");
			
			st.setString(1, obj.getNome_Cli());
			st.setString(2, obj.getCpf_Cli());
			st.setInt(3, obj.getTel_Cli());
			st.setInt(4, obj.getCel_Cli());
			st.setString(5, obj.getEmail_Cli());
			
			st.setInt(6, obj.getId_Cli());
			
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
					"DELETE FROM Cliente WHERE id_Cli = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Cliente findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Cliente.*, Endereco.* "
					+ "FROM Cliente "
					+ "INNER JOIN Endereco "
					+ "ON Cliente.id_End = Endereco.id_End "
					+ "WHERE id_Cli = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Cliente obj = instantiateCliente(rs, end);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	@Override
	public List<Cliente> findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT Cliente.*, Endereco.* "
					+ "FROM Cliente "
					+ "INNER JOIN Endereco "
					+ "ON Cliente.id_End = Endereco.id_End "
					+ "WHERE nome_Cli LIKE ?");
			
			st.setString(1, name + "%");
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Cliente obj = instantiateCliente(rs, end);
				
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
	public List<Cliente> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT Cliente.*, Endereco.*  "
					+ "FROM Cliente "
					+ "INNER JOIN Endereco "
					+ "ON Cliente.id_End = Endereco.id_End "
					+ "ORDER BY nome_Cli");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Cliente> list = new ArrayList<>();
			
			while(rs.next()) {
				Endereco end = instantiateEndereco(rs);
				Cliente obj = instantiateCliente(rs, end);
				
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
	
	private Cliente instantiateCliente(ResultSet rs, Endereco end) throws SQLException{
		Cliente obj = new Cliente();
		
		obj.setId_Cli(rs.getInt("id_Cli"));
		obj.setNome_Cli(rs.getString("nome_Cli"));
		obj.setCpf_Cli(rs.getString("cpf_Cli"));
		obj.setTel_Cli(rs.getInt("tel_Cli"));
		obj.setCel_Cli(rs.getInt("cel_Cli"));
		obj.setEmail_Cli(rs.getString("email_Cli"));
		obj.setId_End(rs.getInt("id_End"));

		obj.setEndereco(end);
		
		return obj;
	}


}
