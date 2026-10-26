package models;

import java.math.BigDecimal;

public class Produto {
    
    private int codigo = 0;
    private String prodName;
    private BigDecimal price;
    private String descricao;
    private BigDecimal peso;
    private String unidade = "kg";
    private boolean ativo = true;
    
    
    public Produto(int codigo, String prodName, String descricao, BigDecimal price, BigDecimal peso, String unidade){
        this.codigo = codigo;
        this.prodName = prodName;
        this.price = price;
        this.descricao = descricao;
        this.peso = peso;
        this.unidade = unidade;
        
    }

    // Mantido para o código antigo (console): assume "kg" quando a unidade não é informada.
    public Produto(int codigo, String prodName, String descricao, BigDecimal price, BigDecimal peso){
        this(codigo, prodName, descricao, price, peso, "kg");
    }

    public Produto(){}
    
    public int getCodigo(){ return codigo; }
    public String getProdName(){ return prodName; }
    public BigDecimal getPrice(){ return price; }
    public String getDesc(){ return descricao; }
    public BigDecimal getPeso(){ return peso; }
    public String getUnidade(){ return unidade; }
    public boolean isAtivo(){ return ativo; }
    
    public void setCodigo(int codigo){ this.codigo = codigo; }
    public void setProdName(String prodName){ this.prodName = prodName; }
    public void setPrice(BigDecimal price){ this.price = price; }
    public void setDesc(String descricao){ this.descricao = descricao; }
    public void setPeso(BigDecimal peso){ this.peso = peso; }
    public void setUnidade(String unidade){ this.unidade = unidade; }
    public void setAtivo(boolean ativo){ this.ativo = ativo; }
}