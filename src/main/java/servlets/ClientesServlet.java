package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ClienteDao;
import models.Cliente;
import models.TipoCliente;
import models.Endereco;
import validation.ClienteValid;

@WebServlet("/clientes")
public class ClientesServlet extends HttpServlet {
    private final ClienteDao dao = new ClienteDao();
    private final ClienteValid clienteValid = new ClienteValid();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            List<Cliente> clientes = dao.listar();
            out.println("<h1>Clientes</h1><ul>");
            for (Cliente c : clientes) {
                out.println("<li>" + esc(c.getName()) + " - " + esc(c.getEmail()) + "</li>");
            }
            out.println("</ul>");
        } catch (SQLException e) {
            resp.setStatus(500);
            out.println("Erro ao consultar o banco: " + esc(e.getMessage()));
        }
    }
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            TipoCliente tipo = TipoCliente.valueOf(request.getParameter("tipo"));
            String nome = request.getParameter("nome").trim();
            String documento = request.getParameter("documento").replaceAll("\\D", "");
            String email = request.getParameter("email").trim().toLowerCase();

            String estado = request.getParameter("estado").trim();
            String cidade = request.getParameter("cidade").trim();
            String cep = request.getParameter("cep").replaceAll("\\D", "");
            String rua = request.getParameter("rua").trim();
            String numero = request.getParameter("numero").trim();

            Endereco end = new Endereco(estado, cidade, cep, rua, numero);
            List<Endereco> enderecos = new ArrayList<>();
            enderecos.add(end);
            Cliente novo = new Cliente(0, nome, tipo, documento, email, enderecos);

            List<String> erros = clienteValid.validar(novo);
            if (!erros.isEmpty()) {
                String msg = URLEncoder.encode(erros.get(0), "UTF-8");
                response.sendRedirect("form-cliente.jsp?erro=" + msg);
                return;
            }
            if (dao.existePorDocumento(documento)) {
                response.sendRedirect("form-cliente.jsp?erro=documento_duplicado");
                return;
            }
            if (dao.existePorEmail(email)) {
                response.sendRedirect("form-cliente.jsp?erro=email_duplicado");
                return;
            }
            dao.inserir(novo);
            response.sendRedirect("form-cliente.jsp?sucesso=Cliente+cadastrado+com+sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            if ("23505".equals(e.getSQLState())) {
                response.sendRedirect("form-cliente.jsp?erro=cliente_duplicado");
            } else {
                response.setStatus(500);
                response.getWriter().println("Erro no banco de dados: " + e.getMessage());
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            response.sendRedirect("form-cliente.jsp?erro=formato_invalido");
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}