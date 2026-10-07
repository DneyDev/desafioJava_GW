package models;

import java.math.BigDecimal;

public class ItemEntrega {

    private Produto produto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemEntrega(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPrice();
    }

    public void somarQuantidade(int extra) {
        this.quantidade += extra;
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}