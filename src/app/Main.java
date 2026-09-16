package src.app;

import src.models.Cliente;
import src.models.Endereco;
import src.models.Entrega;
import src.models.Produto;

public class Main {
    public static void main(String[] args) {
        //Para testar é só alterar os valores de null para o desejado
        //Ordem de info: estado, cidade, cep, rua, numero 
        Endereco end = new Endereco("Pernambuco", "Recife", "35000-001", "Tamanduá", "35" );
        //ordem de info: name, cpf, email, end(Endereço)
        Cliente cliente = new Cliente("Sidney", "012.345.678-90", "sidney@email.com", end);

        //Para produto, é só ir enumerando e decidir os valores
        //ordem de info: codigo, nome do produto, preço
        Produto p1 = new Produto("01", "bola", 10.00);
        Produto p2 = new Produto("02", "tesoura", 5.00);

        //ordem de info: id de rastreio, cliente
        Entrega entrega = new Entrega("BR01", cliente);
        entrega.addProd(p1);
        entrega.addProd(p2);

        entrega.exibirResumo();
        entrega.updStatus("A caminho");
    }
}
