package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import db.ClienteDao;
import db.EnderecoDao;
import models.Cliente;
import models.Endereco;
import utils.CodRastreioGen;
import validation.EnderecoValid;

@WebServlet("/enderecos")
public class EnderecoServlet extends HttpServlet {
    private final EnderecoDao dao = new EnderecoDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final EnderecoValid enderecoValid = new EnderecoValid();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String editar = request.getParameter("editar");
            if (editar != null && !editar.isBlank()) {
                int id = Integer.parseInt(editar);
                Endereco endereco = dao.buscarPorId(id);
                if (endereco == null) {
                    redirect(request, response, "/clientes", "erro", "Endereço não encontrado.");
                    return;
                }
                Cliente cliente = clienteDao.buscarPorId(endereco.getClienteId());
                request.setAttribute("endereco", endereco);
                request.setAttribute("clienteSelecionado", cliente);
                request.getRequestDispatcher("/form-endereco.jsp").forward(request, response);
                return;
            }

            String clienteIdParam = request.getParameter("clienteId");
            if (clienteIdParam != null && !clienteIdParam.isBlank()) {
                int clienteId = Integer.parseInt(clienteIdParam);
                Cliente cliente = clienteDao.buscarPorId(clienteId);
                if (cliente == null) {
                    redirect(request, response, "/clientes", "erro", "Cliente não encontrado.");
                    return;
                }
                request.setAttribute("clienteSelecionado", cliente);
            } else {
                request.setAttribute("clientes", clienteDao.listar());
            }
            request.getRequestDispatcher("/form-endereco.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            redirect(request, response, "/form-endereco.jsp", "erro", "Não foi possível consultar os dados. Tente novamente.");
        } catch (IllegalArgumentException | NullPointerException e) {
            redirect(request, response, "/form-endereco.jsp", "erro", "Identificador inválido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");
        boolean editando = idParam != null && !idParam.isBlank();
        int id = 0;
        int clienteId = 0;

        try {
            Endereco existente = null;
            if (editando) {
                id = Integer.parseInt(idParam);
                existente = dao.buscarPorId(id);
                if (existente == null) {
                    redirect(request, response, "/clientes", "erro", "Endereço não encontrado.");
                    return;
                }
                clienteId = existente.getClienteId();
            } else {
                clienteId = Integer.parseInt(request.getParameter("clienteId"));
            }

            String estado = request.getParameter("estado").trim();
            String uf = CodRastreioGen.paraSigla(estado);
            if (uf != null) {
                estado = uf;
            }
            Endereco endereco = new Endereco(
                estado,
                request.getParameter("cidade").trim(),
                request.getParameter("cep").replaceAll("\\D", ""),
                request.getParameter("rua").trim(),
                request.getParameter("numero").trim()
            );
            endereco.setClienteId(clienteId);
            endereco.setId(id);

            Cliente cliente = clienteDao.buscarPorId(clienteId);
            if (cliente == null) {
                redirect(request, response, "/clientes", "erro", "Cliente não encontrado.");
                return;
            }
            List<String> erros = enderecoValid.validar(endereco);
            if (!erros.isEmpty()) {
                redirect(request, response, editando ? "/enderecos?editar=" + id : "/enderecos?clienteId=" + clienteId,
                        "erro", String.join("|", erros));
                return;
            }

            if (editando) {
                if (!dao.atualizar(endereco)) {
                    redirect(request, response, "/clientes", "erro", "Endereço não encontrado.");
                    return;
                }
                redirect(request, response, "/clientes?editar=" + clienteId, "sucesso", "Endereço atualizado com sucesso.");
            } else {
                dao.inserir(endereco);
                redirect(request, response, "/clientes?editar=" + clienteId, "sucesso", "Endereço cadastrado com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String destino = clienteId > 0 ? "/clientes?editar=" + clienteId : "/clientes";
            redirect(request, response, destino, "erro", "Não foi possível salvar o endereço. Tente novamente.");
        } catch (IllegalArgumentException | NullPointerException e) {
            String destino = editando && id > 0 ? "/enderecos?editar=" + id : "/enderecos";
            redirect(request, response, destino, "erro", "Formato inválido. Confira os dados informados.");
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String caminho, String parametro, String mensagem)
            throws IOException {
        response.sendRedirect(request.getContextPath() + caminho + (caminho.contains("?") ? "&" : "?") + parametro + "="
                + URLEncoder.encode(mensagem, "UTF-8"));
    }
}