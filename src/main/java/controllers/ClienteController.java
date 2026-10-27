package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Cliente;
import models.Endereco;
import utils.CodRastreioGen;
import validation.ClienteValid;
import java.sql.SQLException;
import db.ClienteDao;
import models.TipoCliente;

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
    private boolean documentoJaCadastrado(String documentoLimpo) {
        try {
            return clienteDao.existePorDocumento(documentoLimpo);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
            return false;
        }
    }
    private TipoCliente lerTipo() {
        while (true) {
            System.out.println("Tipo de cliente (1 - Pessoa Física, 2 - Pessoa Jurídica): ");
            String r = leitor.nextLine().trim();
            if (r.equals("1")) return TipoCliente.PF;
            if (r.equals("2")) return TipoCliente.PJ;
            System.out.println("Erro: digite 1 ou 2!");
        }
    }
    private boolean emailJaCadastrado(String emailMinusculo) {
        try {
            return clienteDao.existePorEmail(emailMinusculo);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
            return false;
        }
    }
    public void cadastrar() {
        ClienteValid clienteValid = new ClienteValid();
        String erro;

        System.out.println("===== Menu de Cadastro =====");

        TipoCliente tipo = lerTipo();

        String name;
        do {
            System.out.println(tipo.getRotuloNome() + " do Cliente: ");
            name = leitor.nextLine().trim();
            erro = clienteValid.validarNome(name, tipo);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        String documento;
        do {
            System.out.println(tipo.getRotuloDocumento() + ": ");
            documento = leitor.nextLine().trim();
            erro = clienteValid.validarDocumento(tipo, documento);
            if (erro == null && documentoJaCadastrado(documento.replaceAll("\\D", ""))) {
                erro = "Já existe um cliente com esse " + tipo.getRotuloDocumento() + "!";
            }
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);
        documento = documento.replaceAll("\\D", "");

        String email;
        do {
            System.out.println("Email: ");
            email = leitor.nextLine().trim();
            erro = clienteValid.validarEmail(email);
            if (erro == null && emailJaCadastrado(email.toLowerCase())) {
                erro = "Já existe um cliente com esse email!";
            }
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

        String bairro;
        do {
            System.out.println("Bairro: ");
            bairro = leitor.nextLine().trim();
            erro = clienteValid.validarCampo(bairro, "Bairro", 100);
            if (erro != null) System.out.println("Erro: " + erro);
        } while (erro != null);

        estado = CodRastreioGen.paraSigla(estado);
        cep    = cep.replaceAll("\\D", "");
        email  = email.toLowerCase();

        Endereco endCliente = new Endereco(estado, cidade, cep, rua, numero, bairro);
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(endCliente);
        
        Cliente novo = new Cliente(0, name, tipo, documento, email, enderecos);
        try {
            clienteDao.inserir(novo);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Erro: já existe um cliente com esse documento ou email.");
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