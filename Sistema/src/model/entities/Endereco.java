package model.entities;

import java.util.Objects;

public class Endereco {

	private Integer id_End;
	private String cep_End;
	private Integer num_End;
	private String rua_End;
	private String bairro_End;
	private String cidade_End;
	
	public Endereco() {
	}

	public Endereco(Integer id_End, String cep_End, Integer num_End, String rua_End, String bairro_End, String cidade_End) {
		this.id_End = id_End;
		this.cep_End = cep_End;
		this.num_End = num_End;
		this.rua_End = rua_End;
		this.bairro_End = bairro_End;
		this.cidade_End = cidade_End;
	}

	public Integer getId_End() {
		return id_End;
	}

	public void setId_End(Integer id_End) {
		this.id_End = id_End;
	}

	public String getCep_End() {
		return cep_End;
	}

	public void setCep_End(String cep_End) {
		this.cep_End = cep_End;
	}

	public Integer getNum_End() {
		return num_End;
	}

	public void setNum_End(Integer num_End) {
		this.num_End = num_End;
	}

	public String getRua_End() {
		return rua_End;
	}

	public void setRua_End(String rua_End) {
		this.rua_End = rua_End;
	}

	public String getBairro_End() {
		return bairro_End;
	}

	public void setBairro_End(String bairro_End) {
		this.bairro_End = bairro_End;
	}

	public String getCidade_End() {
		return cidade_End;
	}

	public void setCidade_End(String cidade_End) {
		this.cidade_End = cidade_End;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_End);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		return Objects.equals(id_End, other.id_End);
	}

	
	
	
	
	
}
