package model.entities;

import java.io.Serializable;
import java.util.Objects;

import org.w3c.dom.ls.LSSerializer;

public class Colaborador implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer idColab;
	private String name;
	private String email;
	private String cnpj_cpf;
	private Integer telefone;
	private Integer celular;
	private String user_Col;
	private String user_Senha;
	
	private Integer id_End;
	private Endereco endereco;
	
	
	public Colaborador() {
	}


	public Colaborador(Integer idColab, String name, String email, String cnpj_cpf, Integer telefone, Integer celular,
			String user_Col, String user_Senha, Integer id_End, Endereco endereco) {
		this.idColab = idColab;
		this.name = name;
		this.email = email;
		this.cnpj_cpf = cnpj_cpf;
		this.telefone = telefone;
		this.celular = celular;
		this.user_Col = user_Col;
		this.user_Senha = user_Senha;
		this.id_End = id_End;
		this.endereco = endereco;
	}


	public Integer getIdColab() {
		return idColab;
	}


	public void setIdColab(Integer idColab) {
		this.idColab = idColab;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCnpj_cpf() {
		return cnpj_cpf;
	}


	public void setCnpj_cpf(String cnpj_cpf) {
		this.cnpj_cpf = cnpj_cpf;
	}


	public Integer getTelefone() {
		return telefone;
	}


	public void setTelefone(Integer telefone) {
		this.telefone = telefone;
	}


	public Integer getCelular() {
		return celular;
	}


	public void setCelular(Integer celular) {
		this.celular = celular;
	}


	public String getUser_Col() {
		return user_Col;
	}


	public void setUser_Col(String user_Col) {
		this.user_Col = user_Col;
	}


	public String getUser_Senha() {
		return user_Senha;
	}


	public void setUser_Senha(String user_Senha) {
		this.user_Senha = user_Senha;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public int hashCode() {
		return Objects.hash(celular, cnpj_cpf, email, endereco, idColab, id_End, name, telefone, user_Col, user_Senha);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colaborador other = (Colaborador) obj;
		return Objects.equals(celular, other.celular) && Objects.equals(cnpj_cpf, other.cnpj_cpf)
				&& Objects.equals(email, other.email) && Objects.equals(endereco, other.endereco)
				&& Objects.equals(idColab, other.idColab) && Objects.equals(id_End, other.id_End)
				&& Objects.equals(name, other.name) && Objects.equals(telefone, other.telefone)
				&& Objects.equals(user_Col, other.user_Col) && Objects.equals(user_Senha, other.user_Senha);
	}

	
	
}
