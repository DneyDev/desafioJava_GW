package validation;

import models.Produto;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProdutoValid implements Valid<Produto> {

    // Lista única de unidades: o <select> do form-produto.jsp e a validação usam a mesma.
    public static final List<String> UNIDADES_VALIDAS = List.of("kg", "g", "L", "mL");

    private static final BigDecimal PRECO_MAXIMO =
            new BigDecimal("99999999.99");
    public String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "Nome é obrigatório!";
        }
        if (nome.length() > 255) {
            return "Nome deve ter no máximo 255 caracteres!";
        }
        return null;
    }
    public String validarDescricao(String desc) {
        if (desc == null || desc.isBlank()) {
            return "Descrição é obrigatória!";
        }
        if (desc.length() > 255) {
            return "Descrição deve ter no máximo 255 caracteres!";
        }
        return null;
    }
    public String validarPreco(BigDecimal preco) {
        if (preco == null) {
            return "Preço inválido! Digite um número (ex: 19,90).";
        }
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            return "O preço deve ser maior que zero!";
        }
        if (preco.scale() > 2) {
            return "O preço deve ter no máximo 2 casas decimais!";
        }
        if (preco.compareTo(PRECO_MAXIMO) > 0) {
            return "O preço máximo é R$ 99.999.999,99!";
        }
        return null;
    }
    private static final BigDecimal PESO_MAXIMO =
            new BigDecimal("99999.999");
    public String validarPeso(BigDecimal peso) {
        if (peso == null) {
            return "Medida inválida! Digite um número (ex: 2,5).";
        }
        if (peso.compareTo(BigDecimal.ZERO) <= 0) {
            return "A medida deve ser maior que zero!";
        }
        if (peso.scale() > 3) {
            return "A medida deve ter no máximo 3 casas decimais!";
        }
        if (peso.compareTo(PESO_MAXIMO) > 0) {
            return "A medida máxima é 99999,999!";
        }
        return null;
    }
    public String validarUnidade(String unidade) {
        if (unidade == null || unidade.isBlank()) {
            return "Unidade de medida é obrigatória!";
        }
        if (!UNIDADES_VALIDAS.contains(unidade)) {
            return "Unidade de medida inválida! Use: " + String.join(", ", UNIDADES_VALIDAS) + ".";
        }
        return null;
    }
    @Override
    public List<String> validar(Produto produto) {
       
        List<String> erros = new ArrayList<>();

        addSeErro(erros, validarNome(produto.getProdName()));
        addSeErro(erros, validarDescricao(produto.getDesc()));
        addSeErro(erros, validarPreco(produto.getPrice()));
        addSeErro(erros, validarPeso(produto.getPeso()));
        addSeErro(erros, validarUnidade(produto.getUnidade()));
        return erros;
    }
    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) {
            erros.add("Erro: " + erro);
        }
    }
}