package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
import db.ProdutoDao;
import models.Produto;
import validation.ProdutoValid;
import java.math.BigDecimal;

public class ProdutoController {

    private List<Produto> produtos = new ArrayList<>();
    private final ProdutoDao produtoDao = new ProdutoDao();
    private Scanner leitor;

    public ProdutoController(Scanner leitor) {
        this.leitor = leitor;
        carregarProdutos();
    }

    private void carregarProdutos() {
        try {
            produtos = produtoDao.listar();
        } catch (SQLException e) {
            System.out.println("Erro ao carregar produtos do banco: " + e.getMessage());
        }
    }

    public void registrar() {
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

            BigDecimal price = null;
            try {
                price = new BigDecimal(leitor.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                // fica null; o ProdutoValid rejeita
            }
            
            System.out.println("Peso(Kg): ");
            BigDecimal peso = null;
            try{
                peso = new BigDecimal(leitor.nextLine().trim().replace(",", "."));
            }catch(NumberFormatException e){
                //fica null pois o Produto Valid rejeita
            }

            novoProduto = new Produto(0, prodName, descricao, price, peso);
            erros = produtoValid.validar(novoProduto);
            if (!erros.isEmpty()) {
                System.out.println("Dados inválidos, preencha novamente:");
                erros.forEach(e -> System.out.println("- " + e));
            }
        } while (!erros.isEmpty());

        try {
            produtoDao.inserir(novoProduto);
        } catch (SQLException ex) {
            System.out.println("Erro ao salvar o produto no banco: " + ex.getMessage());
            return;
        }
        produtos.add(novoProduto);
        System.out.println("Produto registrado com sucesso!");
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}