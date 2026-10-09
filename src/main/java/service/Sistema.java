package service;

import java.util.Scanner;
import controllers.ClienteController;
import controllers.EntregaController;
import controllers.ProdutoController;

public class Sistema {

    private ClienteController clienteController;
    private ProdutoController produtoController;
    private EntregaController entregaController;

    public Sistema(Scanner leitor) {
        this.clienteController = new ClienteController(leitor);
        this.produtoController = new ProdutoController(leitor);
        this.entregaController = new EntregaController(leitor, clienteController, produtoController);
    }

    public void cadastroCliente() {
        clienteController.cadastrar();
    }

    public void registrarProduto() {
        produtoController.registrar();
    }

    public void novaEntrega(){
        entregaController.novaEntrega();
    }
}