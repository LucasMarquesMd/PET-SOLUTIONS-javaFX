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
	private String end;
	private Integer end_num;
	private Integer telefone;
	private Integer celular;
	
	public Colaborador() {
	}

	public Colaborador(Integer idColab, String name, String email, String cnpj_cpf, String end, Integer end_num, Integer telefone, Integer celular) {
		this.idColab = idColab;
		this.name = name;
		this.email = email;
		this.cnpj_cpf = cnpj_cpf;
		this.end = end;
		this.end_num = end_num;
		this.telefone = telefone;
		this.celular = celular;
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

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getEnd_num() {
		return end_num;
	}

	public void setEnd_num(Integer end_num) {
		this.end_num = end_num;
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

	@Override
	public int hashCode() {
		return Objects.hash(cnpj_cpf, idColab);
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
		return Objects.equals(cnpj_cpf, other.cnpj_cpf) && Objects.equals(idColab, other.idColab);
	}
	
	
	
	

}
