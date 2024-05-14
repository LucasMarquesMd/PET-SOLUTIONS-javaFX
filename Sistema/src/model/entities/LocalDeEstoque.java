package model.entities;

import java.util.Date;
import java.util.Objects;

import model.entities.enums.LocalStatus;

public class LocalDeEstoque {
	
	private Integer id_Local;
	private String nome_Local;
	private String desc_Local;
	private LocalStatus sit_Local;
	
	public LocalDeEstoque() {
	}

	public LocalDeEstoque(Integer id_Local, String nome_Local, String desc_Local, LocalStatus sit_Local) {
		super();
		this.id_Local = id_Local;
		this.nome_Local = nome_Local;
		this.desc_Local = desc_Local;
		this.sit_Local = sit_Local;
	}

	public Integer getId_Local() {
		return id_Local;
	}

	public void setId_Local(Integer id_Local) {
		this.id_Local = id_Local;
	}

	public String getNome_Local() {
		return nome_Local;
	}

	public void setNome_Local(String nome_Local) {
		this.nome_Local = nome_Local;
	}

	public String getDesc_Local() {
		return desc_Local;
	}

	public void setDesc_Local(String desc_Local) {
		this.desc_Local = desc_Local;
	}

	public LocalStatus getSit_Local() {
		return sit_Local;
	}

	public void setSit_Local(LocalStatus sit_Local) {
		this.sit_Local = sit_Local;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_Local);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalDeEstoque other = (LocalDeEstoque) obj;
		return Objects.equals(id_Local, other.id_Local);
	}

	
}
