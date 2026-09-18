package src.app;
import src.service.Sistema;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Para testar é só alterar os valores de null para o desejado
        //Ordem de info: estado, cidade, cep, rua, numero 
       /*  Endereco end = new Endereco("Pernambuco", "Recife", "35000-001", "Rua Tamanduá", "35" );
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
        */
        Scanner leitor = new Scanner(System.in);
        Sistema sistema = new Sistema(leitor);

        System.out.println("====================");
        System.out.println("       GW POO       ");
        System.out.println("\n Selecione uma opção: ");
        System.out.println("1- Cadastrar Cliente.");
        System.out.println("2- Registrar Produto.");
        System.out.println("3- Nova Entrega.");
        System.out.println("4- Sair.");
        System.out.println("====================");

        int opcao = leitor.nextInt();
        switch (opcao) {
            case 1:
                sistema.cadastroCliente();
                break;
            case 2: 
                sistema.registrarProduto();
                break;
            case 3:
                sistema.novaEntrega();
                break;
            case 4:
                break;
            default:
                System.out.println("====================");
                System.out.println("       GW POO       ");
                System.out.println("\n Selecione uma opção: ");
                System.out.println("1- Cadastrar Cliente.");
                System.out.println("2- Registrar Produto.");
                System.out.println("3- Nova Entrega.");
                System.out.println("4- Sair.");
                System.out.println("====================");
                break;
        }
    }
}
