package models;

public class Cliente {

    private int id;
    private String name;
    private TipoCliente tipo;
    private String documento;
    private String email;
    private Endereco endereco;

    public Cliente(int id, String name, TipoCliente tipo, String documento, String email, Endereco endereco){
        this.id = id;
        this.name = name;
        this.tipo = tipo;
        this.documento = documento;
        this.email = email;
        this.endereco = endereco;
    }

    public Cliente(){}

    public void exibirInfo(){
        System.out.println(tipo.getRotuloNome() + ": " + name);
        System.out.println(tipo.getRotuloDocumento() + ": " + documento);
        System.out.println("Email: " + email);
        System.out.println("Endereco: "+ endereco.getEndCompleto());
    }
    
    public int getId(){ return id; }
    public String getName(){ return name; }
    public TipoCliente getTipo(){ return tipo; }
    public String getDocumento(){ return documento; }
    public String getEmail(){ return email; }
    public Endereco getEnd(){ return endereco;}

    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setTipo(TipoCliente tipo){ this.tipo = tipo; }
    public void setDocumento(String documento){ this.documento = documento; }
    public void setEmail(String email){ this.email = email; }
    public void setEnd(Endereco endereco){ this.endereco = endereco;}
}