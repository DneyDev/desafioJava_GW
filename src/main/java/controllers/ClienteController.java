package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Cliente;
import models.Endereco;
import utils.CodRastreioGen;
import utils.CpfUtil;
import validation.ClienteValid;
import java.sql.SQLException;
import db.ClienteDao;

public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>();
    private Scanner leitor;

    public ClienteController(Scanner leitor) {
        this.leitor = leitor;
        carregarClientes();
    }
    private final ClienteDao clienteDao = new ClienteDao();

    private void carregarClientes(){
        try{
            clientes = clienteDao.listar();
        } catch(SQLException e){
            System.out.println("Erro ao carregar clientes do banco");
        }
    }
    private boolean cpfJaCadastrado(String cpfLimpo) {
        try {
            return clienteDao.existePorCpf(cpfLimpo);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
            return false;
        }
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
            if (erro == null && cpfJaCadastrado(CpfUtil.limpar(cpf))) {
                erro = "Já existe um cliente com esse CPF!";
            }
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String email;
        do {
            System.out.println("Email: ");
            email = leitor.nextLine().trim();
            erro = clienteValid.validarEmail(email);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        cpf = CpfUtil.limpar(cpf);

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
            erro = clienteValid.validarCep(cep);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        estado = CodRastreioGen.paraSigla(estado);
        cep = cep.replaceAll("\\D", "");

        String rua;
        do {
            System.out.println("Rua: ");
            rua = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(rua, "Rua", 255);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String numero;
        do {
            System.out.println("Numero: ");
            numero = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(numero, "Número", 10);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        cpf    = CpfUtil.limpar(cpf);
        estado = CodRastreioGen.paraSigla(estado);
        cep    = cep.replaceAll("\\D", "");
        email  = email.toLowerCase();

        Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero);
        Cliente novo = new Cliente(0,name, cpf, email, endCliente);
        try {
            clienteDao.inserir(novo);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Erro: já existe um cliente com esse CPF.");
            } else {
                System.out.println("Erro ao salvar no banco: " + e.getMessage());
            }
            return;
        }
        clientes.add(novo);

        System.out.println("Cliente cadastrado com sucesso!");
        System.out.println("===========================");
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}