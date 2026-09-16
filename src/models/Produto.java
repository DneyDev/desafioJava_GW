package src.models;

public class Produto {
    //variáveis
    private String codigo;
    private String prodName;
    private double price;
    //Construtor do 'Produto'
    public Produto(String codigo, String prodName, double price){
        this.codigo = codigo;
        this.prodName = prodName;
        this.price = price;
    }
    //getters
    public String getcodigo(){ return codigo; }
    public String getProdName(){ return prodName; }
    public double getPrice(){ return price; }
    //setters
    public void setCodigo(String codigo){ this.codigo = codigo; }
    public void setProdName(String prodName){ this.prodName = prodName; }
    public void setPrice(double price){ this.price = price; }
}
