package src.service;

import java.util.Scanner;
import src.models.*;

import java.util.ArrayList;
import java.util.List;

public class Sistema {

    private List<Cliente> cliente = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private List<Endereco> enderecos =  new ArrayList<>();
    private List<Entrega>  entregas = new ArrayList<>();
    private Scanner leitor;

    public Sistema(Scanner leitor){
        this.leitor = leitor;
    }

    public void cadastroCliente(){

        System.out.println("===== Menu de Cadastro =====");
        System.out.println("Insira o nome do Cliente: ");
        String name = leitor.nextLine();
        System.out.println("CPF:  ");
        String cpf = leitor.nextLine();
        System.out.println("Email: ");
        String email= leitor.nextLine();
        System.out.println("");

        System.out.println("===== Endereco =====");
        System.out.println("Estado: ");
        String estado = leitor.next();
        System.out.println("Cidade: ");
        String cidade = leitor.next();
        System.out.println("CEP: ");
        String cep = leitor.next();
        System.out.println("Rua: ");
        String rua = leitor.next();
        System.out.println("Numero: ");
        String numero = leitor.next();

        Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero);
        Cliente novoCliente = new Cliente(name, cpf, email, endCliente);
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
        produtos.add(novoProduto);

        System.out.println("===========================");
    }
    
}   
