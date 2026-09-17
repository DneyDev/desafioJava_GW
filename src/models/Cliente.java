package src.models;

public class Cliente {

    private String name;
    private String cpf;
    private String email;
    private Endereco endereco;

    public Cliente(String name, String cpf, String email, Endereco endereco){
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
    }

    public Cliente(){}

    public void exibirInfo(){
        System.out.println("Nome: "+ name);
        System.out.println("CPF: " + cpf);
        System.out.println("Email: " + email);
        System.out.println("Endereco: "+ endereco.getEndCompleto());
    }
    
    public String getName(){ return name; }
    public String getCpf(){ return cpf; }
    public String getEmail(){ return email; }
    public Endereco getEnd(){ return endereco;}

    public void setName(String name){ this.name = name; }
    public void setCpf(String cpf){ this.cpf = cpf; }
    public void setEmail(String email){ this.email = email; }
    public void setEnd(Endereco endereco){ this.endereco = endereco;}
}
