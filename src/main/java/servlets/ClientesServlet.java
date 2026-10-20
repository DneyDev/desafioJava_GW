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
        try {
            List<Cliente> clientes = dao.listar();
            PrintWriter out = resp.getWriter();
            out.println("<h1>Clientes</h1><ul>");
            for (Cliente c : clientes) {
                out.println("<li>" + esc(c.getName()) + " - " + esc(c.getEmail()) + "</li>");
            }
            out.println("</ul>");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("form-cliente.jsp?erro=" + URLEncoder.encode("Não foi possível consultar os clientes. Tente novamente.", "UTF-8"));
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
                String msg = URLEncoder.encode(String.join("|", erros), "UTF-8");
                response.sendRedirect("form-cliente.jsp?erro=" + msg);
                return;
            }
            if (dao.existePorDocumento(documento)) {
                redirectErro(response, "Já existe um cliente cadastrado com esse CPF/CNPJ.");
                return;
            }
            if (dao.existePorEmail(email)) {
                redirectErro(response, "Já existe um cliente cadastrado com esse e-mail.");
                return;
            }

            dao.inserir(novo);
            response.sendRedirect("form-cliente.jsp?sucesso="
                    + URLEncoder.encode("Cliente cadastrado com sucesso.", "UTF-8"));
        } catch (SQLException e) {
            e.printStackTrace();
            if ("23505".equals(e.getSQLState())) {
                redirectErro(response, "Já existe um cliente com esse CPF/CNPJ ou e-mail.");
            } else {
                redirectErro(response, "Não foi possível salvar o cliente. Tente novamente.");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            redirectErro(response, "Formato inválido. Confira os dados informados.");
        }
    }

    private void redirectErro(HttpServletResponse response, String mensagem) throws IOException {
        response.sendRedirect("form-cliente.jsp?erro=" + URLEncoder.encode(mensagem, "UTF-8"));
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}