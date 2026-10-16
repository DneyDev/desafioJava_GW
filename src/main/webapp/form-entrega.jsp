<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Cliente" %>
<%@ page import="models.Produto" %>
<%@ page import="db.ClienteDao" %>
<%@ page import="db.ProdutoDao" %>
<%
    ClienteDao clienteDao = new ClienteDao();
    ProdutoDao produtoDao = new ProdutoDao();
    List<Cliente> clientes = clienteDao.listar();
    List<Produto> produtos = produtoDao.listar();
%>
<% request.setAttribute("paginaAtiva", "nova-entrega"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nova Entrega - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Nova Entrega</div>
                <h1 class="page-title">Registrar Nova Entrega</h1>
            </div>
            
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card form-card">
            <h3>Dados da entrega</h3>
            <form action="${pageContext.request.contextPath}/lancar-entrega" method="post">
                <div class="form-group">
                    <label for="clienteId">Cliente Destinatário</label>
                    <select id="clienteId" name="clienteId" class="form-control" required>
                        <option value="">Selecione um Cliente</option>
                        <% for (Cliente c : clientes) { %>
                            <option value="<%= c.getId() %>"><%= c.getName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="produtoId">Produto</label>
                    <select id="produtoId" name="produtoId" class="form-control" required>
                        <option value="">Selecione um Produto</option>
                        <% for (Produto p : produtos) { %>
                            <option value="<%= p.getCodigo() %>"><%= p.getProdName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="quantidade">Quantidade</label>
                    <input id="quantidade" type="number" name="quantidade" class="form-control" value="1" min="1" required>
                </div>
                <div class="form-actions"><button type="submit" class="btn-green">Salvar Entrega</button></div>
            </form>
        </div>
    </div>

</body>
</html>