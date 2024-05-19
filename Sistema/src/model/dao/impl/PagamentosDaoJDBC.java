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
import model.dao.PagamentosDao;
import model.entities.Colaborador;
import model.entities.Pagamentos;
import model.entities.Pedidos;
import model.entities.enums.PedidoStatus;
import model.entities.enums.TipoDePagamento;
import model.services.ColaboradorServices;

public class PagamentosDaoJDBC implements PagamentosDao{

	private Connection conn = null;
	
	public PagamentosDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Pagamentos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Pagamento (dt_Pag, preco_Pag, tipo_Pag) "
					+ "VALUES "
					+ "(?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getDt_Pag().getTime()));
			st.setDouble(2, obj.getPreco_Pag());
			st.setString(3, obj.getTipo_Pag().toString());

			
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//Recupera a chave primaria do objeto inserido
				
				while(rs.next()) {
					int id = rs.getInt(1);
					obj.setId_Pag(id);
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
	public void update(Pagamentos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Pagamento  "
					+ "SET dt_Pag = ?, preco_Pag = ?, tipo_Pag = ?, nro_Ped = ? "
					+ "WHERE id_Pag = ?");
			
			st.setDate(1, new java.sql.Date(obj.getDt_Pag().getTime()));
			st.setDouble(2, obj.getPreco_Pag());
			st.setString(3, obj.getTipo_Pag().toString());
			if(obj.getNro_Ped() == null) {
				st.setNull(4, java.sql.Types.INTEGER);
			}else {
				st.setInt(4, obj.getNro_Ped());
			}
			
			st.setInt(5, obj.getId_Pag());

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
					"DELETE FROM Pagamento WHERE id_Pag = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Pagamentos findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Pagamento.* "
					+ "FROM Pagamento "
					+ "WHERE id_Pag = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Pagamentos obj = instantiatePagamentos(rs);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	
	
	
	@Override
	public List<Pagamentos> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement(
					"SELECT *  "
					+ "FROM Pagamento "
					+ "ORDER BY id_Pag");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<Pagamentos> list = new ArrayList<>();
			
			while(rs.next()) {
				Pagamentos obj = instantiatePagamentos(rs);
				
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
	public List<Pagamentos> consultPag(String numero) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement(
					"SELECT *  "
					+ "FROM Pagamento "
					+ "WHERE id_Pag LIKE ? ");
		
			st.setString(1, numero + "%");
			rs = st.executeQuery();
			
			List<Pagamentos> list = new ArrayList<>();
			
			while(rs.next()) {
				Pagamentos obj = instantiatePagamentos(rs);
				
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
	
//	private Pedidos instantiatePedidos(ResultSet rs) throws SQLException {
//		Pedidos obj = new Pedidos();
//		
//		obj.setId_Ped(rs.getInt("id_Ped"));
//		obj.setDt_Ped(new java.util.Date(rs.getDate("dt_Ped").getTime()));
//		obj.setPreco_Ped(rs.getDouble("preco_Ped"));
//		obj.setId_Col(rs.getInt("id_Col"));
//		obj.setStatus_Ped(PedidoStatus.valueOf(rs.getString("status_Ped")));
//		
//		ColaboradorServices service = new ColaboradorServices();
//		Colaborador col = service.findById(obj.getId_Col());
//		
//		obj.setColaborador(col);
//		
//		return obj;
//	}
	
	private Pagamentos instantiatePagamentos(ResultSet rs) throws SQLException{
		Pagamentos obj = new Pagamentos();
		
		obj.setId_Pag(rs.getInt("id_Pag"));
		obj.setPreco_Pag(rs.getDouble("preco_Pag"));
		obj.setDt_Pag(new java.util.Date(rs.getDate("dt_Pag").getTime()));
		obj.setTipo_Pag(TipoDePagamento.valueOf(rs.getString("tipo_Pag")));
		obj.setNro_Ped(rs.getInt("nro_Ped"));
		
		return obj;
	}

}
