package model.entities;

import java.util.Date;

public class Fornecimento {

	private Integer id_Forne;
	private Date dt_Forne;
	private Double preco_Forne;
	
	private Integer id_Forn;
	private Integer id_Prod;
	private Integer id_Est;
	
	private Fornecedor fornecedor;
	private Produto produto;
	private Estoque estoque;
	
	public Fornecimento() {
	}

	public Fornecimento(Integer id_Forne, Date dt_Forne, Double preco_Forne, Integer id_Forn, Integer id_Prod,
			Integer id_Est, Fornecedor fornecedor, Produto produto, Estoque estoque) {
		this.id_Forne = id_Forne;
		this.dt_Forne = dt_Forne;
		this.preco_Forne = preco_Forne;
		this.id_Forn = id_Forn;
		this.id_Prod = id_Prod;
		this.id_Est = id_Est;
		this.fornecedor = fornecedor;
		this.produto = produto;
		this.estoque = estoque;
	}

	public Integer getId_Forne() {
		return id_Forne;
	}

	public void setId_Forne(Integer id_Forne) {
		this.id_Forne = id_Forne;
	}

	public Date getDt_Forne() {
		return dt_Forne;
	}

	public void setDt_Forne(Date dt_Forne) {
		this.dt_Forne = dt_Forne;
	}

	public Double getPreco_Forne() {
		return preco_Forne;
	}

	public void setPreco_Forne(Double preco_Forne) {
		this.preco_Forne = preco_Forne;
	}

	public Integer getId_Forn() {
		return id_Forn;
	}

	public void setId_Forn(Integer id_Forn) {
		this.id_Forn = id_Forn;
	}

	public Integer getId_Prod() {
		return id_Prod;
	}

	public void setId_Prod(Integer id_Prod) {
		this.id_Prod = id_Prod;
	}

	public Integer getId_Est() {
		return id_Est;
	}

	public void setId_Est(Integer id_Est) {
		this.id_Est = id_Est;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}
	
	
	
}
