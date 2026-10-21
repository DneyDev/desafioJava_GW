<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Cliente" %>
<%@ page import="models.Endereco" %>
<%!
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean editando = cliente != null;
    String nome = editando ? cliente.getName() : "";
    String tipo = editando ? cliente.getTipo().name() : "PF";
    String documento = editando ? cliente.getDocumento() : "";
    String email = editando ? cliente.getEmail() : "";
%>
<% request.setAttribute("paginaAtiva", editando ? "lista-clientes" : "cliente"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= editando ? "Editar Cliente" : "Cadastrar Cliente" %> - VelozBR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Clientes &gt; <%= editando ? "Editar" : "Cadastrar" %></div>
                <h1 class="page-title"><%= editando ? "Editar cliente" : "Cadastrar novo cliente" %></h1>
            </div>
            <% if (editando) { %>
                <a class="btn-secondary" href="${pageContext.request.contextPath}/clientes">Voltar para clientes</a>
            <% } %>
        </div>
        <%@ include file="/WEB-INF/partials/alerta.jspf" %>
        <div class="table-card form-card">
            <form action="${pageContext.request.contextPath}/clientes" method="post">
                <% if (editando) { %>
                    <input type="hidden" name="id" value="<%= cliente.getId() %>">
                <% } %>
                <h3>Dados do cliente</h3>
                <div class="form-grid">
                    <div class="form-group span-all">
                        <label for="nome">Nome do Cliente / Razão social</label>
                        <input id="nome" type="text" name="nome" class="form-control" maxlength="255" value="<%= escHtml(nome) %>" required>
                    </div>
                    <div class="form-group">
                        <label for="tipo">Tipo</label>
                        <select id="tipo" name="tipo" class="form-control" required>
                            <option value="PF" <%= "PF".equals(tipo) ? "selected" : "" %>>Pessoa Física (PF)</option>
                            <option value="PJ" <%= "PJ".equals(tipo) ? "selected" : "" %>>Pessoa Jurídica (PJ)</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="documento">CPF / CNPJ</label>
                        <input id="documento" type="text" name="documento" class="form-control" maxlength="18" value="<%= escHtml(documento) %>" required>
                    </div>
                    <div class="form-group span-all">
                        <label for="email">E-mail</label>
                        <input id="email" type="email" name="email" class="form-control" maxlength="255" value="<%= escHtml(email) %>" required>
                    </div>
                </div>
                <% if (!editando) { %>
                    <h3 class="form-section">Endereço</h3>
                    <div class="form-grid">
                        <div class="form-group span-all">
                            <label for="rua">Rua</label>
                            <input id="rua" type="text" name="rua" class="form-control" maxlength="255" required>
                        </div>
                        <div class="form-group">
                            <label for="numero">Número</label>
                            <input id="numero" type="text" name="numero" class="form-control" maxlength="10" required>
                        </div>
                        <div class="form-group">
                            <label for="cidade">Cidade</label>
                            <input id="cidade" type="text" name="cidade" class="form-control" maxlength="100" required>
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
                            <input id="cep" type="text" name="cep" class="form-control" maxlength="9" required>
                        </div>
                    </div>
                <% } %>
                <div class="form-actions">
                    <button type="submit" class="btn-green"><%= editando ? "Salvar alterações" : "Cadastrar cliente" %></button>
                </div>
            </form>
        </div>
        <% if (editando) { %>
            <div class="table-card">
                <div class="section-header">
                    <h3>Endereços</h3>
                    <a class="btn-green" href="${pageContext.request.contextPath}/enderecos?clienteId=<%= cliente.getId() %>">Adicionar endereço</a>
                </div>
                <div class="table-scroll">
                    <table>
                        <thead><tr><th>Endereço completo</th><th>Ações</th></tr></thead>
                        <tbody>
                            <% if (cliente.getEnderecos() != null && !cliente.getEnderecos().isEmpty()) {
                                   for (Endereco endereco : cliente.getEnderecos()) { %>
                                <tr>
                                    <td><%= escHtml(endereco.getEndCompleto()) %></td>
                                    <td><a class="btn-secondary" href="${pageContext.request.contextPath}/enderecos?editar=<%= endereco.getId() %>">Editar</a></td>
                                </tr>
                            <%     }
                               } else { %>
                                <tr><td colspan="2" class="empty-state">Nenhum endereço cadastrado.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        <% } %>
    </div>
    <script>
        (function () {
            var tipo = document.getElementById('tipo');
            var documento = document.getElementById('documento');
            var labelDocumento = document.querySelector('label[for="documento"]');
            var labelNome = document.querySelector('label[for="nome"]');
            function atualizarCampos() {
                var pj = tipo.value === 'PJ';
                labelDocumento.textContent = pj ? 'CNPJ' : 'CPF';
                labelNome.textContent = pj ? 'Razão social' : 'Nome do cliente';
                documento.placeholder = pj ? '00.000.000/0001-00' : '000.000.000-00';
            }
            tipo.addEventListener('change', atualizarCampos);
            atualizarCampos();
        })();
    </script>
</body>
</html>