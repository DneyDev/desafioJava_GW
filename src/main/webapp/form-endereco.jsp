<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Endereco" %>
<%@ page import="models.Cliente" %>
<%@ page import="db.ClienteDao" %>

<%!
    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    Endereco endereco = (Endereco) request.getAttribute("endereco");
    Cliente clienteSelecionado =
            (Cliente) request.getAttribute("clienteSelecionado");

    boolean editando = endereco != null;

    List<Cliente> clientes =
            (List<Cliente>) request.getAttribute("clientes");

    if (clientes == null && !editando) {
        try {
            clientes = new ClienteDao().listar();
        } catch (Exception e) {
            clientes = java.util.Collections.emptyList();
        }
    }

    int clienteId = clienteSelecionado != null
            ? clienteSelecionado.getId() : 0;

    String rua = editando ? endereco.getRua() : "";
    String numero = editando ? endereco.getNumero() : "";
    String cidade = editando ? endereco.getCidade() : "";
    String estado = editando ? endereco.getEstado() : "MG";
    String cep = editando ? endereco.getCep() : "";

    String clienteIdParam = request.getParameter("clienteId");

    if (!editando && clienteIdParam != null) {
        try {
            clienteId = Integer.parseInt(clienteIdParam);
        } catch (NumberFormatException ignored) {
            clienteId = 0;
        }
    }

    request.setAttribute("paginaAtiva",
            editando ? "lista-clientes" : "endereco");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><%= editando ? "Editar endereço" : "Cadastrar endereço" %> - VelozBR</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>

<%@ include file="/WEB-INF/partials/sidebar.jspf" %>

<div class="main-content">

    <div class="top-header">
        <div>
            <div class="breadcrumbs">
                Home &gt; Endereços &gt;
                <%= editando ? "Editar" : "Cadastrar" %>
            </div>

            <h1 class="page-title">
                <%= editando ? "Editar endereço" : "Cadastrar endereço" %>
            </h1>
        </div>

        <a class="btn-secondary"
           href="${pageContext.request.contextPath}/clientes">
            Voltar para clientes
        </a>
    </div>

    <%@ include file="/WEB-INF/partials/alerta.jspf" %>

    <div class="table-card form-card">

        <form action="${pageContext.request.contextPath}/enderecos"
              method="post">

            <% if (editando) { %>
                <input type="hidden" name="id"
                       value="<%= endereco.getId() %>">
            <% } %>

            <h3>Dados do endereço</h3>
            <p class="legenda-obrigatorio"><span>*</span> campo obrigatório</p>
            <div class="form-grid">

                <div class="form-group span-all">
                    <label for="clienteId">Cliente</label>

                    <select id="clienteId"
                            name="clienteId"
                            class="form-control"
                            required
                            <%= editando ? "disabled" : "" %>>

                        <option value="">Selecione um cliente</option>

                        <% if (clientes != null) {
                            for (Cliente cliente : clientes) { %>

                                <option value="<%= cliente.getId() %>"
                                    <%= cliente.getId() == clienteId
                                            ? "selected" : "" %>>
                                    <%= escHtml(cliente.getName()) %>
                                </option>

                        <%  }
                           } %>
                    </select>

                    <% if (editando && clienteSelecionado != null) { %>
                        <input type="hidden" name="clienteId"
                               value="<%= clienteSelecionado.getId() %>">
                    <% } %>
                </div>

                <div class="form-group span-all">
                    <label for="rua">Rua</label>

                    <input id="rua"
                           type="text"
                           name="rua"
                           class="form-control"
                           maxlength="255"
                           value="<%= escHtml(rua) %>"
                           required>
                </div>

                <div class="form-group">
                    <label for="numero">Número</label>

                    <input id="numero"
                           type="text"
                           name="numero"
                           class="form-control"
                           maxlength="10"
                           value="<%= escHtml(numero) %>"
                           required>
                </div>

                <div class="form-group">
                    <label for="cidade">Cidade</label>

                    <input id="cidade"
                           type="text"
                           name="cidade"
                           class="form-control"
                           maxlength="100"
                           value="<%= escHtml(cidade) %>"
                           required>
                </div>

                <div class="form-group">
                    <label for="estado">Estado (UF)</label>

                    <select id="estado"
                            name="estado"
                            class="form-control"
                            required>

                        <%
                            String[] ufs = {
                                "AC", "AL", "AP", "AM", "BA", "CE",
                                "DF", "ES", "GO", "MA", "MT", "MS",
                                "MG", "PA", "PB", "PR", "PE", "PI",
                                "RJ", "RN", "RS", "RO", "RR", "SC",
                                "SP", "SE", "TO"
                            };

                            for (String uf : ufs) {
                        %>
                            <option value="<%= uf %>"
                                <%= uf.equalsIgnoreCase(estado)
                                        ? "selected" : "" %>>
                                <%= uf %>
                            </option>
                        <% } %>

                    </select>
                </div>

                <div class="form-group">
                    <label for="cep">CEP</label>

                    <input id="cep"
                           type="text"
                           name="cep"
                           class="form-control"
                           maxlength="9"
                           inputmode="numeric"
                           pattern="([0-9]{8}|[0-9]{5}-[0-9]{3})"
                           title="Informe o CEP com 8 números, com ou sem hífen (ex: 50000-000)."
                           value="<%= escHtml(cep) %>"
                           required>
                </div>

            </div>

            <div class="form-actions">
                <button type="submit" class="btn-green">
                    <%= editando ? "Salvar alterações" : "Cadastrar endereço" %>
                </button>
            </div>

        </form>

    </div>
</div>

</body>
</html>