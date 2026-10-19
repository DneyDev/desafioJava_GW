package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.EntregaDao;

@WebServlet("/atualizar-status")
public class AtualizarStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EntregaDao entregaDao = new EntregaDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String codigoRastreio = request.getParameter("codigoRastreio");
        String novoStatus = request.getParameter("novoStatus");

        try {
            if (codigoRastreio != null && novoStatus != null) {
                entregaDao.atualizarStatus(codigoRastreio, novoStatus);
            }
            response.sendRedirect("lista-entregas");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().println("Erro ao atualizar status: " + e.getMessage());
        }
    }
}