package model.entities;

import java.util.Date;
import java.util.List;

public class Pedidos {

	private Integer id_Ped;
	private Date dt_Ped;
	private Double preco_Ped;
	private Integer id_PedIt;
	private Integer id_Col;
	private Integer id_Pag;
	
	private List<PedidoItems> pedidoItems;
	private Colaborador colaborador;
	//private Pegamento pagamento;
	
	public Pedidos() {
	}
	public Pedidos(Integer id_Ped, Date dt_Ped, Double preco_Ped, Integer id_PedIt, Integer id_Col, Integer id_Pag,
			Colaborador colaborador) {
		super();
		this.id_Ped = id_Ped;
		this.dt_Ped = dt_Ped;
		this.preco_Ped = preco_Ped;
		this.id_PedIt = id_PedIt;
		this.id_Col = id_Col;
		this.id_Pag = id_Pag;
		this.colaborador = colaborador;
	}
	public Integer getId_Ped() {
		return id_Ped;
	}
	public void setId_Ped(Integer id_Ped) {
		this.id_Ped = id_Ped;
	}
	public Date getDt_Ped() {
		return dt_Ped;
	}
	public void setDt_Ped(Date dt_Ped) {
		this.dt_Ped = dt_Ped;
	}
	public Double getPreco_Ped() {
		return preco_Ped;
	}
	public void setPreco_Ped(Double preco_Ped) {
		this.preco_Ped = preco_Ped;
	}
	public Integer getId_PedIt() {
		return id_PedIt;
	}
	public void setId_PedIt(Integer id_PedIt) {
		this.id_PedIt = id_PedIt;
	}
	public Integer getId_Col() {
		return id_Col;
	}
	public void setId_Col(Integer id_Col) {
		this.id_Col = id_Col;
	}
	public Integer getId_Pag() {
		return id_Pag;
	}
	public void setId_Pag(Integer id_Pag) {
		this.id_Pag = id_Pag;
	}
	public Colaborador getColaborador() {
		return colaborador;
	}
	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public void setPedidoItems(List<PedidoItems> pedidoItems) {
		this.pedidoItems = pedidoItems;
	}
	
	public List<PedidoItems> getPedidoItems(){
		return pedidoItems;
	}
	
	//Adiciona um item na lista de items
	public void addItem(PedidoItems item) {
		pedidoItems.add(item);
	}
	
	public void removeItem(PedidoItems item) {
		pedidoItems.remove(item);
	}
	
	//Retorna o total do pedido
	public Double total() {
		double sum = 0;
		for(PedidoItems item : pedidoItems) {
			sum += item.subTotal();
		}
		return sum;
	}
	
}
