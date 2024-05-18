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
import model.dao.FornecimentoDao;
import model.entities.Endereco;
import model.entities.Estoque;
import model.entities.Fornecedor;
import model.entities.Fornecimento;
import model.entities.LocalDeEstoque;
import model.entities.Produto;
import model.services.EnderecoService;
import model.services.LocalDeEstoqueServices;
import model.services.ProdutoServices;

public class FornecimentoDaoJDBC implements FornecimentoDao {

	private Connection conn = null;

	public FornecimentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Fornecimento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO " + "Fornecimento (dt_Forne, preco_Forne, id_Forn, id_Prod, id_Est) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDt_Forne().getTime()));
			st.setDouble(2, obj.getPreco_Forne());
			st.setInt(3, obj.getFornecedor().getId_Forn());
			st.setInt(4, obj.getProduto().getId_Prod());
			st.setInt(5, obj.getEstoque().getId_Est());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();

				while (rs.next()) {
					int id = rs.getInt(1);
					obj.setId_Forne(id);
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

	}

	@Override
	public void update(Fornecimento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Fornecimento  "
					+ "SET dt_Forne = ?, preco_Forne = ?, id_Forn = ?, id_Prod = ? " + "WHERE id_Forne = ?");

			st.setDate(1, new java.sql.Date(obj.getDt_Forne().getTime()));
			st.setDouble(2, obj.getPreco_Forne());
			st.setInt(3, obj.getFornecedor().getId_Forn());
			st.setInt(4, obj.getProduto().getId_Prod());

			st.setInt(5, obj.getId_Forne());

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
			st = conn.prepareStatement("DELETE FROM Fornecimento WHERE id_Forne = ? ");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Fornecimento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Fornecimento.*, Fornecedor.*, Produto.*, Estoque.* "
					+ "FROM Fornecimento "
					+ "INNER JOIN Estoque ON Fornecimento.id_Est = Estoque.id_Est "
					+ "INNER JOIN Produto ON Fornecimento.id_Prod = Produto.id_Prod "
					+ "INNER JOIN fornecedor ON Fornecimento.id_Forn = fornecedor.id_Forn "
					+ "WHERE Fornecimento.id_Forne = ?", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Estoque est = instantiateEstoque(rs);
				Fornecedor forn = instantiateFornecedor(rs);
				Produto prod = instantiateProduto(rs);
				Fornecimento obj = instantiateFornecimento(rs, forn, prod, est);
				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	@Override
	public List<Fornecimento> findByName(String num) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement(
					"SELECT Fornecimento.*, Fornecedor.*, Produto.* " 
					+ "FROM Fornecimento "
					+ "INNER JOIN Fornecimento ON Fornecimento.id_Forn = Fornecedor.id_Forn "
					+ "INNER JOIN Produto ON Fornecimento.id_Prod = Produto.id_Prod " 
					+ "WHERE id_Forne Like ? ");

			st.setString(1, num + "%");
			rs = st.executeQuery();

			List<Fornecimento> list = new ArrayList<>();

			while (rs.next()) {
				Estoque est = instantiateEstoque(rs);
				Fornecedor forn = instantiateFornecedor(rs);
				Produto prod = instantiateProduto(rs);
				Fornecimento obj = instantiateFornecimento(rs, forn, prod, est);

				list.add(obj);
			} // end while

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}// End findAll

	@Override
	public List<Fornecimento> findAll() {
		// PrepareStatement -> Utilizada quando uma instrucao sql sera executada vaira
		// vezes (Statement)
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// Cria a string sql eja define a conexao
			st = conn.prepareStatement(
					"SELECT Fornecimento.*, Fornecedor.*, Produto.* " 
					+ "FROM Fornecimento "
					+ "INNER JOIN Fornecimento ON Fornecimento.id_Forn = Fornecedor.id_Forn "
					+ "INNER JOIN Produto ON Fornecimento.id_Prod = Produto.id_Prod " );

			// Executa a query e salva o resultado no ResultSet
			rs = st.executeQuery();// Cria uma tabela, apaontando nenhum valor

			List<Fornecimento> list = new ArrayList<>();

			while (rs.next()) {
				Estoque est = instantiateEstoque(rs);
				Fornecedor forn = instantiateFornecedor(rs);
				Produto prod = instantiateProduto(rs);
				Fornecimento obj = instantiateFornecimento(rs, forn, prod, est);

				list.add(obj);
			} // end while

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}// End findAll
	
	

	private Estoque instantiateEstoque(ResultSet rs) throws SQLException {
		Estoque obj = new Estoque();

		obj.setId_Est(rs.getInt("id_Est"));
		obj.setQt_Prod_Est(rs.getInt("qtd_Prod_Est"));
		obj.setDt_Est(new java.util.Date(rs.getDate("dt_Est").getTime()));
		obj.setId_Local(rs.getInt("id_Local"));
		obj.setId_Prod(rs.getInt("id_Prod"));

		LocalDeEstoqueServices serviceLocal = new LocalDeEstoqueServices();
		ProdutoServices serviceProd = new ProdutoServices();
		
		LocalDeEstoque local = serviceLocal.findById(obj.getId_Local());
		Produto prod = serviceProd.findById(obj.getId_Prod());
		
		obj.setLocalDeEstoque(local);
		obj.setProduto(prod);

		return obj;

	}
	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
		Fornecedor obj = new Fornecedor();

		obj.setId_Forn(rs.getInt("id_Forn"));
		obj.setNome_Forn(rs.getString("nome_Forn"));
		obj.setCnpj_Forn(rs.getString("cnpj_Forn"));
		obj.setTel_Forn(rs.getInt("tel_Forn"));
		obj.setEmail_Forn(rs.getString("email_Forn"));
		obj.setId_End(rs.getInt("id_End"));

		EnderecoService service = new EnderecoService();
		
		Endereco end = service.findById(obj.getId_End());
		obj.setEndereco(end);

		return obj;

	}

	private Produto instantiateProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();

		obj.setId_Prod(rs.getInt("id_Prod"));
		obj.setNome_Prod(rs.getString("nome_Prod"));
		obj.setDesc_Prod(rs.getString("desc_Prod"));
		obj.setPreco_Prod(rs.getDouble("preco_Cli"));
		obj.setQtd_Estocado(rs.getInt("qtd_Estocado"));
		obj.setQtd_Min(rs.getInt("qtd_Min"));

		return obj;
	}

	private Fornecimento instantiateFornecimento(ResultSet rs, Fornecedor forn, Produto prod, Estoque est) throws SQLException {
		Fornecimento obj = new Fornecimento();

		obj.setId_Forne(rs.getInt("id_Forne"));
		obj.setDt_Forne(new java.util.Date(rs.getDate("dt_Forne").getTime()));
		obj.setPreco_Forne(rs.getDouble("preco_Forne"));
		obj.setId_Forn(rs.getInt("id_Forn"));
		obj.setId_Prod(rs.getInt("id_Prod"));
		obj.setId_Est(rs.getInt("id_Est"));

		obj.setFornecedor(forn);
		obj.setProduto(prod);
		obj.setEstoque(est);

		return obj;
	}

}
