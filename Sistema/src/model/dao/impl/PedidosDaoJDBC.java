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
import model.dao.PedidosDao;
import model.entities.Pedidos;
import model.entities.enums.PedidoStatus;
import model.entities.Colaborador;

public class PedidosDaoJDBC implements PedidosDao{

	private Connection conn = null;
	
	public PedidosDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Pedidos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Pedido (dt_Ped, preco_Ped, id_Col, status_Ped) "
					+ "VALUES "
					+ "(?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getDt_Ped().getTime()));
			st.setDouble(2, obj.getPreco_Ped());
			st.setInt(3, obj.getId_Col());
			st.setString(4, obj.getStatus_Ped().toString());

			
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);//Pega o valor do primeiro campo
					//obj.setId_End(id);
					obj.setId_Ped(id);
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
	public void update(Pedidos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Pedido  "
					+ "SET dt_Ped = ?, preco_Ped = ?, id_Col = ?, status_Ped = ? "
					+ "WHERE id_Ped = ?");
			
			st.setDate(1, new java.sql.Date(obj.getDt_Ped().getTime()));
			st.setDouble(2, obj.getPreco_Ped());
			st.setInt(3, obj.getId_Col());
			st.setString(4, obj.getStatus_Ped().toString());
			st.setInt(5, obj.getId_Ped());

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
					"DELETE FROM Pedido WHERE id_Col = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Pedidos findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Pedido.*, Colaborador.* "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "WHERE id_Ped = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	
	@Override
	public List<Pedidos> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "ORDER BY id_Ped");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Pedidos> list = new ArrayList<>();
			
			while(rs.next()) {
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col);
				
				list.add(obj);
			}//end while
			
			return list;
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}//End findAll
	
	private Colaborador instantiateColaborador(ResultSet rs) throws SQLException{
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
		
		//obj.setEndereco(end);

		return obj;
	}
	
	private Pedidos instantiatePedidos(ResultSet rs, Colaborador col) throws SQLException{
		Pedidos obj = new Pedidos();
		
		obj.setId_Ped(rs.getInt("id_Ped"));
		obj.setDt_Ped(new java.util.Date(rs.getDate("dt_Ped").getTime()));
		obj.setPreco_Ped(rs.getDouble("preco_Ped"));
		obj.setId_Col(rs.getInt("id_Col"));
		obj.setStatus_Ped(PedidoStatus.valueOf(rs.getString("status_Ped")));
		
		obj.setColaborador(col);
		
		return obj;
	}

	@Override
	public List<Pedidos> consultPed(Integer numero) {
		// TODO Auto-generated method stub
		return null;
	}

}
