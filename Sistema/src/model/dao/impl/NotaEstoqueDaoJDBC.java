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
import model.dao.NotaEstoqueDao;
import model.entities.Fornecimento;
import model.entities.NotaEstoque;
import model.entities.enums.LocalStatus;
import model.services.FornecedorServices;
import model.services.ProdutoServices;

public class NotaEstoqueDaoJDBC implements NotaEstoqueDao{

	private Connection conn = null;
	
	public NotaEstoqueDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(NotaEstoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO "
					+ "Nota_Estoque (valor, id_Forne, nro_Nota) "
					+ "VALUES "
					+ "(?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setDouble(1, obj.getValor());
			st.setInt(2, obj.getFornecimento().getId_Forne());
			st.setInt(3, obj.getNro_Nota());

			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				while(rs.next()) {
					int id = rs.getInt(1);
					obj.setId_Nota(id);
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
	public void update(NotaEstoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Nota_Estoque  "
					+ "SET valor = ?, id_Forne = ?, nro_Nota = ? "
					+ "WHERE id_Nota = ?");
			
			st.setDouble(1, obj.getValor());
			st.setInt(2, obj.getId_Forne());
			st.setInt(3, obj.getNro_Nota());
			
			st.setInt(4, obj.getId_Nota());

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
					"DELETE FROM Nota_Estoque WHERE id_Nota = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public NotaEstoque findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Nota_Estoque.*, Fornecimento.* "
					+ "FROM Nota_Estoque "
					+ "INNER JOIN Fornecimento "
					+ "ON Nota_Estoque.id_Fornee = Fornecimento.id_Fornee "
					+ "WHERE id_Nota = ? ");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Fornecimento forn = instantiateFornecimento(rs);
				NotaEstoque obj = instantiateNotaEstoque(rs, forn);
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	@Override
	public List<NotaEstoque> findByNum(String num) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement(
					"SELECT Nota_Estoque.*, Fornecimento.* "
					+ "FROM Nota_Estoque "
					+ "INNER JOIN Fornecimento "
					+ "ON Nota_Estoque.id_Forne = Fornecimento.id_Forne "
					+ "WHERE nro_Nota LIKE ?");
			
			st.setString(1, num + "%");
			rs = st.executeQuery();
			
			List<NotaEstoque> list = new ArrayList<>();
			
			
			while(rs.next()) {
				Fornecimento forne = instantiateFornecimento(rs);
				NotaEstoque obj = instantiateNotaEstoque(rs, forne);
				
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
	public List<NotaEstoque> findAll() {
		//PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Cria a string sql eja define a conexao
			st = conn.prepareStatement(
					"SELECT Nota_Estoque.*, Fornecimento.*  "
					+ "FROM Nota_Estoque "
					+ "INNER JOIN Fornecimento "
					+ "ON Nota_Estoque.id_Forne = Fornecimento.id_Forne ");
			
			//Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();//Cria uma tabela, apaontando nenhum valor
			
			List<NotaEstoque> list = new ArrayList<>();
			
			while(rs.next()) {
				Fornecimento forne = instantiateFornecimento(rs);
				NotaEstoque obj = instantiateNotaEstoque(rs, forne);
				
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
	
	
	
	private Fornecimento instantiateFornecimento(ResultSet rs) throws SQLException{
		Fornecimento obj = new Fornecimento();
		
		obj.setId_Forn(rs.getInt("id_Forne"));
		obj.setDt_Forne(new java.util.Date(rs.getDate("dt_Forne").getTime()));
		obj.setPreco_Forne(rs.getDouble("preco_Forne"));
		obj.setId_Forn(rs.getInt("id_Forne"));
		obj.setId_Prod(rs.getInt("id_Prod"));

		ProdutoServices serviceProd = new ProdutoServices();
		obj.setProduto(serviceProd.findById(rs.getInt("id_Prod")));
		
		FornecedorServices serviceForn = new FornecedorServices();
		obj.setFornecedor(serviceForn.findById(rs.getInt("id_Forne")));
		
		return obj;
	}
	
	private NotaEstoque instantiateNotaEstoque(ResultSet rs, Fornecimento forne) throws SQLException{
		NotaEstoque obj = new NotaEstoque();
		
		obj.setId_Nota(rs.getInt("id_Nota"));
		obj.setValor(rs.getDouble("valor"));
		obj.setNro_Nota(rs.getInt("nro_Nota"));
		obj.setId_Forne(rs.getInt("id_Forne"));
		
		obj.setFornecimento(forne);
		
		return obj;
	}


}
