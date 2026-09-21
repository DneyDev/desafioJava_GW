package src.app;
import src.service.Sistema;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
        do{
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
        } while (opcao != 4);

    }
}
