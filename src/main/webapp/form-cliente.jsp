<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("paginaAtiva", "cliente"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cadastrar Cliente - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Cadastrar Cliente</div>
                <h1 class="page-title">Cadastrar Novo Cliente</h1>
            </div>
            
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card form-card">
            <form action="${pageContext.request.contextPath}/clientes" method="post">
                <h3>Dados do cliente</h3>
                <div class="form-grid">
                <div class="form-group span-all">
                    <label for="nome">Nome do Cliente / Razão social</label>
                    <input id="nome" type="text" name="nome" class="form-control" placeholder="Ex: Mercado Horizonte"  required>
                </div>
                    <div class="form-group">
                        <label for="tipo">Tipo</label>
                        <select id="tipo" name="tipo" class="form-control" required>
                            <option value="PF">Pessoa Física (PF)</option>
                            <option value="PJ">Pessoa Jurídica (PJ)</option>
                        </select>
                    </div>
                <div class="form-group">
                    <label for="documento">CPF / CNPJ</label>
                    <input id="documento" type="text" name="documento" class="form-control" placeholder="00.000.000/0001-00" maxlength="18" required>
                </div>
                <div class="form-group span-all">
                    <label for="email">E-mail</label>
                    <input id="email" type="email" name="email" class="form-control" placeholder="contato@empresa.com"  required>
                </div>
                </div>

                <h3 class="form-section">Endereço</h3>
                <div class="form-grid">
                <div class="form-group span-all">
                    <label for="rua">Rua</label>
                    <input id="rua" type="text" name="rua" class="form-control" placeholder=""  required>
                </div>
                <div class="form-group">
                    <label for="numero">Número</label>
                    <input id="numero" type="text" name="numero" class="form-control" placeholder="" maxlength="10" required>
                </div>
                <div class="form-group">
                    <label for="cidade">Cidade</label>
                    <input id="cidade" type="text" name="cidade" class="form-control" placeholder=""  required>
                </div>
                    <div class="form-group">
                        <label for="estado">UF</label>
                        <select id="estado" name="estado" class="form-control" required>
                            <% String[] ufs = {"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};
                               for (String uf : ufs) { %>
                                <option value="<%= uf %>"><%= uf %></option>
                            <% } %>
                        </select>
                    </div>
                <div class="form-group">
                    <label for="cep">CEP</label>
                    <input id="cep" type="text" name="cep" class="form-control" placeholder="00000-000" maxlength="9" required>
                </div>
                </div>
                <div class="form-actions"><button type="submit" class="btn-green">Salvar Cliente</button></div>
            </form>
        </div>
    </div>

</body>
</html>