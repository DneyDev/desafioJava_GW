
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Cliente" %>
<%@ page import="models.Endereco" %>
<%@ page import="models.Entrega" %>
<%@ page import="models.ItemEntrega" %>
<%@ page import="models.Produto" %>
<%@ page import="db.ClienteDao" %>
<%@ page import="db.ProdutoDao" %>
<%!
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
%>
<%
    Entrega entrega = (Entrega) request.getAttribute("entrega");
    boolean editando = entrega != null;

    List<Cliente> clientes =
            (List<Cliente>) request.getAttribute("clientes");
    List<Produto> produtos =
            (List<Produto>) request.getAttribute("produtos");

    if (clientes == null && !editando) {
        try {
            clientes = new ClienteDao().listar();
        } catch (Exception e) {
            clientes = java.util.Collections.emptyList();
        }
    }

    if (produtos == null) {
        try {
            produtos = new ProdutoDao().listar();
        } catch (Exception e) {
            produtos = java.util.Collections.emptyList();
        }
    }

    Cliente clienteAtual = editando ? entrega.getCliente() : null;
    Cliente remetenteAtual = editando ? entrega.getRemetente() : null;
    Endereco origemAtual = editando ? entrega.getOrigem() : null;
    Endereco destinoAtual = editando ? entrega.getDestino() : null;

    int produtoAtualId = -1;
    int quantidadeAtual = 1;

    if (editando && entrega.getItens() != null
            && !entrega.getItens().isEmpty()) {
        ItemEntrega item = entrega.getItens().get(0);

        if (item.getProduto() != null) {
            produtoAtualId = item.getProduto().getCodigo();
        }

        quantidadeAtual = item.getQuantidade();
    }
%>
<% request.setAttribute("paginaAtiva",
        editando ? "entregas" : "nova-entrega"); %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= editando ? "Editar entrega" : "Nova entrega" %> - VelozBR</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">
                    Home &gt; Entregas &gt;
                    <%= editando ? "Editar" : "Nova entrega" %>
                </div>
                <h1 class="page-title">
                    <%= editando ? "Editar entrega" : "Registrar nova entrega" %>
                </h1>
            </div>

            <% if (editando) { %>
                <a class="btn-secondary"
                   href="${pageContext.request.contextPath}/lista-entregas">
                    Voltar para entregas
                </a>
            <% } %>
        </div>

        <%@ include file="/WEB-INF/partials/alerta.jspf" %>

        <div class="table-card form-card">
            <h3>Dados da entrega</h3>
            <p class="legenda-obrigatorio">
                <span>*</span> campo obrigatório
            </p>

            <form action="${pageContext.request.contextPath}<%= editando
                    ? "/entregas/editar" : "/lancar-entrega" %>"
                  method="post">

                <% if (editando) { %>
                    <input type="hidden" name="codigo"
                           value="<%= escHtml(entrega.getIdRastreio().trim()) %>">

                    <div class="form-group">
                        <label>Cliente destinatário</label>
                        <input type="text" class="form-control"
                               value="<%= escHtml(clienteAtual.getName()) %>"
                               readonly>
                    </div>
                <% } else { %>
                    <div class="form-group">
                        <label for="clienteId">Cliente destinatário</label>
                        <select id="clienteId" name="clienteId"
                                class="form-control" required>
                            <option value="">Selecione um cliente</option>
                            <% for (Cliente cliente : clientes) { %>
                                <option value="<%= cliente.getId() %>">
                                    <%= escHtml(cliente.getName()) %>
                                </option>
                            <% } %>
                        </select>
                    </div>
                <% } %>

                <!-- Dados do remetente: campos exigidos pelo EntregaServlet -->
                <h3 class="form-section">Dados do remetente</h3>

                <div class="form-grid">
                    <div class="form-group">
                        <label for="origemTipo">
                            Tipo de pessoa <span>*</span>
                        </label>

                        <select id="origemTipo" name="origemTipo"
                                class="form-control" required>
                            <option value="PF"
                                <%= remetenteAtual != null
                                    && "PF".equals(remetenteAtual.getTipo().name())
                                    ? "selected" : "" %>>
                                Pessoa física (CPF)
                            </option>
                            <option value="PJ"
                                <%= remetenteAtual != null
                                    && "PJ".equals(remetenteAtual.getTipo().name())
                                    ? "selected" : "" %>>
                                Pessoa jurídica (CNPJ)
                            </option>
                        </select>
                    </div>

                    <div class="form-group span-all">
                        <label for="origemNome">
                            Nome / razão social <span>*</span>
                        </label>
                        <input id="origemNome" name="origemNome"
                               type="text" class="form-control"
                               maxlength="255"
                               value="<%= remetenteAtual == null
                                   ? "" : escHtml(remetenteAtual.getName()) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="origemDocumento">
                            CPF / CNPJ <span>*</span>
                        </label>
                        <input id="origemDocumento" name="origemDocumento"
                               type="text" class="form-control"
                               maxlength="18"
                               value="<%= remetenteAtual == null
                                   ? "" : escHtml(remetenteAtual.getDocumento()) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="origemEmail">
                            E-mail do remetente <span>*</span>
                        </label>
                        <input id="origemEmail" name="origemEmail"
                               type="email" class="form-control"
                               maxlength="255"
                               value="<%= remetenteAtual == null
                                   ? "" : escHtml(remetenteAtual.getEmail()) %>"
                               required>
                    </div>
                </div>

                <h3 class="form-section">Endereço de origem</h3>

                <div class="form-grid">
                    <div class="form-group span-all">
                        <label for="origemRua">Rua</label>
                        <input id="origemRua" name="origemRua"
                               type="text" class="form-control"
                               maxlength="255"
                               value="<%= origemAtual == null
                                   ? "" : escHtml(origemAtual.getRua()) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="origemNumero">Número</label>
                        <input id="origemNumero" name="origemNumero"
                               type="text" class="form-control"
                               maxlength="10"
                               value="<%= origemAtual == null
                                   ? "" : escHtml(origemAtual.getNumero()) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="origemCidade">Cidade</label>
                        <input id="origemCidade" name="origemCidade"
                               type="text" class="form-control"
                               maxlength="100"
                               value="<%= origemAtual == null
                                   ? "" : escHtml(origemAtual.getCidade()) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="origemEstado">UF</label>
                        <select id="origemEstado" name="origemEstado"
                                class="form-control" required>
                            <% String[] ufs = {
                                "AC","AL","AP","AM","BA","CE","DF","ES",
                                "GO","MA","MT","MS","MG","PA","PB","PR",
                                "PE","PI","RJ","RN","RS","RO","RR","SC",
                                "SP","SE","TO"
                            };

                            for (String uf : ufs) { %>
                                <option value="<%= uf %>"
                                    <%= origemAtual != null
                                        && uf.equalsIgnoreCase(
                                            origemAtual.getEstado())
                                        ? "selected" : "" %>>
                                    <%= uf %>
                                </option>
                            <% } %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="origemCep">CEP</label>
                        <input id="origemCep" name="origemCep"
                               type="text" class="form-control"
                               maxlength="9"
                               value="<%= origemAtual == null
                                   ? "" : escHtml(origemAtual.getCep()) %>"
                               required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="destinoId">Endereço de destino</label>
                    <select id="destinoId" name="destinoId"
                            class="form-control" required>
                        <% if (!editando) { %>
                            <option value="">Selecione um cliente primeiro</option>

                            <% for (Cliente cliente : clientes) {
                                if (cliente.getEnderecos() == null) continue;

                                for (Endereco endereco : cliente.getEnderecos()) { %>
                                    <option value="<%= endereco.getId() %>"
                                            data-cliente-id="<%= cliente.getId() %>">
                                        <%= escHtml(endereco.getEndCompleto()) %>
                                    </option>
                                <% }
                            }
                        } else if (clienteAtual.getEnderecos() != null) {
                            for (Endereco endereco : clienteAtual.getEnderecos()) { %>
                                <option value="<%= endereco.getId() %>"
                                    <%= destinoAtual != null
                                        && destinoAtual.getId() == endereco.getId()
                                        ? "selected" : "" %>>
                                    <%= escHtml(endereco.getEndCompleto()) %>
                                </option>
                            <% }
                        } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="produtoId">Produto</label>
                    <select id="produtoId" name="produtoId"
                            class="form-control" required>
                        <option value="">Selecione um produto</option>

                        <% for (Produto produto : produtos) { %>
                            <option value="<%= produto.getCodigo() %>"
                                <%= produtoAtualId == produto.getCodigo()
                                    ? "selected" : "" %>>
                                <%= escHtml(produto.getProdName()) %>
                            </option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="quantidade">Quantidade</label>
                    <input id="quantidade" name="quantidade"
                           type="number" class="form-control"
                           value="<%= quantidadeAtual %>"
                           min="1" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-green">
                        <%= editando ? "Salvar alterações" : "Salvar entrega" %>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <% if (!editando) { %>
        <script>
            (function () {
                var clienteSel = document.getElementById('clienteId');
                var destinoSel = document.getElementById('destinoId');
                var placeholder = destinoSel.options[0];
                var todosDestinos =
                    Array.prototype.slice.call(destinoSel.options, 1);

                function atualizarDestinos() {
                    var clienteId = clienteSel.value;

                    var compativeis = todosDestinos.filter(function (opt) {
                        return clienteId !== ''
                            && opt.getAttribute('data-cliente-id') === clienteId;
                    });

                    while (destinoSel.firstChild) {
                        destinoSel.removeChild(destinoSel.firstChild);
                    }

                    placeholder.textContent = clienteId === ''
                        ? 'Selecione um cliente primeiro'
                        : compativeis.length === 0
                            ? 'Cliente sem endereço cadastrado'
                            : 'Selecione o endereço de destino';

                    destinoSel.appendChild(placeholder);

                    compativeis.forEach(function (opt) {
                        destinoSel.appendChild(opt);
                    });

                    destinoSel.selectedIndex = 0;
                }

                clienteSel.addEventListener('change', atualizarDestinos);
                atualizarDestinos();
            })();
        </script>
    <% } %>
</body>
</html>
