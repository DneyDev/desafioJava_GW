<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Produto - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <div class="sidebar">
        <div>
            <div class="logo">🚚 VelozBR</div>
            <ul class="menu">
                <li><a href="${pageContext.request.contextPath}/index.jsp">📊 Painel</a></li>
                <li><a href="${pageContext.request.contextPath}/lista-entregas">📦 Gestão de Entregas</a></li>
                <li><a href="${pageContext.request.contextPath}/form-entrega.jsp">➕ Nova Entrega</a></li>
                <li><a href="${pageContext.request.contextPath}/form-cliente.jsp">👤 Cadastrar Cliente</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/form-produto.jsp">🏷️ Cadastrar Produto</a></li>
            </ul>
        </div>
    </div>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home > Cadastrar Produto</div>
                <h1 class="page-title">Cadastrar Novo Produto</h1>
            </div>
        </div>

        <div class="table-card" style="max-width: 520px;">
            <form action="${pageContext.request.contextPath}/cadastrar-produto" method="post">
                <div class="form-group">
                    <label>Nome do Produto</label>
                    <input type="text" name="nome" class="form-control" placeholder="Ex: Caixa de Detergente" required>
                </div>

                <div class="form-group">
                    <label>Preço Unitário (R$)</label>
                    <input type="number" step="0.01" name="preco" class="form-control" placeholder="0.00" required>
                </div>

                <div class="form-group">
                    <label>Peso (kg)</label>
                    <input type="number" step="0.01" name="peso" class="form-control" placeholder="0.00" required>
                </div>

                <button type="submit" class="btn-green" style="width: 100%; justify-content: center; margin-top: 10px;">Salvar Produto</button>
            </form>
        </div>
    </div>

</body>
</html>