<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Entrega" %>
<%@ page import="models.ItemEntrega" %>
<%@ page import="db.EntregaDao" %>
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
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gestão de Entregas - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <div>
            <div class="logo">🚚 VelozBR</div>
            <ul class="menu">
                <li><a href="${pageContext.request.contextPath}/index.jsp">📊 Painel</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/lista-entregas">📦 Gestão de Entregas</a></li>
                <li><a href="${pageContext.request.contextPath}/form-entrega.jsp">➕ Nova Entrega</a></li>
                <li><a href="${pageContext.request.contextPath}/form-cliente.jsp">👤 Cadastrar Cliente</a></li>
                <li><a href="${pageContext.request.contextPath}/form-produto.jsp">🏷️ Cadastrar Produto</a></li>
            </ul>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home > Entregas</div>
                <h1 class="page-title">Gestão de Entregas</h1>
            </div>
            <a href="${pageContext.request.contextPath}/form-entrega.jsp" class="btn-green">+ Nova entrega</a>
        </div>

        <div class="table-card">
            <h3>Todas as Entregas</h3>
            <table>
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Cliente</th>
                        <th>Produtos / Itens</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Alterar Status</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (lista != null && !lista.isEmpty()) {
                            for (Entrega e : lista) {
                                String badgeClass = "badge-pendente";
                                if ("Entregue".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-entregue";
                                else if ("Em Trânsito".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-transito";
                                else if ("Cancelado".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-cancelado";
                    %>
                        <tr>
                            <td><strong style="color: var(--primary-green);"><%= e.getIdRastreio() %></strong></td>
                            <td>
                                <% if (e.getCliente() != null) { %>
                                    <%= e.getCliente().getName() %>
                                <% } else { %>
                                    <em>N/A</em>
                                <% } %>
                            </td>
                            <td>
                                <% 
                                    if (e.getItens() != null && !e.getItens().isEmpty()) {
                                        for (ItemEntrega item : e.getItens()) { 
                                %>
                                    <div><%= item.getQuantidade() %>x <%= item.getProduto() != null ? item.getProduto().getProdName() : "Produto" %></div>
                                <% 
                                        } 
                                    } else { 
                                %>
                                    <span style="color: var(--text-muted);">-</span>
                                <%  } %>
                            </td>
                            <td>R$ <%= String.format("%.2f", e.calcularTotal()) %></td>
                            <td><span class="badge <%= badgeClass %>"><%= e.getStatus() %></span></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/atualizar-status" method="post" style="display:flex; gap:6px; align-items:center;">
                                    <input type="hidden" name="codigoRastreio" value="<%= e.getIdRastreio() %>">
                                    <select name="novoStatus" class="form-control" style="padding: 4px 8px; font-size:12px; width:auto;">
                                        <option value="Pendente" <%= "Pendente".equalsIgnoreCase(e.getStatus()) ? "selected":"" %>>Pendente</option>
                                        <option value="Em Trânsito" <%= "Em Trânsito".equalsIgnoreCase(e.getStatus()) ? "selected":"" %>>Em Trânsito</option>
                                        <option value="Entregue" <%= "Entregue".equalsIgnoreCase(e.getStatus()) ? "selected":"" %>>Entregue</option>
                                        <option value="Cancelado" <%= "Cancelado".equalsIgnoreCase(e.getStatus()) ? "selected":"" %>>Cancelado</option>
                                    </select>
                                    <button type="submit" class="btn-green" style="padding: 5px 10px; font-size:12px;">OK</button>
                                </form>
                            </td>
                        </tr>
                    <%      } 
                        } else { 
                    %>
                        <tr><td colspan="6" style="text-align:center; padding: 30px;">Nenhuma entrega registrada.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>