package servlets;

import java.io.IOException;
import java.net.URLEncoder;
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
import validation.EnderecoValid;

@WebServlet(urlPatterns = {"/lancar-entrega", "/entregas/editar"})
public class EntregaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EntregaDao entregaDao = new EntregaDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final ProdutoDao produtoDao = new ProdutoDao();
    private final EnderecoValid enderecoValid = new EnderecoValid();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"/entregas/editar".equals(request.getServletPath())) {
            response.sendRedirect(request.getContextPath() + "/form-entrega.jsp");
            return;
        }
        String codigo = request.getParameter("codigo");
        try {
            if (codigo == null || codigo.isBlank()) {
                redirectErro(request, response, true, codigo, "Código de rastreio obrigatório.");
                return;
            }
            Entrega entrega = entregaDao.buscarPorCodigo(codigo);
            if (entrega == null) {
                redirectErro(request, response, false, codigo, "Entrega não encontrada.");
                return;
            }
            if (!"Pendente".equalsIgnoreCase(entrega.getStatus())) {
                redirectErro(request, response, false, codigo, "Somente entregas Pendentes podem ser editadas.");
                return;
            }
            Cliente cliente = clienteDao.buscarPorId(entrega.getCliente().getId());
            if (cliente == null) {
                redirectErro(request, response, false, codigo, "Cliente da entrega não encontrado.");
                return;
            }
            entrega.setCliente(cliente);
            request.setAttribute("entrega", entrega);
            request.setAttribute("produtos", produtoDao.listar());
            request.getRequestDispatcher("/form-entrega.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            redirectErro(request, response, false, codigo, "Não foi possível consultar a entrega. Tente novamente.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        boolean editando = "/entregas/editar".equals(request.getServletPath());
        String codigo = request.getParameter("codigo");

        try {
            Entrega existente = null;
            Cliente cliente;
            if (editando) {
                if (codigo == null || codigo.isBlank()) {
                    redirectErro(request, response, true, codigo, "Código de rastreio obrigatório.");
                    return;
                }
                existente = entregaDao.buscarPorCodigo(codigo.trim());
                if (existente == null) {
                    redirectErro(request, response, false, codigo, "Entrega não encontrada.");
                    return;
                }
                if (!"Pendente".equalsIgnoreCase(existente.getStatus())) {
                    redirectErro(request, response, false, codigo, "Somente entregas Pendentes podem ser editadas.");
                    return;
                }
                cliente = clienteDao.buscarPorId(existente.getCliente().getId());
            } else {
                int clienteId = Integer.parseInt(request.getParameter("clienteId"));
                cliente = clienteDao.buscarPorId(clienteId);
            }

            if (cliente == null) {
                redirectErro(request, response, editando, codigo, "Cliente não encontrado.");
                return;
            }

            int produtoId = Integer.parseInt(request.getParameter("produtoId"));
            int quantidade = Integer.parseInt(request.getParameter("quantidade"));
            int destinoId = Integer.parseInt(request.getParameter("destinoId"));
            Produto produto = produtoDao.buscarPorId(produtoId);
            if (produto == null) {
                redirectErro(request, response, editando, codigo, "Produto não encontrado.");
                return;
            }
            if (quantidade <= 0) {
                redirectErro(request, response, editando, codigo, "Quantidade deve ser maior que zero.");
                return;
            }

            String estado = CodRastreioGen.paraSigla(request.getParameter("origemEstado").trim());
            Endereco origem = new Endereco(
                estado,
                request.getParameter("origemCidade").trim(),
                request.getParameter("origemCep").replaceAll("\\D", ""),
                request.getParameter("origemRua").trim(),
                request.getParameter("origemNumero").trim()
            );
            List<String> errosEndereco = enderecoValid.validar(origem);
            if (!errosEndereco.isEmpty()) {
                redirectErro(request, response, editando, codigo, String.join("|", errosEndereco));
                return;
            }

            Endereco destino = null;
            if (cliente.getEnderecos() != null) {
                for (Endereco endereco : cliente.getEnderecos()) {
                    if (endereco.getId() == destinoId) {
                        destino = endereco;
                        break;
                    }
                }
            }
            if (destino == null) {
                redirectErro(request, response, editando, codigo, "O endereço de destino não pertence ao cliente selecionado.");
                return;
            }
            if (enderecosIguais(origem, destino)) {
                redirectErro(request, response, editando, codigo, "Origem e destino devem ser diferentes.");
                return;
            }

            if (editando) {
                Entrega atualizada = new Entrega(existente.getId(), existente.getIdRastreio(), cliente, origem, destino);
                atualizada.setStatus(existente.getStatus());
                atualizada.addProd(produto, quantidade);
                if (!entregaDao.atualizar(atualizada)) {
                    redirectErro(request, response, false, codigo, "Somente entregas Pendentes podem ser editadas.");
                    return;
                }
                redirect(request, response, "/lista-entregas", "sucesso", "Entrega atualizada com sucesso.");
                return;
            }

            String uf = CodRastreioGen.paraSigla(destino.getEstado());
            Set<String> existentes = entregaDao.codigosPorUf(uf);
            String codigoRastreio = CodRastreioGen.gerar(uf, existentes);
            Entrega entrega = new Entrega(codigoRastreio, cliente, origem, destino);
            entrega.addProd(produto, quantidade);
            entregaDao.salvar(entrega);
            redirect(request, response, "/lista-entregas", "sucesso", "Entrega cadastrada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            if ("23503".equals(e.getSQLState())) {
                redirectErro(request, response, editando, codigo, "Não foi possível salvar a entrega por causa de um vínculo inválido.");
            } else {
                redirectErro(request, response, editando, codigo, "Não foi possível salvar a entrega. Tente novamente.");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            redirectErro(request, response, editando, codigo, "Formato inválido. Confira os dados informados.");
        }
    }

    private boolean enderecosIguais(Endereco origem, Endereco destino) {
        return origem.getRua().trim().equalsIgnoreCase(destino.getRua().trim())
                && origem.getNumero().trim().equalsIgnoreCase(destino.getNumero().trim())
                && origem.getCidade().trim().equalsIgnoreCase(destino.getCidade().trim())
                && origem.getEstado().trim().equalsIgnoreCase(destino.getEstado().trim())
                && origem.getCep().replaceAll("\\D", "").equals(destino.getCep().replaceAll("\\D", ""));
    }

    private void redirectErro(HttpServletRequest request, HttpServletResponse response, boolean editando, String codigo, String mensagem)
            throws IOException {
        String caminho = editando && codigo != null && !codigo.isBlank()
                ? "/entregas/editar?codigo=" + URLEncoder.encode(codigo.trim(), "UTF-8")
                : "/form-entrega.jsp";
        redirect(request, response, caminho, "erro", mensagem);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String caminho, String parametro, String mensagem)
            throws IOException {
        response.sendRedirect(request.getContextPath() + caminho + (caminho.contains("?") ? "&" : "?") + parametro + "="
                + URLEncoder.encode(mensagem, "UTF-8"));
    }
}