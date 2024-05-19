package model.entities;

import java.util.Date;

import model.entities.enums.TipoDePagamento;

public class Pagamentos {

	private Integer id_Pag;
	private Double preco_Pag;
	private Date dt_Pag;
	private TipoDePagamento tipo_Pag;
	private Integer nro_Ped;
	
	
	public Pagamentos(){
	}


	public Pagamentos(Integer id_Pag, Double preco_Pag, Date dt_Pag, TipoDePagamento tipo_Pag, Integer nro_Ped) {
		this.id_Pag = id_Pag;
		this.preco_Pag = preco_Pag;
		this.dt_Pag = dt_Pag;
		this.tipo_Pag = tipo_Pag;
		//this.nro_Ped = nro_Ped;
	}


	public Integer getId_Pag() {
		return id_Pag;
	}


	public void setId_Pag(Integer id_Pag) {
		this.id_Pag = id_Pag;
	}


	public Double getPreco_Pag() {
		return preco_Pag;
	}


	public void setPreco_Pag(Double preco_Pag) {
		this.preco_Pag = preco_Pag;
	}


	public Date getDt_Pag() {
		return dt_Pag;
	}


	public void setDt_Pag(Date dt_Pag) {
		this.dt_Pag = dt_Pag;
	}


	public TipoDePagamento getTipo_Pag() {
		return tipo_Pag;
	}


	public void setTipo_Pag(TipoDePagamento tipo_Pag) {
		this.tipo_Pag = tipo_Pag;
	}


	public Integer getNro_Ped() {
		return nro_Ped;
	}


	public void setNro_Ped(Integer nro_Ped) {
		this.nro_Ped = nro_Ped;
	}

	
	
}
