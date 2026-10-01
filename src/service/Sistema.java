package src.service;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import src.controllers.ClienteController;
import src.controllers.ProdutoController;
import src.db.EntregaDao;
import src.models.*;
import src.utils.CodRastreioGen;
import src.validation.EntregaValid;

public class Sistema {

    private ClienteController clienteController;
    private ProdutoController produtoController;
    private final EntregaDao entregaDao = new EntregaDao();
    private List<Entrega> entregas = new ArrayList<>();
    private Scanner leitor;

    public Sistema(Scanner leitor) {
        this.leitor = leitor;
        this.clienteController = new ClienteController(leitor);
        this.produtoController = new ProdutoController(leitor);
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

    public void cadastroCliente() {
        clienteController.cadastrar();
    }

    public void registrarProduto() {
        produtoController.registrar();
    }

    public void novaEntrega() {
        System.out.println("===== Registrar Nova Entrega =====");

        List<Cliente> clientes = clienteController.getClientes();
        List<Produto> produtos = produtoController.getProdutos();

        if (clientes.isEmpty() || produtos.isEmpty()) {
            System.out.println("Cadastre ao menos um cliente e um produto antes de registrar uma entrega.");
            return;
        }

        System.out.println("Clientes: ");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(i + " - " + clientes.get(i).getName());
        }
        int indice = lerInt("Selecione o Cliente: ");
        if (indice < 0 || indice >= clientes.size()) {
            System.out.println("Cliente inválido!");
            return;
        }

        String uf = CodRastreioGen.paraSigla(clientes.get(indice).getEnd().getEstado());

        Set<String> existentes = new HashSet<>();
        for (Entrega ent : entregas) existentes.add(ent.getIdRastreio());
        try {
            existentes.addAll(entregaDao.codigosPorUf(uf));
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar o banco: " + ex.getMessage());
            return;
        }
        String codigo = CodRastreioGen.gerar(uf, existentes);
        Entrega novaEntrega = new Entrega(codigo, clientes.get(indice));

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