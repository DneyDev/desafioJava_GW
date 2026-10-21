<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Cliente" %>
<%@ page import="models.Endereco" %>
<%@ page import="db.ClienteDao" %>
<%!
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    Endereco endereco = (Endereco) request.getAttribute("endereco");
    Cliente clienteSelecionado = (Cliente) request.getAttribute("clienteSelecionado");
    boolean editando = endereco != null;
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    if (clientes == null && !editando && clienteSelecionado == null) {
        try {
            clientes = new ClienteDao().listar();
        } catch (Exception e) {
            clientes = java.util.Collections.emptyList();
        }
    }
    int clienteId = clienteSelecionado != null ? clienteSelecionado.getId() : 0;
    String rua = editando ? endereco.getRua() : "";
    String numero = editando ? endereco.getNumero() : "";
    String cidade = editando ? endereco.getCidade() : "";
    String estado = editando ? endereco.getEstado() : "PE";
    String cep = editando ? endereco.getCep() : "";
%>
<% request.setAttribute("paginaAtiva", editando ? "lista-clientes" : "endereco"); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= editando ? "Editar endereço" : "Cadastrar endereço" %> - VelozBR</title>
    <link rel="stylesheet