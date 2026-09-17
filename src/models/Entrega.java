package src.models;

import java.util.ArrayList;
import java.util.List;

public class Entrega {
    private String idRastreio;
    private Cliente cliente;
    private List<Produto>produtos;
    private String status;
    
    public Entrega(String idRastreio, Cliente cliente){
        this.idRastreio = idRastreio;
        this.cliente = cliente;
        this.produtos = new ArrayList<>();
        this.status = "Pendente";
    }

    public Entrega(){
        this.produtos = new ArrayList<>();
        this.status = "Pendente";
    }
    
    public void addProd(Produto produto){
        produtos.add(produto);
    }
    public void updStatus(String novoStatus){
        this.status = novoStatus;
        System.out.println("Status de entrega: " + idRastreio + " | Atualizado para: "+ status);
    }
    public double calcularTotal(){
        double total = 0;
        for(Produto p : produtos){ 
            total += p.getPrice();
        }
        return total;
    }
    public void exibirResumo(){
        System.out.println("\n===== Resumo da Entrega =====");
        System.out.println("Rastreio: "+ idRastreio + " | Status: "+ status);
        System.out.println("Endereco: "+ cliente.getEnd().getEndCompleto());
        System.out.println("Destinatario: "+ cliente.getName());
        System.out.println("Itens: ");
        for(Produto p : produtos){
            System.out.printf(" - " + p.getProdName()+ ": R$ %.2f%n", p.getPrice());
        }
        System.out.printf("Total da compra: R$ %.2f%n", calcularTotal());
        System.out.println("===========================");
    }
    
    public String getIdRastreio(){ return idRastreio; }
    public Cliente getCliente(){ return cliente; }
    public List<Produto> getProdutos(){ return produtos; }
    
    public void setIdRastreio(String idRastreio){ this.idRastreio = idRastreio; }
    public void setCliente(Cliente cliente){ this.cliente = cliente; }
    public String getStatus(){ return status; }
}