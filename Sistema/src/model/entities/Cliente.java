package model.entities;

import java.util.Objects;

public class Cliente {

	private Integer id_Cli;
	private String nome_Cli;
	private String cpf_Cli;
	private String tel_Cli;
	private String cel_Cli;
	private String email_Cli;
	
	private Integer id_End;
	private Endereco endereco;
	
	public Cliente() {
	}

	public Cliente(Integer id_Cli, String nome_Cli, String cpf_Cli, String tel_Cli, String cel_Cli, String email_Cli,
			Integer id_End, Endereco endereco) {
		this.id_Cli = id_Cli;
		this.nome_Cli = nome_Cli;
		this.cpf_Cli = cpf_Cli;
		this.tel_Cli = tel_Cli;
		this.cel_Cli = cel_Cli;
		this.email_Cli = email_Cli;
		this.id_End = id_End;
		this.endereco = endereco;
	}

	public Integer getId_Cli() {
		return id_Cli;
	}

	public void setId_Cli(Integer id_Cli) {
		this.id_Cli = id_Cli;
	}

	public String getNome_Cli() {
		return nome_Cli;
	}

	public void setNome_Cli(String nome_Cli) {
		this.nome_Cli = nome_Cli;
	}

	public String getCpf_Cli() {
		return cpf_Cli;
	}

	public void setCpf_Cli(String cpf_Cli) {
		this.cpf_Cli = cpf_Cli;
	}

	public String getTel_Cli() {
		return tel_Cli;
	}

	public void setTel_Cli(String tel_Cli) {
		this.tel_Cli = tel_Cli;
	}

	public String getCel_Cli() {
		return cel_Cli;
	}

	public void setCel_Cli(String cel_Cli) {
		this.cel_Cli = cel_Cli;
	}

	public String getEmail_Cli() {
		return email_Cli;
	}

	public void setEmail_Cli(String email_Cli) {
		this.email_Cli = email_Cli;
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
		return Objects.hash(id_Cli);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id_Cli, other.id_Cli);
	}

	
	
}
