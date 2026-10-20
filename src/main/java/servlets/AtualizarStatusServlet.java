package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.EntregaDao;
import validation.EntregaValid;

@WebServlet("/atualizar-status")
public class AtualizarStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EntregaDao entregaDao = new EntregaDao();
    private final EntregaValid entregaValid = new EntregaValid();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String codigoRastreio = request.getParameter("codigoRastreio");
        String novoStatus = request.getParameter("novoStatus");

        if (codigoRastreio == null || codigoRastreio.isBlank()
                || novoStatus == null || novoStatus.isBlank()) {
            redirectErro(response, "Os campos código de rastreio e status são obrigatórios.");
            return;
        }

        try {
            String statusAtual = entregaDao.lerStatusAtual(codigoRastreio);
            if (statusAtual == null) {
                redirectErro(response, "Entrega não encontrada para o código de rastreio informado.");
                return;
            }

            String erro = entregaValid.validarAtualizacaoStatus(statusAtual, novoStatus);
            if (erro != null) {
                redirectErro(response, erro);
                return;
            }

            String statusPadrao = EntregaValid.statusPadrao(novoStatus);
            boolean atualizou = entregaDao.atualizarStatus(codigoRastreio, statusPadrao);
            if (!atualizou) {
                redirectErro(response, "Entrega não encontrada para o código de rastreio informado.");
                return;
            }

            response.sendRedirect("lista-entregas?sucesso="
                    + URLEncoder.encode("Status da entrega atualizado com sucesso.", "UTF-8"));
        } catch (SQLException e) {
            e.printStackTrace();
            redirectErro(response, "Não foi possível atualizar o status da entrega. Tente novamente.");
        }
    }

    private void redirectErro(HttpServletResponse response, String mensagem) throws IOException {
        response.sendRedirect("lista-entregas?erro=" + URLEncoder.encode(mensagem, "UTF-8"));
    }
}