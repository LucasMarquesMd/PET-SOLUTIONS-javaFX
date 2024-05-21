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
import model.entities.Colaborador;
import model.entities.Pagamentos;
import model.entities.Pedidos;
import model.entities.enums.NivelDeAcesso;
import model.entities.enums.PedidoStatus;
import model.entities.enums.TipoDePagamento;

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
					+ "Pedido (dt_Ped, preco_Ped, id_Col, status_Ped, id_Pag) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getDt_Ped().getTime()));
			st.setDouble(2, obj.getPreco_Ped());
			st.setInt(3, obj.getId_Col());
			st.setString(4, obj.getStatus_Ped().toString());
			st.setInt(5, obj.getId_Pag());

			
			
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
					"DELETE FROM Pedido WHERE id_Ped = ? ");
			
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
					"SELECT Pedido.*, Colaborador.*, Pagamento.* "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "INNER JOIN Pagamento "
					+ "ON Pedido.id_Pag = Pagamento.id_Pag"
					+ "WHERE id_Ped = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Pagamentos pag = instantiatePagamentos(rs);
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col, pag);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	@Override
	public Pedidos findByPag(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Pedido.*, Colaborador.*, Pagamento.* "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "INNER JOIN Pagamento "
					+ "ON Pedido.id_Pag = Pagamento.id_Pag"
					+ "WHERE Pedido.id_Pag = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Pagamentos pag = instantiatePagamentos(rs);
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col, pag);
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
			st = conn.prepareStatement(
					"SELECT Pedido.*, Colaborador.*, Pagamento.*  "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "INNER JOIN Pagamento "
					+ "ON Pedido.id_Pag = Pagamento.id_Pag "
					+ "ORDER BY id_Ped");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Pedidos> list = new ArrayList<>();
			
			while(rs.next()) {
				Pagamentos pag = instantiatePagamentos(rs);
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col, pag);
				
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
	
	private Pagamentos instantiatePagamentos(ResultSet rs) throws SQLException{
		Pagamentos obj = new Pagamentos();
		
		obj.setId_Pag(rs.getInt("id_Pag"));
		obj.setDt_Pag(new java.util.Date(rs.getDate("dt_Pag").getTime()));
		obj.setPreco_Pag(rs.getDouble("preco_Pag"));
		obj.setTipo_Pag(TipoDePagamento.valueOf(rs.getString("tipo_Pag")));
		
		//obj.setEndereco(end);

		return obj;
	}
	
	
	
	private Colaborador instantiateColaborador(ResultSet rs) throws SQLException{
		Colaborador obj = new Colaborador();
		
		obj.setIdColab(rs.getInt("id_Col"));
		obj.setName(rs.getString("nome_Col"));
		obj.setCnpj_cpf(rs.getString("cpf_Col"));
		obj.setTelefone(rs.getString("tel_Col"));
		obj.setCelular(rs.getString("cel_Col"));
		obj.setEmail(rs.getString("email_Col"));
		obj.setId_End(rs.getInt("id_End"));
		obj.setUser_Col(rs.getString("user_Col"));
		obj.setUser_Senha(rs.getString("user_Senha"));
		obj.setLevel_Access(NivelDeAcesso.valueOf(rs.getString("level_Access")));
		
		//obj.setEndereco(end);

		return obj;
	}
	
	private Pedidos instantiatePedidos(ResultSet rs, Colaborador col, Pagamentos pag) throws SQLException{
		Pedidos obj = new Pedidos();
		
		obj.setId_Ped(rs.getInt("id_Ped"));
		obj.setDt_Ped(new java.util.Date(rs.getDate("dt_Ped").getTime()));
		obj.setPreco_Ped(rs.getDouble("preco_Ped"));
		obj.setId_Col(rs.getInt("id_Col"));
		obj.setStatus_Ped(PedidoStatus.valueOf(rs.getString("status_Ped")));
		obj.setId_Pag(pag.getId_Pag());
		
		obj.setColaborador(col);
		obj.setPagamento(pag);
		
		return obj;
	}

	@Override
	public List<Pedidos> consultPed(String numero) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement("SELECT *  "
					+ "FROM Pedido "
					+ "INNER JOIN Colaborador "
					+ "ON Pedido.id_Col = Colaborador.id_Col "
					+ "INNER JOIN Pagamento "
					+ "ON Pedido.id_Pag = Pagamento.id_Pag "
					+ "WHERE id_Ped LIKE ? ");
		
			st.setString(1, numero + "%");
			rs = st.executeQuery();
			
			List<Pedidos> list = new ArrayList<>();
			
			while(rs.next()) {
				Pagamentos pag = instantiatePagamentos(rs);
				Colaborador col = instantiateColaborador(rs);
				Pedidos obj = instantiatePedidos(rs, col, pag);
				
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
	}

}
