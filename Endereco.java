public class Endereco {

    //variáveis da Classe
    private String estado;
    private String cidade;
    private int cep;

    //Construtor do 'Endereco'
    public Endereco(String estado, String cidade, int cep){
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
    }
    //getters
    public String getEstado(){ return estado; }
    public String getCidade(){ return cidade; }
    public int getCep(){ return cep; }
}
