<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Entrega" %>
<%@ page import="db.EntregaDao" %>
<%@ page import="db.ClienteDao" %>
<%@ page import="db.ProdutoDao" %>
<%
    EntregaDao entregaDao = new EntregaDao();
    ClienteDao clienteDao = new ClienteDao();
    ProdutoDao produtoDao = new ProdutoDao();
    
    List<Entrega> entregas = null;
    int emAndamento = 0, concluida = 0, totalClientes = 0, totalProdutos = 0;
    
    try {
        entregas = entregaDao.listarTodas();
        if (entregas != null) {
            for (Entrega e : entregas) {
                if ("Entregue".equalsIgnoreCase(e.getStatus())) concluida++;
                else emAndamento++;
            }
        }
        totalClientes = clienteDao.listar().size();
        totalProdutos = produtoDao.listar().size();
    } catch (Exception e) {
        entregas = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel Operacional - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <div>
            <div class="logo">🚚 VelozBR</div>
            <ul class="menu">
                <li class="active"><a href="${pageContext.request.contextPath}/index.jsp">📊 Painel</a></li>
                <li><a href="${pageContext.request.contextPath}/lista-entregas">📦 Gestão de Entregas</a></li>
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
                <div class="breadcrumbs">Home > Painel</div>
                <h1 class="page-title">Painel Operacional</h1>
            </div>
            <a href="${pageContext.request.contextPath}/form-entrega.jsp" class="btn-green">+ Nova entrega</a>
        </div>

        <!-- Metric Cards -->
        <div class="metrics-grid">
            <div class="metric-card">
                <div class="label">Em andamento</div>
                <div class="value"><%= emAndamento %></div>
            </div>
            <div class="metric-card">
                <div class="label">Entregas concluídas</div>
                <div class="value"><%= concluida %></div>
            </div>
            <div class="metric-card">
                <div class="label">Total de Clientes</div>
                <div class="value"><%= totalClientes %></div>
            </div>
            <div class="metric-card">
                <div class="label">Total de Produtos</div>
                <div class="value"><%= totalProdutos %></div>
            </div>
        </div>

        <!-- Table -->
        <div class="table-card">
            <h3>Entregas Recentes</h3>
            <table>
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Cliente</th>
                        <th>Status</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (entregas != null && !entregas.isEmpty()) {
                        for (Entrega e : entregas) { 
                            String badgeClass = "badge-pendente";
                            if ("Entregue".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-entregue";
                            else if ("Em Trânsito".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-transito";
                    %>
                        <tr>
                            <td><strong style="color: var(--primary-green);"><%= e.getIdRastreio() %></strong></td>
                            <td><%= e.getCliente() != null ? e.getCliente().getName() : "N/A" %></td>
                            <td><span class="badge <%= badgeClass %>"><%= e.getStatus() %></span></td>
                            <td>R$ <%= String.format("%.2f", e.calcularTotal()) %></td>
                        </tr>
                    <% } } else { %>
                        <tr><td colspan="4" style="text-align:center;">Nenhuma entrega registrada.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>