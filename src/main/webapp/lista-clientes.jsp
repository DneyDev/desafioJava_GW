
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Cliente" %>
<%!
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static String formatarDocumento(String doc) {
        if (doc == null) return "";
        String d = doc.replaceAll("\\D", "");

        if (d.length() == 11) {
            return d.substring(0, 3) + "." + d.substring(3, 6) + "."
                 + d.substring(6, 9) + "-" + d.substring(9);
        }
        if (d.length() == 14) {
            return d.substring(0, 2) + "." + d.substring(2, 5) + "."
                 + d.substring(5, 8) + "/" + d.substring(8, 12) + "-" + d.substring(12);
        }
        return doc;
    }
%>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>
<% request.setAttribute("paginaAtiva", "lista-clientes"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Clientes - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Clientes</div>
                <h1 class="page-title">Clientes cadastrados</h1>
            </div>
            <a class="btn-green" href="${pageContext.request.contextPath}/form-cliente.jsp">Cadastrar cliente</a>
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card">
            <h3>Todos os clientes</h3>
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Nome / Razão social</th><th>Tipo</th><th>Documento</th><th>E-mail</th><th>Endereços</th><th>Ações</th></tr></thead>
                    <tbody>
                        <% if (clientes != null && !clientes.isEmpty()) {
                               for (Cliente cliente : clientes) { %>
                            <tr>
                                <td><%= escHtml(cliente.getName()) %></td>
                                <td><%= cliente.getTipo() == null ? "" : escHtml(cliente.getTipo().name()) %></td>
                                <td><%= escHtml(formatarDocumento(cliente.getDocumento())) %></td>
                                <td><%= escHtml(cliente.getEmail()) %></td>
                                <td><%= cliente.getEnderecos() == null ? 0 : cliente.getEnderecos().size() %></td>
                                <td>
                                    <div class="acoes">
                                        <a class="btn-secondary"
                                           href="${pageContext.request.contextPath}/clientes?editar=<%= cliente.getId() %>">
                                            Editar
                                        </a>

                                        <form
                                            action="${pageContext.request.contextPath}/clientes"
                                            method="post"
                                            onsubmit="return confirm('Excluir permanentemente este cliente? A exclusão será bloqueada se ele possuir entregas vinculadas ou se algum de seus endereços for usado como destino de uma entrega.')">

                                            <input type="hidden"
                                                   name="acao"
                                                   value="excluir">

                                            <input type="hidden"
                                                   name="id"
                                                   value="<%= cliente.getId() %>">

                                            <button type="submit" class="btn-danger">
                                                Excluir
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <%     }
                           } else { %>
                            <tr><td colspan="6" class="empty-state">Nenhum cliente cadastrado.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>