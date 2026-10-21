
package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ProdutoDao;
import models.Produto;
import validation.ProdutoValid;

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDao dao = new ProdutoDao();
    private final ProdutoValid produtoValid = new ProdutoValid();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String editar = request.getParameter("editar");

            if (editar != null && !editar.isBlank()) {
                int id = Integer.parseInt(editar);
                Produto produto = dao.buscarPorId(id);

                if (produto == null) {
                    redirect(request, response, "/produtos",
                            "erro", "Produto não encontrado.");
                    return;
                }

                request.setAttribute("produto", produto);

                request.getRequestDispatcher("/form-produto.jsp")
                       .forward(request, response);
                return;
            }

            request.setAttribute("produtos", dao.listar());

            request.getRequestDispatcher("/lista-produtos.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();

            redirect(request, response, "/form-produto.jsp",
                    "erro",
                    "Não foi possível consultar os produtos. Tente novamente.");

        } catch (IllegalArgumentException | NullPointerException e) {
            redirect(request, response, "/form-produto.jsp",
                    "erro", "Identificador de produto inválido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");
        String idParam = request.getParameter("id");

        int id = 0;
        boolean editando = idParam != null && !idParam.isBlank();

        try {
            if ("excluir".equals(acao)) {
                id = Integer.parseInt(idParam);

                if (dao.buscarPorId(id) == null || !dao.excluir(id)) {
                    redirect(request, response, "/produtos",
                            "erro", "Produto não encontrado.");
                    return;
                }

                redirect(request, response, "/produtos",
                        "sucesso", "Produto excluído com sucesso.");
                return;
            }

            if (editando) {
                id = Integer.parseInt(idParam);

                if (dao.buscarPorId(id) == null) {
                    redirect(request, response, "/produtos",
                            "erro", "Produto não encontrado.");
                    return;
                }
            }

            String nome = request.getParameter("nome").trim();
            String descricao = request.getParameter("descricao").trim();

            BigDecimal preco = new BigDecimal(
                request.getParameter("preco").trim()
            );

            BigDecimal peso = new BigDecimal(
                request.getParameter("peso").trim()
            );

            Produto produto = new Produto(
                id, nome, descricao, preco, peso
            );

            List<String> erros = produtoValid.validar(produto);

            if (!erros.isEmpty()) {
                mostrarFormularioComErro(
                    request, response, editando, id,
                    String.join("|", erros)
                );
                return;
            }

            if (editando
                    ? dao.existePorNomeExceto(nome, id)
                    : dao.existePorNome(nome)) {

                mostrarFormularioComErro(
                    request, response, editando, id,
                    "Já existe outro produto cadastrado com esse nome."
                );
                return;
            }

            if (editando) {
                if (!dao.atualizar(produto)) {
                    redirect(request, response, "/produtos",
                            "erro", "Produto não encontrado.");
                    return;
                }

                redirect(request, response, "/produtos",
                        "sucesso", "Produto atualizado com sucesso.");

            } else {
                dao.inserir(produto);

                redirect(request, response, "/form-produto.jsp",
                        "sucesso", "Produto cadastrado com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            if ("excluir".equals(acao) && "23503".equals(e.getSQLState())) {
                redirect(request, response, "/produtos",
                        "erro",
                        "Este produto já foi usado em entregas e não pode ser excluído.");

            } else if ("23505".equals(e.getSQLState())) {
                mostrarFormularioComErro(
                    request, response, editando, id,
                    "Já existe outro produto cadastrado com esse nome."
                );

            } else if ("excluir".equals(acao)) {
                redirect(request, response, "/produtos",
                        "erro",
                        "Não foi possível excluir o produto. Tente novamente.");

            } else {
                mostrarFormularioComErro(
                    request, response, editando, id,
                    "Não foi possível salvar o produto. Tente novamente."
                );
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            mostrarFormularioComErro(
                request, response, editando, id,
                "Formato inválido. Confira os dados informados."
            );
        }
    }

    private void mostrarFormularioComErro(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean editando,
            int id,
            String mensagem)
            throws ServletException, IOException {

        request.setAttribute("formSubmitted", true);
        request.setAttribute("erro", mensagem);

        if (editando && id > 0) {
            try {
                Produto produto = dao.buscarPorId(id);

                if (produto != null) {
                    request.setAttribute("produto", produto);
                }

            } catch (SQLException e) {
                throw new ServletException(
                    "Não foi possível recuperar o produto para exibir o formulário.",
                    e
                );
            }
        } else {
            request.removeAttribute("produto");
        }

        request.getRequestDispatcher("/form-produto.jsp")
               .forward(request, response);
    }

    private void redirect(
            HttpServletRequest request,
            HttpServletResponse response,
            String caminho,
            String parametro,
            String mensagem)
            throws IOException {

        response.sendRedirect(
            request.getContextPath()
                + caminho
                + (caminho.contains("?") ? "&" : "?")
                + parametro
                + "="
                + URLEncoder.encode(mensagem, "UTF-8")
        );
    }
}
