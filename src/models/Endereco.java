package src.models;

public class Endereco{
    //variáveis da Classe
    private String estado;
    private String cidade;
    private String cep;
    private String rua;
    private String numero;

    //Construtor do 'Endereco'
    public Endereco(String estado, String cidade, String cep, String rua, String numero){
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
    }

    public String getEndCompleto(){
        return rua + ", " + numero + " - " + cidade + "/" + estado + " (CEP: " + cep + ")";
    }
    //getters
    public String getEstado(){ return estado; }
    public String getCidade(){ return cidade; }
    public String getCep(){ return cep; }
    public String getRua(){ return rua; }
    public String getNumero(){ return numero; }

    //setters
    public void setEstado(String estado){ this.estado = estado; }
    public void setCidade(String cidade){ this.cidade = cidade;}
    public void setCep(String cep){ this.cep = cep; }
    public void setRua(String rua){ this.rua = rua; }
    public void setNumero(String numero){ this.numero = numero; }
}
