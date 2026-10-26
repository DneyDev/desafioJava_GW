
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="db.EntregaDao" %>
<%@ page import="models.Entrega" %>
<%@ page import="models.ItemEntrega" %>
<%@ page import="validation.EntregaValid" %>
<%!
    private static boolean validationStatusFinal(String status) {
        return "Entregue".equalsIgnoreCase(status)
                || "Cancelada".equalsIgnoreCase(status);
    }

    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
%>
<%
    List<Entrega> lista =
            (List<Entrega>) request.getAttribute("listaEntregas");

    if (lista == null) {
        try {
            lista = new EntregaDao().listarTodas();
        } catch (Exception e) {
            lista = java.util.Collections.emptyList();
        }
    }
%>
<% request.setAttribute("paginaAtiva", "entregas"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Histórico de Entregas - VelozBR</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>

    <div class="main-content">
        <div class="top-header">
            <div>
                <div class="breadcrumbs">Home &gt; Entregas</div>
                <h1 class="page-title">Histórico de entregas</h1>
            </div>

            <a href="${pageContext.request.contextPath}/form-entrega.jsp"
               class="btn-green">
                Nova entrega
            </a>
        </div>

        <%@ include file="/WEB-INF/partials/alerta.jspf" %>

        <div class="table-card">
            <h3>Todas as entregas</h3>

            <div class="table-scroll">
                <table>
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Cliente</th>
                            <th>Rota</th>
                            <th>Produtos / Itens</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Alterar status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>

                    <tbody>
                    <% if (lista != null && !lista.isEmpty()) {
                           for (Entrega entrega : lista) {
                               String status = entrega.getStatus() == null
                                       ? "" : entrega.getStatus();

                               String badgeClass = "badge-pendente";

                               if ("Entregue".equalsIgnoreCase(status)) {
                                   badgeClass = "badge-entregue";
                               } else if ("Em transito".equalsIgnoreCase(status)) {
                                   badgeClass = "badge-transito";
                               } else if ("Cancelada".equalsIgnoreCase(status)) {
                                   badgeClass = "badge-cancelado";
                               }
                    %>
                        <tr>
                            <td class="codigo">
                                <%= escHtml(entrega.getIdRastreio()) %>
                            </td>

                            <td>
                                <%= entrega.getCliente() == null
                                        ? "N/A"
                                        : escHtml(entrega.getCliente().getName()) %>
                            </td>

                            <td>
                                <div>
                                    <strong>Origem:</strong>
                                    <%= entrega.getOrigem() == null
                                            ? "N/A"
                                            : escHtml(entrega.getOrigem().getEndCompleto()) %>
                                </div>
                                <div>
                                    <strong>Destino:</strong>
                                    <%= entrega.getDestino() == null
                                            ? "N/A"
                                            : escHtml(entrega.getDestino().getEndCompleto()) %>
                                </div>
                            </td>

                            <td>
                                <% if (entrega.getItens() != null
                                        && !entrega.getItens().isEmpty()) {
                                       for (ItemEntrega item : entrega.getItens()) {
                                %>
                                    <div>
                                        <%= item.getQuantidade() %>x
                                        <%= item.getProduto() == null
                                                ? "Produto"
                                                : escHtml(item.getProduto().getProdName()) %>
                                    </div>
                                <%     }
                                   } else { %>
                                    <span>-</span>
                                <% } %>
                            </td>

                            <td>
                                R$
                                <%= String.format(
                                        new Locale("pt", "BR"),
                                        "%.2f",
                                        entrega.calcularTotal()) %>
                            </td>

                            <td>
                                <span class="badge <%= badgeClass %>">
                                    <%= escHtml(status) %>
                                </span>
                            </td>

                            <td>
                                <form class="status-form"
                                      action="${pageContext.request.contextPath}/atualizar-status"
                                      method="post">

                                    <input type="hidden"
                                           name="codigoRastreio"
                                           value="<%= escHtml(entrega.getIdRastreio()) %>">

                                    <select name="novoStatus"
                                            class="form-control"
                                            aria-label="Novo status"
                                            <%= validationStatusFinal(status)
                                                    ? "disabled" : "" %>>

                                        <% for (String s : EntregaValid.STATUS_VALIDOS) { %>
                                            <option value="<%= escHtml(s) %>"
                                                <%= s.equalsIgnoreCase(status)
                                                        ? "selected" : "" %>
                                                <%= ("Em transito".equalsIgnoreCase(status)
                                                        && "Pendente".equalsIgnoreCase(s))
                                                        ? "disabled style=\"color:#cbd5e1;background-color:#f3f4f6;\""
                                                        : "" %>>
                                                <%= escHtml(s) %>
                                            </option>
                                        <% } %>
                                    </select>

                                    <button type="submit"
                                            class="btn-green"
                                            <%= validationStatusFinal(status)
                                                    ? "disabled" : "" %>>
                                        OK
                                    </button>
                                </form>
                            </td>

                            <td>
                                <% if ("Pendente".equalsIgnoreCase(status)) { %>

                                    <a class="btn-secondary"
                                       href="${pageContext.request.contextPath}/entregas/editar?codigo=<%= URLEncoder.encode(entrega.getIdRastreio().trim(), "UTF-8") %>">
                                        Editar
                                    </a>

                                <% } else if ("Cancelada".equalsIgnoreCase(status)) { %>

                                    <div class="acoes">
                                        <form action="${pageContext.request.contextPath}/entregas/excluir"
                                              method="post"
                                              onsubmit="return confirm('Deseja excluir permanentemente esta entrega?');">

                                            <input type="hidden"
                                                   name="codigoRastreio"
                                                   value="<%= escHtml(entrega.getIdRastreio()) %>">

                                            <button type="submit"
                                                    class="btn-danger">
                                                Excluir
                                            </button>
                                        </form>
                                    </div>

                                <% } else { %>
                                    <span>—</span>
                                <% } %>
                            </td>
                        </tr>

                    <%     }
                       } else { %>

                        <tr>
                            <td colspan="8" class="empty-state">
                                Nenhuma entrega registrada.
                            </td>
                        </tr>

                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
