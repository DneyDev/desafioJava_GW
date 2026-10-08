package models;

import java.math.BigDecimal;

public class Produto {
    
    private int codigo = 0;
    private String prodName;
    private BigDecimal price;
    private String descricao;
    private BigDecimal peso;
    
    
    public Produto(int codigo, String prodName, String descricao, BigDecimal price, BigDecimal peso){
        this.codigo = codigo;
        this.prodName = prodName;
        this.price = price;
        this.descricao = descricao;
        this.peso = peso;
    }

    public Produto(){}
    
    public int getCodigo(){ return codigo; }
    public String getProdName(){ return prodName; }
    public BigDecimal getPrice(){ return price; }
    public String getDesc(){ return descricao; }
    public BigDecimal getPeso(){ return peso; }
    
    public void setCodigo(int codigo){ this.codigo = codigo; }
    public void setProdName(String prodName){ this.prodName = prodName; }
    public void setPrice(BigDecimal price){ this.price = price; }
    public void setDesc(String descricao){ this.descricao = descricao; }
    public void getPeso(BigDecimal peso){ this.peso = peso; }
}
