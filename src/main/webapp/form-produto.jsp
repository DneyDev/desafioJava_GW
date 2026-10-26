<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Produto" %>
<%@ page import="validation.ProdutoValid" %>

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
    Produto produto = (Produto) request.getAttribute("produto");
    boolean editando = produto != null;

    boolean formularioEnviado =
            Boolean.TRUE.equals(request.getAttribute("formSubmitted"));

    String nome = formularioEnviado
            ? request.getParameter("nome")
            : (editando ? produto.getProdName() : "");

    String descricao = formularioEnviado
            ? request.getParameter("descricao")
            : (editando ? produto.getDesc() : "");

    String preco = formularioEnviado
            ? request.getParameter("preco")
            : (editando ? produto.getPrice().toPlainString() : "");

    String peso = formularioEnviado
            ? request.getParameter("peso")
            : (editando ? produto.getPeso().toPlainString() : "");

    String unidade = formularioEnviado
            ? request.getParameter("unidade")
            : (editando ? produto.getUnidade() : "kg");
%>
<% request.setAttribute("paginaAtiva", editando ? "lista-produtos" : "produto"); %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><%= editando ? "Editar produto" : "Cadastrar produto" %> - VelozBR</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">
                    Home &gt; Produtos &gt;
                    <%= editando ? "Editar" : "Cadastrar" %>
                </div>

                <h1 class="page-title">
                    <%= editando ? "Editar produto" : "Cadastrar novo produto" %>
                </h1>
            </div>

            <% if (editando) { %>
                <a class="btn-secondary"
                   href="${pageContext.request.contextPath}/produtos">
                    Voltar para produtos
                </a>
            <% } %>
        </div>

        <%@ include file="/WEB-INF/partials/alerta.jspf" %>

        <div class="table-card form-card">
            <form action="${pageContext.request.contextPath}/produtos"
                  method="post">

                <% if (editando) { %>
                    <input type="hidden" name="id"
                           value="<%= produto.getCodigo() %>">
                <% } %>

                <h3>Dados do produto</h3>

                <p class="legenda-obrigatorio"><span>*</span> campo obrigatório</p>

                <div class="form-grid">
                    <div class="form-group span-all">
                        <label for="nome">Nome do produto</label>

                        <input id="nome"
                               type="text"
                               name="nome"
                               class="form-control"
                               maxlength="255"
                               value="<%= escHtml(nome) %>"
                               required>
                    </div>

                    <div class="form-group span-all">
                        <label for="descricao">Descrição</label>

                        <textarea id="descricao"
                                  name="descricao"
                                  class="form-control"
                                  rows="4"
                                  maxlength="255"
                                  required><%= escHtml(descricao) %></textarea>
                    </div>

                    <div class="form-group">
                        <label for="preco">Preço unitário (R$)</label>

                        <input id="preco"
                               type="number"
                               name="preco"
                               class="form-control"
                               step="0.01"
                               min="0.01"
                               value="<%= escHtml(preco) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="peso">Medida (peso ou volume)</label>

                        <input id="peso"
                               type="number"
                               name="peso"
                               class="form-control"
                               step="0.001"
                               min="0.001"
                               value="<%= escHtml(peso) %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="unidade">Unidade</label>

                        <select id="unidade" name="unidade"
                                class="form-control" required>
                            <% for (String u : ProdutoValid.UNIDADES_VALIDAS) { %>
                                <option value="<%= u %>"
                                    <%= u.equals(unidade) ? "selected" : "" %>>
                                    <%= u %>
                                </option>
                            <% } %>
                        </select>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-green">
                        <%= editando ? "Salvar alterações" : "Cadastrar produto" %>
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>