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
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Nova Entrega - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <div class="sidebar">
        <div>
            <div class="logo">🚚 VelozBR</div>
            <ul class="menu">
                <li><a href="${pageContext.request.contextPath}/index.jsp">📊 Painel</a></li>
                <li><a href="${pageContext.request.contextPath}/lista-entregas">📦 Gestão de Entregas</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/form-entrega.jsp">➕ Nova Entrega</a></li>
                <li><a href="${pageContext.request.contextPath}/form-cliente.jsp">👤 Cadastrar Cliente</a></li>
                <li><a href="${pageContext.request.contextPath}/form-produto.jsp">🏷️ Cadastrar Produto</a></li>
            </ul>
        </div>
    </div>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home > Nova Entrega</div>
                <h1 class="page-title">Registrar Nova Entrega</h1>
            </div>
        </div>

        <div class="table-card" style="max-width: 520px;">
            <form action="${pageContext.request.contextPath}/lancar-entrega" method="post">
                <div class="form-group">
                    <label>Cliente Destinatário</label>
                    <select name="clienteId" class="form-control" required>
                        <option value="">Selecione um Cliente</option>
                        <% for (Cliente c : clientes) { %>
                            <option value="<%= c.getId() %>"><%= c.getName() %></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Produto</label>
                    <select name="produtoId" class="form-control" required>
                        <option value="">Selecione um Produto</option>
                        <% for (Produto p : produtos) { %>
                            <option value="<%= p.getCodigo() %>"><%= p.getProdName() %></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Quantidade</label>
                    <input type="number" name="quantidade" class="form-control" value="1" min="1" required>
                </div>

                <button type="submit" class="btn-green" style="width: 100%; justify-content: center; margin-top: 10px;">Salvar Entrega</button>
            </form>
        </div>
    </div>

</body>
</html>