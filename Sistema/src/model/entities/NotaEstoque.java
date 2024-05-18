package model.entities;

public class NotaEstoque {

	private Integer id_Nota;
	private Double valor;
	private Integer nro_Nota;
	private Integer id_Forne;
	
	
	private Fornecimento fornecimento;
	
	public NotaEstoque() {
	}

	public NotaEstoque(Integer id_Nota, Double valor, Integer nro_Nota, Integer id_Forne, Fornecimento fornecimento) {
		this.id_Nota = id_Nota;
		this.valor = valor;
		this.nro_Nota = nro_Nota;
		this.id_Forne = id_Forne;
		this.fornecimento = fornecimento;
	}

	public Integer getId_Nota() {
		return id_Nota;
	}

	public void setId_Nota(Integer id_Nota) {
		this.id_Nota = id_Nota;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getNro_Nota() {
		return nro_Nota;
	}

	public void setNro_Nota(Integer nro_Nota) {
		this.nro_Nota = nro_Nota;
	}

	public Integer getId_Forne() {
		return id_Forne;
	}

	public void setId_Forne(Integer id_Forne) {
		this.id_Forne = id_Forne;
	}

	public Fornecimento getFornecimento() {
		return fornecimento;
	}

	public void setFornecimento(Fornecimento fornecimento) {
		this.fornecimento = fornecimento;
	}

		
}
