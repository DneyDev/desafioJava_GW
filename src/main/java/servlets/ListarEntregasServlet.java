package servlets;

import db.EntregaDao;
import models.Entrega;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/lista-entregas")
public class ListarEntregasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            EntregaDao dao = new EntregaDao();
            List<Entrega> lista = dao.listarTodas();
            request.setAttribute("listaEntregas", lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/lista-entregas.jsp").forward(request, response);
    }
}