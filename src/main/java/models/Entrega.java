package models;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Entrega {
    private String idRastreio;
    private Cliente cliente;
    private List<ItemEntrega> itens;
    private String status;
    
    public Entrega(String idRastreio, Cliente cliente){
        this.idRastreio = idRastreio;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = "Pendente";
    }

    public Entrega(){
        this.itens = new ArrayList<>();
        this.status = "Pendente";
    }
    
    public void addProd(Produto produto, int quantidade){
        for(ItemEntrega item : itens){
            if(item.getProduto().equals(produto)){
                item.somarQuantidade(quantidade);
                return;
            }
        }
        itens.add(new ItemEntrega(produto, quantidade));
    }
    public void updStatus(String novoStatus){
        this.status = novoStatus;
        System.out.println("Status de entrega: " + idRastreio + " | Atualizado para: "+ status);
    }

    public BigDecimal calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(ItemEntrega item : itens){
            total = total.add(item.getSubtotal());
        }
        return total;
    }
    public void exibirResumo(){
        System.out.println("\n===== Resumo da Entrega =====");
        System.out.println("Rastreio: "+ idRastreio + " | Status: "+ status);
        System.out.println("Endereco: "+ cliente.getEnd().getEndCompleto());
        System.out.println("Destinatario: "+ cliente.getName());
        System.out.println("Itens: ");
        for(ItemEntrega item : itens){
            System.out.printf(" - %dx %s: R$ %.2f%n",
                item.getQuantidade(), item.getProduto().getProdName(), item.getSubtotal());
        }
        System.out.printf("Total da compra: R$ %.2f%n", calcularTotal());
        System.out.println("===========================");
    }
    
    public String getIdRastreio(){ return idRastreio; }
    public Cliente getCliente(){ return cliente; }
    public List<ItemEntrega> getItens(){ return itens; }
    
    public void setIdRastreio(String idRastreio){ this.idRastreio = idRastreio; }
    public void setCliente(Cliente cliente){ this.cliente = cliente; }
    public String getStatus(){ return status; }
}