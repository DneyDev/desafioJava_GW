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

    public ItemEntrega(Produto produto, int quantidade, BigDecimal precoUnitario) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public void somarQuantidade(int extra) {
        try {
            this.quantidade = Math.addExact(this.quantidade, extra);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("A quantidade total desse produto excede o limite permitido!");
        }
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}