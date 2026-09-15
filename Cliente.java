public class Cliente {

    //definição de variáveis do Cliente
    private String name;
    private int cpf;
    private String email;

    //construtor para a classe Cliente
    public Cliente(String name, int cpf, String email){
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }
    //getters
    public String getName(){ return name; }
    public int getCpf(){ return cpf; }
    public String getEmail(){ return email; }
}
