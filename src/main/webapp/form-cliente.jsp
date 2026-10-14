<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Cliente - VelozBR</title>
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
                <li class="active"><a href="${pageContext.request.contextPath}/form-cliente.jsp">👤 Cadastrar Cliente</a></li>
                <li><a href="${pageContext.request.contextPath}/form-produto.jsp">🏷️ Cadastrar Produto</a></li>
            </ul>
        </div>
    </div>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home > Cadastrar Cliente</div>
                <h1 class="page-title">Cadastrar Novo Cliente</h1>
            </div>
        </div>

        <div class="table-card" style="max-width: 520px;">
            <form action="${pageContext.request.contextPath}/cadastrar-cliente" method="post">
                <div class="form-group">
                    <label>Nome do Cliente / Empresa</label>
                    <input type="text" name="nome" class="form-control" placeholder="Ex: Mercado Horizonte" required>
                </div>

                <div class="form-group">
                    <label>Tipo</label>
                    <select name="tipo" class="form-control" required>
                        <option value="PF">Pessoa Física (PF)</option>
                        <option value="PJ">Pessoa Jurídica (PJ)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>CPF / CNPJ</label>
                    <input type="text" name="documento" class="form-control" placeholder="00.000.000/0001-00" required>
                </div>

                <div class="form-group">
                    <label>Endereço Completo</label>
                    <input type="text" name="endereco" class="form-control" placeholder="Rua, Número, Bairro, Cidade - UF" required>
                </div>

                <button type="submit" class="btn-green" style="width: 100%; justify-content: center; margin-top: 10px;">Salvar Cliente</button>
            </form>
        </div>
    </div>

</body>
</html>