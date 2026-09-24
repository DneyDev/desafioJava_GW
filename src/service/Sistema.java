package src.service;

import java.util.Scanner;
import java.util.UUID;
import src.models.*;
import src.validation.ClienteValid;
import src.validation.EntregaValid;
import src.validation.ProdutoValid;

import java.util.ArrayList;
import java.util.List;

public class Sistema {

    private List<Cliente> cliente = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    //private List<Endereco> enderecos =  new ArrayList<>();
    private List<Entrega>  entregas = new ArrayList<>();
    private Scanner leitor;

    public Sistema(Scanner leitor){
        this.leitor = leitor;
    }
    private int lerInt(String msg) {
        while (true) {
            System.out.println(msg);
            try {
                return Integer.parseInt(leitor.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido!");
            }
        }
    }
    private boolean lerSN(String msg) {
        while (true) {
            System.out.println(msg + " (S/N)");
            String r = leitor.nextLine().trim();
            if (r.equalsIgnoreCase("S")) return true;
            if (r.equalsIgnoreCase("N")) return false;
            System.out.println("Insira um caractere válido!");
        }
    }

    public void cadastroCliente(){
        
        ClienteValid clienteValid = new ClienteValid();
        List<String>erros;
        Cliente novoCliente;

        do{   
            System.out.println("===== Menu de Cadastro =====");
            System.out.println("Insira o nome do Cliente: ");
            String name = leitor.nextLine().trim();
            System.out.println("CPF:  ");
            String cpf = leitor.nextLine().trim();
            System.out.println("Email: ");
            String email = leitor.nextLine().trim();
            System.out.println("");

            System.out.println("===== Endereco =====");
            System.out.println("Estado: ");
            String estado = leitor.nextLine().trim();
            System.out.println("Cidade: ");
            String cidade = leitor.nextLine().trim();
            System.out.println("CEP: ");
            String cep = leitor.nextLine().trim();
            System.out.println("Rua: ");
            String rua = leitor.nextLine().trim();
            System.out.println("Numero: ");
            String numero = leitor.nextLine().trim();

            Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero);
            novoCliente = new Cliente(name, cpf, email, endCliente);    

            
            erros = clienteValid.validar(novoCliente);
            if(!erros.isEmpty()){
                System.out.println("Não foi possível cadastrar o cliente:");
                erros.forEach(erro -> System.out.println("- " + erro));
            }
        }while(!erros.isEmpty());    

        cliente.add(novoCliente);
        System.out.println("===========================");
        
    }
    public void registrarProduto() {
        ProdutoValid produtoValid = new ProdutoValid();
        Produto novoProduto;
        List<String> erros;

        do {
            System.out.println("===== Menu de Registro de Produto =====");
            System.out.println("Insira o nome do Produto: ");
            String prodName = leitor.nextLine().trim();
            System.out.println("Descricao: ");
            String descricao = leitor.nextLine().trim();
            System.out.println("Preco: ");

            double price = 0;
            try {
                price = Double.parseDouble(leitor.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                // fica 0; o ProdutoValid rejeita
            }

            novoProduto = new Produto(produtos.size() + 1, prodName, descricao, price);
            erros = produtoValid.validar(novoProduto);
            if (!erros.isEmpty()) {
                System.out.println("Dados inválidos, preencha novamente:");
                erros.forEach(e -> System.out.println("- " + e));
            }
        } while (!erros.isEmpty());

        produtos.add(novoProduto);
        System.out.println("Produto registrado com sucesso!");
    }
    
    public void novaEntrega() {
        System.out.println("===== Registrar Nova Entrega =====");

        if (cliente.isEmpty() || produtos.isEmpty()) {
            System.out.println("Cadastre ao menos um cliente e um produto antes de registrar uma entrega.");
            return;
        }

        System.out.println("Clientes: ");
        for (int i = 0; i < cliente.size(); i++) {
            System.out.println(i + " - " + cliente.get(i).getName());
        }
        int indice = lerInt("Selecione o Cliente: ");
        if (indice < 0 || indice >= cliente.size()) {
            System.out.println("Cliente inválido!");
            return;
        }

        Entrega novaEntrega = new Entrega(UUID.randomUUID().toString(), cliente.get(indice));

        do {
            System.out.println("Produto(s): ");
            for (int i = 0; i < produtos.size(); i++) {
                System.out.println(i + " - " + produtos.get(i).getProdName());
            }
            int indiceProd = lerInt("Selecione o Produto: ");
            if (indiceProd < 0 || indiceProd >= produtos.size()) {
                System.out.println("Produto inválido!");
            } else {
                novaEntrega.addProd(produtos.get(indiceProd));
                System.out.println("Produto adicionado.");
            }
        } while (lerSN("Adicionar outro produto a esta entrega?"));

        List<String> erros = new EntregaValid().validar(novaEntrega);
        if (!erros.isEmpty()) {
            erros.forEach(e -> System.out.println("- " + e));
            return;
        }

        entregas.add(novaEntrega);
        System.out.println("Entrega registrada com sucesso!");
        novaEntrega.exibirResumo();
    }
}   