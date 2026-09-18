package src.models;

public class Produto {
    
    private int codigo = 0;
    private String prodName;
    private double price;
    private String descricao;
    
    public Produto(int codigo, String prodName, String descricao, double price){
        this.codigo = codigo;
        this.prodName = prodName;
        this.price = price;
        this.descricao = descricao;
    }

    public Produto(){}
    
    public int getcodigo(){ return codigo; }
    public String getProdName(){ return prodName; }
    public double getPrice(){ return price; }
    public String getDesc(){ return descricao; }
    
    public void setCodigo(int codigo){ this.codigo = codigo; }
    public void setProdName(String prodName){ this.prodName = prodName; }
    public void setPrice(double price){ this.price = price; }
    public void setDesc(String descricao){ this.descricao = descricao; }
}
