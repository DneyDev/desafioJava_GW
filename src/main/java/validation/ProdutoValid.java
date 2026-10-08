package validation;

import models.Produto;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProdutoValid implements Valid<Produto> {

    private static final BigDecimal PRECO_MAXIMO = new BigDecimal("99999999.99");

    public String validarNome(String nome) {
        if (nome == null || nome.isBlank()) return "Nome é obrigatório!";
        if (nome.length() > 255) return "Nome deve ter no máximo 255 caracteres!";
        return null;
    }

    public String validarDescricao(String desc) {
        return (desc == null || desc.isBlank()) ? "Descrição é obrigatória!" : null;
    }

    public String validarPreco(BigDecimal preco) {
        if (preco == null) return "Preço inválido! Digite um número (ex: 19,90).";
        if (preco.compareTo(BigDecimal.ZERO) <= 0) return "O preço deve ser maior que zero!";
        if (preco.scale() > 2) return "O preço deve ter no máximo 2 casas decimais!";
        if (preco.compareTo(PRECO_MAXIMO) > 0) return "O preço máximo é R$ 99.999.999,99!";
        return null;
    }

    private static final BigDecimal PESO_MAXIMO = new BigDecimal("99999.999");

    public String validarPeso(BigDecimal peso) {
        if (peso == null) return "Peso inválido! Digite um número em kg (ex: 2,5).";
        if (peso.compareTo(BigDecimal.ZERO) <= 0) return "O peso deve ser maior que zero!";
        if (peso.scale() > 3) return "O peso deve ter no máximo 3 casas decimais!";
        if (peso.compareTo(PESO_MAXIMO) > 0) return "O peso máximo é 99999,999 kg!";
        return null;
    }

    @Override
    public List<String> validar(Produto produto) { //todas as validações de Produto inicializam aqui
        List<String> erros = new ArrayList<>();
        addSeErro(erros, validarNome(produto.getProdName()));
        addSeErro(erros, validarDescricao(produto.getDesc()));
        addSeErro(erros, validarPreco(produto.getPrice()));
        addSeErro(erros, validarPeso(produto.getPeso())); 
        return erros;
    }

    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) erros.add("Erro: " + erro);
    }
}
