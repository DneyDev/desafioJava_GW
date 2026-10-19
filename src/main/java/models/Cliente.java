package models;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int id;
    private String name;
    private TipoCliente tipo;
    private String documento;
    private String email;
    private List<Endereco> enderecos;

    public Cliente(int id, String name, TipoCliente tipo, String documento, String email, List<Endereco> enderecos){
        this.id = id;
        this.name = name;
        this.tipo = tipo;
        this.documento = documento;
        this.email = email;
        this.enderecos = enderecos != null ? enderecos : new ArrayList<>();
    }

    public Cliente(){
        this.enderecos = new ArrayList<>();
    }

    public void exibirInfo(){
        System.out.println(tipo.getRotuloNome() + ": " + name);
        System.out.println(tipo.getRotuloDocumento() + ": " + documento);
        System.out.println("Email: " + email);
        if(enderecos != null && !enderecos.isEmpty()){
            System.out.println("Enderecos Cadastrados: " + enderecos.size());
            for(int i = 0; i < enderecos.size(); i++){
                System.out.println(" - Endereco " + (i+1) + ": " + enderecos.get(i).getEndCompleto());
            }
        }
    }
    
    public int getId(){ return id; }
    public String getName(){ return name; }
    public TipoCliente getTipo(){ return tipo; }
    public String getDocumento(){ return documento; }
    public String getEmail(){ return email; }
    public List<Endereco> getEnderecos(){ return enderecos;}

    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setTipo(TipoCliente tipo){ this.tipo = tipo; }
    public void setDocumento(String documento){ this.documento = documento; }
    public void setEmail(String email){ this.email = email; }
    public void setEnderecos(List<Endereco> enderecos){ this.enderecos = enderecos;}
    
    public void addEndereco(Endereco endereco){
        if(this.enderecos == null) this.enderecos = new ArrayList<>();
        this.enderecos.add(endereco);
    }
}