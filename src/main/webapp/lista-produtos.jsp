<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="models.Produto" %>
<%!
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
    private static String escJs(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"")
                .replace("\r", "\\r").replace("\n", "\\n").replace("</", "<\\/");
    }
%>
<%
    List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
%>
<% request.setAttribute("paginaAtiva", "lista-produtos"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Produtos - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Produtos</div>
                <h1 class="page-title">Produtos cadastrados</h1>
            </div>
            <a class="btn-green" href="${pageContext.request.contextPath}/form-produto.jsp">Cadastrar produto</a>
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card">
            <h3>Todos os produtos</h3>
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Nome</th><th>Descrição</th><th>Preço</th><th>Peso</th><th>Ações</th></tr></thead>
                    <tbody>
                        <% if (produtos != null && !produtos.isEmpty()) {
                               for (Produto produto : produtos) { %>
                            <tr>
                                <td><%= escHtml(produto.getProdName()) %></td>
                                <td class="descricao-celula"><%= escHtml(produto.getDesc()) %></td>
                                <td>R$ <%= String.format(new Locale("pt", "BR"), "%.2f", produto.getPrice()) %></td>
                                <td><%= String.format(new Locale("pt", "BR"), "%.3f", produto.getPeso()) %> kg</td>
                                <td>
                                    <div class="acoes">
                                        <a class="btn-secondary" href="${pageContext.request.contextPath}/produtos?editar=<%= produto.getCodigo() %>">Editar</a>
                                        <form action="${pageContext.request.contextPath}/produtos" method="post" onsubmit="return confirm('Excluir o produto <%= escHtml(escJs(produto.getProdName())) %>?')">
                                            <input type="hidden" name="acao" value="excluir">
                                            <input type="hidden" name="id" value="<%= produto.getCodigo() %>">
                                            <button type="submit" class="btn-danger">Excluir</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <%     }
                           } else { %>
                            <tr><td colspan="5" class="empty-state">Nenhum produto cadastrado.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>