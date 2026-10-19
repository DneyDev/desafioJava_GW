package models;

public class Endereco {
    private int id;
    private int clienteId;
    private String estado;
    private String cidade;
    private String cep;
    private String rua;
    private String numero;

    public Endereco(int id, int clienteId, String estado, String cidade, String cep, String rua, String numero) {
        this.id = id;
        this.clienteId = clienteId;
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
    }

    public Endereco(String estado, String cidade, String cep, String rua, String numero) {
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
    }
    
    public Endereco() {}

    public String getEndCompleto() {
        return rua + ", " + numero + " - " + cidade + "/" + estado + " (" + cep + ")";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}