package src.service;

import java.util.Scanner;
import java.util.UUID;
import src.models.*;
import src.validation.ClienteValid;
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

    public void cadastroCliente(){

        ClienteValid clienteValid = new ClienteValid();
        
        System.out.println("===== Menu de Cadastro =====");
        System.out.println("Insira o nome do Cliente: ");
        String name = leitor.nextLine();
        System.out.println("CPF:  ");
        String cpf = leitor.nextLine();
        System.out.println("Email: ");
        String email = leitor.nextLine();
        System.out.println("");

        System.out.println("===== Endereco =====");
        System.out.println("Estado: ");
        String estado = leitor.nextLine();
        System.out.println("Cidade: ");
        String cidade = leitor.nextLine();
        System.out.println("CEP: ");
        String cep = leitor.nextLine();
        System.out.println("Rua: ");
        String rua = leitor.nextLine();
        System.out.println("Numero: ");
        String numero = leitor.nextLine();

        Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero);
        Cliente novoCliente = new Cliente(name, cpf, email, endCliente);

        
        List<String> erros = clienteValid.validar(novoCliente);
        if(!erros.isEmpty()){
            System.out.println("Não foi possível cadastrar o cliente:");
            erros.forEach(erro -> System.out.println("- " + erro));
            return;
        }

        cliente.add(novoCliente);
        System.out.println("===========================");
    }
    public void registrarProduto(){
        System.out.println("===== Menu de Registro de Produto =====");

        System.out.println("Insira o nome do Produto: ");
        String prodName = leitor.next();
        System.out.println("Descricao: ");
        String descricao= leitor.next();
        System.out.println("Preco:  ");
        double price = leitor.nextDouble();

        Produto novoProduto = new Produto(produtos.size() + 1, prodName, descricao, price);

        //validação para Produto
        ProdutoValid produtoValid = new ProdutoValid();
        List<String>erros = produtoValid.validar(novoProduto);
        if(!erros.isEmpty()){
            System.out.println("Não foi possível registrar o Produto: ");
            erros.forEach(erro -> System.out.println("- "+ erro));
            return;
        }
        produtos.add(novoProduto);

        System.out.println("===========================");
    }
    public void novaEntrega(){
        System.out.println("===== Registar Nova Entrega =====");
        System.out.println("Clientes: ");
        for (int i = 0; i < cliente.size(); i++){
            System.out.println(i +  " - " + cliente.get(i).getName());
        }

        System.out.println("Selecione o Cliente: ");
        int indice = leitor.nextInt();

        if(indice < 0 || indice >= cliente.size()){
            System.out.println("Cliente inválido!");
            return;
        }
        System.out.println("Produto(s): ");
        for (int i = 0; i < produtos.size(); i++){
            System.out.println(i +  " - " + produtos.get(i).getProdName());
        }
        System.out.println("Selecione o Produto: ");
        int indiceProd = leitor.nextInt();

        if(indiceProd < 0 || indiceProd >= produtos.size()){
            System.out.println("Produto Inválido");
            return;
        }
        leitor.nextLine();

        String idRastreio = UUID.randomUUID().toString();
        Entrega novaEntrega = new Entrega(idRastreio, cliente.get(indice));
        novaEntrega.addProd(produtos.get(indiceProd));
        entregas.add(novaEntrega);

        //System.out.println("----- Resumo da Entrega -----");
        //System.out.println("Cliente: "+ cliente.get(indice).getName());
        //System.out.println("Produto: "+ produtos.get(indiceProd).getProdName());
    }
}   