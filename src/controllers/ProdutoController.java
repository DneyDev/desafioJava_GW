package src.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.models.Produto;
import src.validation.ProdutoValid;

public class ProdutoController {

    private List<Produto> produtos = new ArrayList<>();
    private Scanner leitor;

    public ProdutoController(Scanner leitor) {
        this.leitor = leitor;
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

    public List<Produto> getProdutos() {
        return produtos;
    }
}