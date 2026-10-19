<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Entrega" %>
<%@ page import="models.ItemEntrega" %>
<%@ page import="db.EntregaDao" %>
<%@ page import="validation.EntregaValid" %>
<%
    // Se a lista não vier da Servlet (ex: acesso direto ao arquivo JSP), busca no banco diretamente
    List<Entrega> lista = (List<Entrega>) request.getAttribute("listaEntregas");
    if (lista == null) {
        try {
            EntregaDao dao = new EntregaDao();
            lista = dao.listarTodas();
        } catch (Exception ex) {
            lista = java.util.Collections.emptyList();
        }
    }
%>
<% request.setAttribute("paginaAtiva", "entregas"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Histórico de Entregas - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Entregas</div>
                <h1 class="page-title">Histórico de Entregas</h1>
            </div>
            <a href="${pageContext.request.contextPath}/form-entrega.jsp" class="btn-green"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 5v14M5 12h14"/></svg>Nova entrega</a>
        </div>
        <div class="table-card">
            <h3>Todas as Entregas</h3>
            <div class="table-scroll">
            <table>
                <thead>
                    <tr><th>Código</th><th>Cliente</th><th>Produtos / Itens</th><th>Total</th><th>Status</th><th>Alterar Status</th></tr>
                </thead>
                <tbody>
                    <% if (lista != null && !lista.isEmpty()) {
                        for (Entrega e : lista) {
                            String badgeClass = "badge-pendente";
                            if ("Entregue".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-entregue";
                            else if ("Em transito".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-transito";
                            else if ("Cancelada".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-cancelado";
                    %>
                        <tr>
                            <td class="codigo"><%= e.getIdRastreio() %></td>
                            <td><%= e.getCliente() != null ? e.getCliente().getName() : "N/A" %></td>
                            <td>
                                <% if (e.getItens() != null && !e.getItens().isEmpty()) {
                                       for (ItemEntrega item : e.getItens()) { %>
                                    <div><%= item.getQuantidade() %>x <%= item.getProduto() != null ? item.getProduto().getProdName() : "Produto" %></div>
                                <%     }
                                   } else { %>
                                    <span>-</span>
                                <% } %>
                            </td>
                            <td>R$ <%= String.format("%.2f", e.calcularTotal()) %></td>
                            <td><span class="badge <%= badgeClass %>"><%= e.getStatus() %></span></td>
                            <td>
                                <form class="status-form" action="${pageContext.request.contextPath}/atualizar-status" method="post">
                                    <input type="hidden" name="codigoRastreio" value="<%= e.getIdRastreio() %>">
                                    <select name="novoStatus" class="form-control" aria-label="Novo status">
                                        <% for (String s : EntregaValid.STATUS_VALIDOS) { %>
                                            <option value="<%= s %>" <%= s.equalsIgnoreCase(e.getStatus()) ? "selected" : "" %>><%= s %></option>
                                        <% } %>
                                    </select>
                                    <button type="submit" class="btn-green">OK</button>
                                </form>
                            </td>
                        </tr>
                    <% } } else { %>
                        <tr><td colspan="6" style="text-align:center; padding: 30px;">Nenhuma entrega registrada.</td></tr>
                    <% } %>
                </tbody>
            </table>
            </div>
        </div>
    </div>

</body>
</html>