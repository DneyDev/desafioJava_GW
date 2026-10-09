package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
import db.EntregaDao;
import models.Entrega;

public class EntregaController {
    private ClienteController clienteController;
    private ProdutoController produtoController;
    private List<Entrega> entregas = new ArrayList<>();
    private Scanner leitor;

    public EntregaController(Scanner leitor, ClienteController clienteController, ProdutoController produtoController) {
        this.leitor = leitor;
        this.clienteController = clienteController;
        this.produtoController = produtoController;
    }
    private final EntregaDao entregaDao = new EntregaDao();
}
