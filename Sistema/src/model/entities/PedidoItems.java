package model.entities;

import java.util.Objects;

public class PedidoItems {

	private Integer id_PedIt;
	private Integer id_Ped;
	private Integer id_Prod;
	private Integer qt_PedIt;
	private Double preco_PedIt;
	
	private Produto produto;
	
	public PedidoItems() {
	}

	public PedidoItems(Integer id_PedIt, Integer id_Prod, Integer qt_PedIt, Double preco_PedIt, Produto produto) {
		super();
		this.id_PedIt = id_PedIt;
		this.id_Prod = id_Prod;
		this.qt_PedIt = qt_PedIt;
		this.preco_PedIt = preco_PedIt;
		this.produto = produto;
	}

	public Integer getId_PedIt() {
		return id_PedIt;
	}

	public void setId_PedIt(Integer id_PedIt) {
		this.id_PedIt = id_PedIt;
	}

	public Integer getId_Prod() {
		return id_Prod;
	}

	public void setId_Prod(Integer id_Prod) {
		this.id_Prod = id_Prod;
	}

	public Integer getQt_PedIt() {
		return qt_PedIt;
	}

	public void setQt_PedIt(Integer qt_PedIt) {
		this.qt_PedIt = qt_PedIt;
	}

	public Double getPreco_PedIt() {
		return preco_PedIt;
	}

	public void setPreco_PedIt(Double preco_PedIt) {
		this.preco_PedIt = preco_PedIt;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_PedIt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedidoItems other = (PedidoItems) obj;
		return Objects.equals(id_PedIt, other.id_PedIt);
	}
	
	public Double subTotal() {
		return qt_PedIt * preco_PedIt;
	}
	
}
