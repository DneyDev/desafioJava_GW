package src.models;

public class Endereco{
    
    private String estado;
    private String cidade;
    private String cep;
    private String rua;
    private String numero;

    
    public Endereco(String estado, String cidade, String cep, String rua, String numero){
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
    }

    public Endereco(){}

    public String getEndCompleto(){
        return rua + ", " + numero + " - " + cidade + "/" + estado + " (CEP: " + cep + ")";
    }
    
    public String getEstado(){ return estado; }
    public String getCidade(){ return cidade; }
    public String getCep(){ return cep; }
    public String getRua(){ return rua; }
    public String getNumero(){ return numero; }

    
    public void setEstado(String estado){ this.estado = estado; }
    public void setCidade(String cidade){ this.cidade = cidade;}
    public void setCep(String cep){ this.cep = cep; }
    public void setRua(String rua){ this.rua = rua; }
    public void setNumero(String numero){ this.numero = numero; }
}
