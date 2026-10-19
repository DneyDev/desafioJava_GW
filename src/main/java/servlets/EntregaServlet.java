package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ClienteDao;
import db.EntregaDao;
import db.ProdutoDao;
import models.Cliente;
import models.Endereco;
import models.Entrega;
import models.Produto;
import utils.CodRastreioGen;

@WebServlet("/lancar-entrega")
public class EntregaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EntregaDao entregaDao = new EntregaDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final ProdutoDao produtoDao = new ProdutoDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        try {
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            int produtoId = Integer.parseInt(request.getParameter("produtoId"));
            int quantidade = Integer.parseInt(request.getParameter("quantidade"));

            List<Cliente> clientes = clienteDao.listar();
            Cliente cliente = null;
            for (Cliente c : clientes) {
                if (c.getId() == clienteId) {
                    cliente = c;
                    break;
                }
            }
            List<Produto> produtos = produtoDao.listar();
            Produto produto = null;
            for (Produto p : produtos) {
                if (p.getCodigo() == produtoId) {
                    produto = p;
                    break;
                }
            }
            if (cliente == null || produto == null) {
                response.sendRedirect("form-entrega.jsp?erro=dados+incorreto");
                return;
            }
            
            String uf = "BR";
            Endereco destino = null;
            if (cliente.getEnderecos() != null && !cliente.getEnderecos().isEmpty()) {
                destino = cliente.getEnderecos().get(0);
                if (destino.getEstado() != null) {
                    uf = CodRastreioGen.paraSigla(destino.getEstado());
                }
            }
            Set<String> existentes = entregaDao.codigosPorUf(uf);
            String codigoRastreio = CodRastreioGen.gerar(uf, existentes);

            // A origem precisa ser vinculada. Como provisório para funcionar, usaremos o próprio destino.
            // O ideal para o futuro é o usuário selecionar uma origem na tela.
            Endereco origem = destino; 
            Entrega entrega = new Entrega(codigoRastreio, cliente, origem, destino);
            entrega.addProd(produto, quantidade);

            entregaDao.salvar(entrega);

            response.sendRedirect("lista-entregas");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().println("Erro no banco de dados: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendRedirect("form-entrega.jsp?erro=formato+incorreto");
        }
    }
}