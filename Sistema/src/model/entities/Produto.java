package model.entities;

import java.util.Objects;

public class Produto {
	
	private Integer id_Prod;
	private String nome_Prod;
	private String desc_Prod;
	private Double preco_Prod;//Preco_Cli
	private Double preco_Forn;
	private Integer qtd_Estocado;
	private Integer qtd_Min;
	
	private Integer qt_Prod;
	
	public Produto() {
	}

	

	public Produto(Integer id_Prod, String nome_Prod, String desc_Prod, Double preco_Prod, Double preco_Forn,
			Integer qtd_Estocado, Integer qtd_Min, Integer qt_Prod) {
		this.id_Prod = id_Prod;
		this.nome_Prod = nome_Prod;
		this.desc_Prod = desc_Prod;
		this.preco_Prod = preco_Prod;
		this.preco_Forn = preco_Forn;
		this.qtd_Estocado = qtd_Estocado;
		this.qtd_Min = qtd_Min;
		this.qt_Prod = qt_Prod;
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



	public Double getPreco_Forn() {
		return preco_Forn;
	}



	public void setPreco_Forn(Double preco_Forn) {
		this.preco_Forn = preco_Forn;
	}



	public Integer getQtd_Estocado() {
		return qtd_Estocado;
	}



	public void setQtd_Estocado(Integer qtd_Estocado) {
		this.qtd_Estocado = qtd_Estocado;
	}



	public Integer getQtd_Min() {
		return qtd_Min;
	}



	public void setQtd_Min(Integer qtd_Min) {
		this.qtd_Min = qtd_Min;
	}



	public Integer getQt_Prod() {
		return qt_Prod;
	}



	public void setQt_Prod(Integer qt_Prod) {
		this.qt_Prod = qt_Prod;
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
