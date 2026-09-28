package src.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.models.Cliente;
import src.models.Endereco;
import src.validation.ClienteValid;

public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>();
    private Scanner leitor;

    public ClienteController(Scanner leitor) {
        this.leitor = leitor;
    }

    public void cadastrar() {
        ClienteValid clienteValid = new ClienteValid();
        String erro;

        System.out.println("===== Menu de Cadastro =====");

        String name;
        do {
            System.out.println("Insira o nome do Cliente: ");
            name = leitor.nextLine().trim();
            erro = clienteValid.validarNome(name);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String cpf;
        do {
            System.out.println("CPF: ");
            cpf = leitor.nextLine().trim();
            erro = clienteValid.validarCpf(cpf);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String email;
        do {
            System.out.println("Email: ");
            email = leitor.nextLine().trim();
            erro = clienteValid.validarEmail(email);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        System.out.println("");
        System.out.println("===== Endereco =====");

        String estado;
        do {
            System.out.println("Estado: ");
            estado = leitor.nextLine().trim();
            erro = clienteValid.validarEstado(estado);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String cidade;
        do {
            System.out.println("Cidade: ");
            cidade = leitor.nextLine().trim();
            erro = clienteValid.validarCidade(cidade);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String cep;
        do {
            System.out.println("CEP: ");
            cep = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(cep, "CEP");
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String rua;
        do {
            System.out.println("Rua: ");
            rua = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(rua, "Rua");
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String numero;
        do {
            System.out.println("Numero: ");
            numero = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(numero, "Número");
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero);
        clientes.add(new Cliente(name, cpf, email, endCliente));

        System.out.println("Cliente cadastrado com sucesso!");
        System.out.println("===========================");
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}