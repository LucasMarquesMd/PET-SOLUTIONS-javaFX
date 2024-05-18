package model.entities;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Estoque {

	private Integer id_Est;
	private Integer qtd_Prod_Est;
	private Date dt_Est;
	private Integer id_Local;
	private Integer id_Prod;
	
	private LocalDeEstoque localDeEstoque;
	private Produto produto;
	
	public Estoque() {
	}

	public Estoque(Integer id_Est, Integer qtd_Prod_Est, Date dt_Est, Integer id_Local, Integer id_Prod,
			LocalDeEstoque localDeEstoque, Produto produto) {
		this.id_Est = id_Est;
		this.qtd_Prod_Est = qtd_Prod_Est;
		this.dt_Est = dt_Est;
		this.id_Local = id_Local;
		this.id_Prod = id_Prod;
		this.localDeEstoque = localDeEstoque;
		this.produto = produto;
	}

	public Integer getId_Est() {
		return id_Est;
	}

	public void setId_Est(Integer id_Est) {
		this.id_Est = id_Est;
	}

	public Integer getQt_Prod_Est() {
		return qtd_Prod_Est;
	}

	public void setQt_Prod_Est(Integer qtd_Prod_Est) {
		this.qtd_Prod_Est = qtd_Prod_Est;
	}

	public Date getDt_Est() {
		return dt_Est;
	}

	public void setDt_Est(Date dt_Est) {
		this.dt_Est = dt_Est;
	}

	public Integer getId_Local() {
		return id_Local;
	}

	public void setId_Local(Integer id_Local) {
		this.id_Local = id_Local;
	}

	public Integer getId_Prod() {
		return id_Prod;
	}

	public void setId_Prod(Integer id_Prod) {
		this.id_Prod = id_Prod;
	}

	public LocalDeEstoque getLocalDeEstoque() {
		return localDeEstoque;
	}

	public void setLocalDeEstoque(LocalDeEstoque localDeEstoque) {
		this.localDeEstoque = localDeEstoque;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_Est);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estoque other = (Estoque) obj;
		return Objects.equals(id_Est, other.id_Est);
	}
	
	
	
}
 