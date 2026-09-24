package src.validation;

import src.models.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoValid implements Valid<Produto> {

    public String validarNome(String nome) {
        return (nome == null || nome.isBlank()) ? "Nome é obrigatório!" : null;
    }

    public String validarDescricao(String desc) {
        return (desc == null || desc.isBlank()) ? "Descrição é obrigatória!" : null;
    }

    public String validarPreco(double preco) {
        return preco <= 0 ? "O preço deve ser maior que zero!" : null;
    }

    @Override
    public List<String> validar(Produto produto) {
        List<String> erros = new ArrayList<>();
        addSeErro(erros, validarNome(produto.getProdName()));
        addSeErro(erros, validarDescricao(produto.getDesc()));
        addSeErro(erros, validarPreco(produto.getPrice()));
        return erros;
    }

    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) erros.add("Erro: " + erro);
    }
}
