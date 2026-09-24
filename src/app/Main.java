package src.app;

import java.util.Scanner;
import src.service.Sistema;

public class Main {

    private static Scanner leitor = new Scanner(System.in);

    private static int lerOpcao() {
        try {
            return Integer.parseInt(leitor.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean perguntarSN(String msg) {
        String r;
        do {
            System.out.println(msg + " (S/N)");
            r = leitor.nextLine().trim();
            if (!r.equalsIgnoreCase("S") && !r.equalsIgnoreCase("N")) {
                System.out.println("Insira um caractere válido!");
            }
        } while (!r.equalsIgnoreCase("S") && !r.equalsIgnoreCase("N"));
        return r.equalsIgnoreCase("S");
    }

    public static void main(String[] args) {
        Sistema sistema = new Sistema(leitor);
        int opcao;

        do {
            System.out.println("====================");
            System.out.println("       GW POO       ");
            System.out.println("\n Selecione uma opção: ");
            System.out.println("1- Cadastrar Cliente.");
            System.out.println("2- Registrar Produto.");
            System.out.println("3- Nova Entrega.");
            System.out.println("4- Sair.");
            System.out.println("====================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    do { sistema.cadastroCliente(); }
                    while (perguntarSN("Deseja adicionar outro cliente?"));
                    break;
                case 2:
                    do { sistema.registrarProduto(); }
                    while (perguntarSN("Deseja registrar outro produto?"));
                    break;
                case 3:
                    do { sistema.novaEntrega(); }
                    while (perguntarSN("Deseja registrar outra entrega?"));
                    break;
                case 4:
                    System.out.println("Encerrando o sistema. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente!");
            }
        } while (opcao != 4);

        leitor.close();
    }
}