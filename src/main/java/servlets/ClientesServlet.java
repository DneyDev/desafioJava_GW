
package servlets;

import java.io.IOException;
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
import models.Endereco;
import models.TipoCliente;
import validation.ClienteValid;

@WebServlet("/clientes")
public class ClientesServlet extends HttpServlet {

    private final ClienteDao dao = new ClienteDao();
    private final ClienteValid clienteValid = new ClienteValid();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String editar = request.getParameter("editar");

            if (editar != null && !editar.isBlank()) {
                int id = Integer.parseInt(editar);
                Cliente cliente = dao.buscarPorId(id);
                if (cliente == null) {
                    redirect(request, response, "/clientes",
                            "erro", "Cliente não encontrado.");
                    return;
                }
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("/form-cliente.jsp")
                       .forward(request, response);
                return;
            }
            request.setAttribute("clientes", dao.listar());
            request.getRequestDispatcher("/lista-clientes.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            redirect(request, response, "/form-cliente.jsp",
                    "erro",
                    "Não foi possível consultar os clientes. Tente novamente.");
        } catch (IllegalArgumentException | NullPointerException e) {
            redirect(request, response, "/form-cliente.jsp",
                    "erro", "Identificador de cliente inválido.");
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
        Cliente existente = null;

        try {
            if ("excluir".equals(acao)) {
                id = Integer.parseInt(idParam);

                if (!dao.excluir(id)) {
                    redirect(request, response, "/clientes",
                            "erro", "Cliente não encontrado.");
                    return;
                }
                redirect(request, response, "/clientes",
                        "sucesso", "Cliente excluído com sucesso.");
                return;
            }

            List<Endereco> enderecos = new ArrayList<>();
            if (editando) {
                id = Integer.parseInt(idParam);
                existente = dao.buscarPorId(id);
                if (existente == null) {
                    redirect(request, response, "/clientes",
                            "erro", "Cliente não encontrado.");
                    return;
                }
                if (existente.getEnderecos() != null) {
                    enderecos.addAll(existente.getEnderecos());
                }
            } else {
                String estado = request.getParameter("estado").trim();
                String cidade = request.getParameter("cidade").trim();
                String cep = request.getParameter("cep");
                cep = cep == null ? "" : cep.trim();
                String rua = request.getParameter("rua").trim();
                String numero = request.getParameter("numero").trim();
                String bairro = request.getParameter("bairro").trim();
                enderecos.add(
                    new Endereco(estado, cidade, cep, rua, numero, bairro)
                );
            }

            TipoCliente tipo = TipoCliente.valueOf(
                request.getParameter("tipo").trim().toUpperCase()
            );
            String nome = request.getParameter("nome").trim();
            String documento = request.getParameter("documento")
                                      .replaceAll("\\D", "");
            String email = request.getParameter("email")
                                  .trim()
                                  .toLowerCase();
            Cliente cliente = new Cliente(
                id, nome, tipo, documento, email, enderecos
            );

            List<String> erros = new ArrayList<>();
            if (editando && enderecos.isEmpty()) {
                adicionarErro(erros,
                        clienteValid.validarNome(nome, tipo));

                adicionarErro(erros,
                        clienteValid.validarDocumento(tipo, documento));

                adicionarErro(erros,
                        clienteValid.validarEmail(email));
            } else {
                erros = clienteValid.validar(cliente);
            }

            if (!erros.isEmpty()) {
                mostrarFormularioComErro(
                    request, response, editando, existente,
                    String.join("|", erros)
                );
                return;
            }

            for (Endereco endereco : enderecos) {
                if (endereco.getCep() != null) {
                    endereco.setCep(
                        endereco.getCep().replaceAll("\\D", "")
                    );
                }
            }

            if (editando) {
                if (dao.existePorDocumentoExceto(documento, id)) {
                    mostrarFormularioComErro(
                        request, response, true, existente,
                        "Já existe outro cliente cadastrado com esse CPF/CNPJ."
                    );
                    return;
                }

                if (dao.existePorEmailExceto(email, id)) {
                    mostrarFormularioComErro(
                        request, response, true, existente,
                        "Já existe outro cliente cadastrado com esse e-mail."
                    );
                    return;
                }

                if (!dao.atualizar(cliente)) {
                    redirect(request, response, "/clientes",
                            "erro", "Cliente não encontrado.");
                    return;
                }

                redirect(request, response, "/clientes",
                        "sucesso", "Cliente atualizado com sucesso.");
                return;
            }

            if (dao.existePorDocumento(documento)) {
                mostrarFormularioComErro(
                    request, response, false, null,
                    "Já existe um cliente cadastrado com esse CPF/CNPJ."
                );
                return;
            }

            if (dao.existePorEmail(email)) {
                mostrarFormularioComErro(
                    request, response, false, null,
                    "Já existe um cliente cadastrado com esse e-mail."
                );
                return;
            }

            dao.inserir(cliente);

            redirect(request, response, "/form-cliente.jsp",
                    "sucesso", "Cliente cadastrado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            if ("excluir".equals(acao)) {
                redirect(request, response, "/clientes",
                        "erro",
                        "23503".equals(e.getSQLState())
                            ? "Este cliente possui entregas vinculadas ou um de seus endereços é usado como destino. Não é possível excluí-lo para preservar o histórico."
                            : "Não foi possível excluir o cliente. Tente novamente.");
            } else if ("23505".equals(e.getSQLState())) {
                mostrarFormularioComErro(
                    request, response, editando, existente,
                    "Já existe um cliente com esse CPF/CNPJ ou e-mail."
                );
            } else {
                mostrarFormularioComErro(
                    request, response, editando, existente,
                    "Não foi possível salvar o cliente. Tente novamente."
                );
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            if ("excluir".equals(acao)) {
                redirect(request, response, "/clientes",
                        "erro", "Identificador de cliente inválido.");
                return;
            }

            mostrarFormularioComErro(
                request, response, editando, existente,
                "Formato inválido. Confira os dados informados."
            );
        }
    }

    private void mostrarFormularioComErro(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean editando,
            Cliente existente,
            String mensagem)
            throws ServletException, IOException {
        request.setAttribute("formSubmitted", true);
        request.setAttribute("erro", mensagem);
        if (editando && existente != null) {
            request.setAttribute("cliente", existente);
        } else {
            request.removeAttribute("cliente");
        }
        request.getRequestDispatcher("/form-cliente.jsp")
               .forward(request, response);
    }

    private void adicionarErro(List<String> erros, String erro) {
        if (erro != null) {
            erros.add("Erro: " + erro);
        }
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