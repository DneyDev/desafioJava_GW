<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
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

    // os indicadores acima usam todas as entregas; a tabela mostra só as 5 mais recentes (maior id primeiro)
    List<Entrega> recentes = new ArrayList<>(entregas);
    recentes.sort(Comparator.comparingInt(Entrega::getId).reversed());
    if (recentes.size() > 5) {
        recentes = recentes.subList(0, 5);
    }
%>
<% request.setAttribute("paginaAtiva", "painel"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Painel Operacional - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Painel</div>
                <h1 class="page-title">Painel Operacional</h1>
            </div>
        <!--    <a href="${pageContext.request.contextPath}/form-entrega.jsp" class="btn-green"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 5v14M5 12h14"/></svg>Nova entrega</a> -->
        </div>
        <div class="metrics-grid">
            <div class="metric-card">
                <div class="metric-icon"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M2 6h11v10H2zM13 9h5l3 3v4h-8"/><circle cx="6.5" cy="17.5" r="2"/><circle cx="17.5" cy="17.5" r="2"/></svg></div>
                <div>
                    <div class="label">Em andamento</div>
                    <div class="value"><%= emAndamento %></div>
                </div>
            </div>
            <div class="metric-card">
                <div class="metric-icon"><svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M8 12l3 3 5-6"/></svg></div>
                <div>
                    <div class="label">Entregas concluídas</div>
                    <div class="value"><%= concluida %></div>
                </div>
            </div>
            <div class="metric-card">
                <div class="metric-icon"><svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="9" cy="8" r="4"/><path d="M2 21v-1a6 6 0 0 1 6-6h2a6 6 0 0 1 6 6v1"/><path d="M19 8v6M16 11h6"/></svg></div>
                <div>
                    <div class="label">Total de Clientes</div>
                    <div class="value"><%= totalClientes %></div>
                </div>
            </div>
            <div class="metric-card">
                <div class="metric-icon"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3 12V4a1 1 0 0 1 1-1h8l9 9-9 9-9-9z"/><circle cx="8" cy="8" r="1.5"/></svg></div>
                <div>
                    <div class="label">Total de Produtos</div>
                    <div class="value"><%= totalProdutos %></div>
                </div>
            </div>
        </div>

        <div class="table-card">
            <h3>Entregas Recentes</h3>
            <div class="table-scroll">
            <table>
                <thead>
                    <tr><th>Código</th><th>Cliente</th><th>Status</th><th>Total</th></tr>
                </thead>
                <tbody>
                    <% if (!recentes.isEmpty()) {
                        for (Entrega e : recentes) {
                            String badgeClass = "badge-pendente";
                            if ("Entregue".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-entregue";
                            else if ("Em transito".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-transito";
                            else if ("Cancelada".equalsIgnoreCase(e.getStatus())) badgeClass = "badge-cancelado";
                    %>
                        <tr>
                            <td class="codigo"><%= e.getIdRastreio() %></td>
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
    </div>

</body>
</html>