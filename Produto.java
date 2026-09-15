public class Produto {
    
    //variáveis
    private String prodName;
    private float price;
    
    //Construtor do 'Produto'
    public Produto(String prodName, float price){
        this.prodName = prodName;
        this.price = price;
    }
    //getters
    public String getProdName(){ return prodName; }
    public float getPrice(){ return price; }
}
