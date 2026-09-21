package src.app;
import src.service.Sistema;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        Sistema sistema = new Sistema(leitor);
        int opcao;

        do{
            System.out.println("====================");
            System.out.println("       GW POO       ");
            System.out.println("\n Selecione uma opção: ");
            System.out.println("1- Cadastrar Cliente.");
            System.out.println("2- Registrar Produto.");
            System.out.println("3- Nova Entrega.");
            System.out.println("4- Sair.");
            System.out.println("====================");

            opcao = leitor.nextInt();
            String repetir;

            switch (opcao) {
            case 1:
                do{
                    sistema.cadastroCliente();
                    System.out.println("Deseja adicionar outro cliente? (S/N)");
                    repetir = leitor.next();
                }while (repetir.equalsIgnoreCase("S"));
                break;
            case 2: 
                do{
                    sistema.registrarProduto();
                    System.out.println("Deseja registrar outro produto? (S/N)");
                    repetir = leitor.next();
                }while (repetir.equalsIgnoreCase("S"));
                break;
            case 3:
                do{
                    sistema.novaEntrega();
                    System.out.println("Deseja registrar outra entrega? (S/N)");
                    repetir = leitor.next();
                }while(repetir.equalsIgnoreCase("S"));
                break;
            case 4:
                break;
            default:
                System.out.println("Opção inválida, tente novamente!");
                break;
            }
        } while (opcao != 4);
    }
}
