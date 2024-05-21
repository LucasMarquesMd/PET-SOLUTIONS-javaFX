package model.entities;

import java.util.Objects;

import model.entities.enums.NivelDeAcesso;

public class Colaborador{
	
	private Integer idColab;
	private String name;
	private String email;
	private String cnpj_cpf;
	private String telefone;
	private String celular;
	private String user_Col;
	private String user_Senha;
	private NivelDeAcesso level_Access;
	
	private Integer id_End;
	private Endereco endereco;
	
	
	public Colaborador() {
	}


	public Colaborador(Integer idColab, String name, String email, String cnpj_cpf, String telefone, String celular,
			String user_Col, String user_Senha, NivelDeAcesso level_Access, Integer id_End) {
		super();
		this.idColab = idColab;
		this.name = name;
		this.email = email;
		this.cnpj_cpf = cnpj_cpf;
		this.telefone = telefone;
		this.celular = celular;
		this.user_Col = user_Col;
		this.user_Senha = user_Senha;
		this.level_Access = level_Access;
		this.id_End = id_End;
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


	public String getTelefone() {
		return telefone;
	}


	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}


	public String getCelular() {
		return celular;
	}


	public void setCelular(String celular) {
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


	public NivelDeAcesso getLevel_Access() {
		return level_Access;
	}


	public void setLevel_Access(NivelDeAcesso level) {
		this.level_Access = level;
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
		return Objects.hash(idColab);
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
		return Objects.equals(idColab, other.idColab);
	}

	
	
	
}
