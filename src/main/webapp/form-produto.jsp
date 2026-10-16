<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("paginaAtiva", "produto"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cadastrar Produto - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Cadastrar Produto</div>
                <h1 class="page-title">Cadastrar Novo Produto</h1>
            </div>
            
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card form-card">
            <h3>Dados do produto</h3>
            <form action="${pageContext.request.contextPath}/produto" method="post">
                <div class="form-grid">
                <div class="form-group span-all">
                    <label for="nome">Nome do Produto</label>
                    <input id="nome" type="text" name="nome" class="form-control" placeholder="Ex: Caixa de Detergente"  required>
                </div>
                    <div class="form-group span-all">
                        <label for="descricao">Descrição</label>
                        <textarea id="descricao" name="descricao" class="form-control" rows="3" placeholder="Detalhes do produto" required></textarea>
                    </div>
                <div class="form-group">
                    <label for="preco">Preço Unitário (R$)</label>
                    <input id="preco" type="number" name="preco" class="form-control" placeholder="0.00" step="0.01" min="0.01" required>
                </div>
                <div class="form-group">
                    <label for="peso">Peso (kg)</label>
                    <input id="peso" type="number" name="peso" class="form-control" placeholder="0.00" step="0.001" min="0.001" required>
                </div>
                </div>
                <div class="form-actions"><button type="submit" class="btn-green">Salvar Produto</button></div>
            </form>
        </div>
    </div>

</body>
</html>