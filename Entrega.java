import java.util.ArrayList;
import java.util.List;

public class Entrega {
    private String idRastreio;
    private Cliente cliente;
    private List<Produto>produtos;
    private String status;
    //construtor de Entrega
    public Entrega(String idRastreio, Cliente cliente){
        this.idRastreio = idRastreio;
        this.cliente = cliente;
        this.produtos = new ArrayList<>();
        this.status = "Pendente";
    }
    //métodos
    public void addProd(Produto produto){
        produtos.add(produto);
    }
    public void updStatus(String novoStatus){
        this.status = novoStatus;
        System.out.println("Status de entrega: " + idRastreio + " Atualizado para: "+ status);
    }
    public double calcularTotal(){
        double total = 0;
        for(Produto p : produtos){ //foreach p(produto) em Produto
            total += p.getPrice();
        }
        return total;
    }
    public void exibirResumo(){
        System.out.println("\n=====Resumo da Entrega=====");
        System.out.println("Rastreio: "+ idRastreio + "| Status: "+ status);
        System.out.println("Endereco: "+ cliente.getEnd().getEndCompleto());
        System.out.println("Destinatario: "+ cliente.getName());
        System.out.println("Itens: ");
        for(Produto p : produtos){
            System.out.println(" - " + p.getProdName()+ ": R$ "+ p.getPrice());
        }
        System.out.println("Total da compra: R$ "+ calcularTotal());
        System.out.println("===========================");
    }
    //getters
    public String getIdRastreio(){ return idRastreio; }
    public Cliente getCliente(){ return cliente; }
    public List<Produto> getProdutos(){ return produtos; }
    //setters
    public void setIdRastreio(String idRastreio){ this.idRastreio = idRastreio; }
    public void setCliente(Cliente cliente){ this.cliente = cliente; }
    public String getStatus(){ return status; }
}
