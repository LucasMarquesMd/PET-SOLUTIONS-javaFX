package model.entities;

import java.util.Objects;

public class Fornecedor {
	
	private Integer id_Forn;
	private String nome_Forn;
	private String cnpj_Forn;
	private Integer tel_Forn;
	private String email_Forn;
	private Integer id_End;
	
	private Endereco endereco;
	
	public Fornecedor() {
	}

	public Fornecedor(Integer id_Forn, String nome_Forn, String cnpj_Forn, Integer tel_Forn, String email_Forn,
			Integer id_End, Endereco endereco) {
		super();
		this.id_Forn = id_Forn;
		this.nome_Forn = nome_Forn;
		this.cnpj_Forn = cnpj_Forn;
		this.tel_Forn = tel_Forn;
		this.email_Forn = email_Forn;
		this.id_End = id_End;
		this.endereco = endereco;
	}

	public Integer getId_Forn() {
		return id_Forn;
	}

	public void setId_Forn(Integer id_Forn) {
		this.id_Forn = id_Forn;
	}

	public String getNome_Forn() {
		return nome_Forn;
	}

	public void setNome_Forn(String nome_Forn) {
		this.nome_Forn = nome_Forn;
	}

	public String getCnpj_Forn() {
		return cnpj_Forn;
	}

	public void setCnpj_Forn(String cnpj_Forn) {
		this.cnpj_Forn = cnpj_Forn;
	}

	public Integer getTel_Forn() {
		return tel_Forn;
	}

	public void setTel_Forn(Integer tel_Forn) {
		this.tel_Forn = tel_Forn;
	}

	public String getEmail_Forn() {
		return email_Forn;
	}

	public void setEmail_Forn(String email_Forn) {
		this.email_Forn = email_Forn;
	}

	public Integer getId_End() {
		return id_End;
	}

	public void setId_End(Integer id_End) {
		this.id_End = id_End;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_Forn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fornecedor other = (Fornecedor) obj;
		return Objects.equals(id_Forn, other.id_Forn);
	}

	
	
	
	

}
