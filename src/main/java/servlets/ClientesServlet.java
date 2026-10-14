package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import db.ClienteDao;
import models.Cliente;

@WebServlet("/clientes")
public class ClientesServlet extends HttpServlet {
    private final ClienteDao dao = new ClienteDao();

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

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}