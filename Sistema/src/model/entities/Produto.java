package model.entities;

import java.util.Objects;

public class Produto {
	
	private Integer id_Prod;
	private String nome_Prod;
	private String desc_Prod;
	private Double preco_Prod;
	
	public Produto() {
	}

	public Produto(Integer id_Prod, String nome_Prod, String desc_Prod, Double preco_Prod) {
		super();
		this.id_Prod = id_Prod;
		this.nome_Prod = nome_Prod;
		this.desc_Prod = desc_Prod;
		this.preco_Prod = preco_Prod;
	}

	public Integer getId_Prod() {
		return id_Prod;
	}

	public void setId_Prod(Integer id_Prod) {
		this.id_Prod = id_Prod;
	}

	public String getNome_Prod() {
		return nome_Prod;
	}

	public void setNome_Prod(String nome_Prod) {
		this.nome_Prod = nome_Prod;
	}

	public String getDesc_Prod() {
		return desc_Prod;
	}

	public void setDesc_Prod(String desc_Prod) {
		this.desc_Prod = desc_Prod;
	}

	public Double getPreco_Prod() {
		return preco_Prod;
	}

	public void setPreco_Prod(Double preco_Prod) {
		this.preco_Prod = preco_Prod;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_Prod);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(id_Prod, other.id_Prod);
	}
	
	

}
