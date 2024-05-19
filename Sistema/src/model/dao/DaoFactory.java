package model.dao;

import db.DB;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.ColaboradorDaoJDBC;
import model.dao.impl.EnderecoDaoJDBC;
import model.dao.impl.EstoqueDaoJDBC;
import model.dao.impl.FornecedorDaoJDBC;
import model.dao.impl.FornecimentoDaoJDBC;
import model.dao.impl.LocalDeEstoqueDaoJDBC;
import model.dao.impl.NotaEstoqueDaoJDBC;
import model.dao.impl.PagamentosDaoJDBC;
import model.dao.impl.PedidoItemsDaoJDBC;
import model.dao.impl.PedidosDaoJDBC;
import model.dao.impl.ProdutoDaoJDBC;

//Classe responsavel por instanciar a implentacao dos Dao
public class DaoFactory {

	public static ColaboradorDao createColaboradorDao() {
		return new ColaboradorDaoJDBC(DB.getConnection());
	}
	
	public static EnderecoDao createEnderecoDao() {
		return new EnderecoDaoJDBC(DB.getConnection());
	}
	
	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
	public static LocalDeEstoqueDao createLocalDeEstoqueDao() {
		return new LocalDeEstoqueDaoJDBC(DB.getConnection());
	}
	
	public static PedidoItemsDao createPedidoItemsDao() {
		return new PedidoItemsDaoJDBC(DB.getConnection());
	}
	
	public static PedidosDao createPedidosDao() {
		return new PedidosDaoJDBC(DB.getConnection());
	}
	
	public static EstoqueDao createEstoqueDao() {
		return new EstoqueDaoJDBC(DB.getConnection());
	}
	
	public static NotaEstoqueDao createNotaEstoqueDao() {
		return new NotaEstoqueDaoJDBC(DB.getConnection());
	}
	
	public static FornecimentoDao createFornecimentoDao() {
		return new FornecimentoDaoJDBC(DB.getConnection());
	}
	
	public static PagamentosDao createPagamentosDao() {
		return new PagamentosDaoJDBC(DB.getConnection());
	}
}
