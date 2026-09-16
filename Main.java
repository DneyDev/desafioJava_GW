public class Main {
    public static void main(String[] args) {
        //Para testar é só alterar os valores de null para o desejado
        Endereco end = new Endereco(null, null, null, null, null);
        Cliente cliente = new Cliente(null, null, null, end);

        //Para produto, é só ir enumerando e decidir os valores
        Produto p1 = new Produto(null, null, 0);
        Produto p2 = new Produto(null, null, 0);

        Entrega entrega = new Entrega(null, cliente);
        entrega.addProd(p1);
        entrega.addProd(p2);

        entrega.exibirResumo();
        entrega.updStatus(null);
    }
}
