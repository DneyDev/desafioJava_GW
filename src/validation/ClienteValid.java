package src.validation;

import src.models.Cliente;
import src.utils.*;
import src.models.Endereco;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteValid implements Valid<Cliente> {

    public static final Pattern EMAIL_REGEX =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern NOME_REGEX =
        Pattern.compile("^[\\p{L}]+([ '\\-][\\p{L}]+)*$");
    // \p{L} aceita qualquer letra, inclusive acentuadas (á, ã, ç, é...).
    // ([ '\\-][\\p{L}]+)* permite espaços, apóstrofo e hífen entre as palavras, mas impede que o nome começe ou termine com esses símbolos

    public String validarCampo(String valor, String nomeCampo){
        if (valor == null || valor.isEmpty()) return nomeCampo + " é obrigatório!";
        return null;
    }

    public String validarNome(String nome) {
        String erro = validarCampo(nome, "Nome");
        if (erro != null) return erro;
        if (!NOME_REGEX.matcher(nome.trim()).matches()) {
            return "Nome inválido! Use apenas letras e espaços (sem números).";
        }
        return null;
    }
    public String validarCidade(String cidade) {
        String erro = validarCampo(cidade, "Cidade");
        if (erro != null) return erro;
        if (!NOME_REGEX.matcher(cidade).matches()) {
            return "Cidade inválida! Use apenas letras.";
        }
        return null;
    }
    public String validarCpf(String cpf){
        String erro = validarCampo(cpf, "CPF");
        if (erro != null) return erro;
        if (!CpfUtil.valido(cpf)) return "CPF inválido!";
        return null;
    }

    public String validarEmail(String email) {
        String erro = validarCampo(email, "Email");
        if (erro != null) return erro;
        if (!EMAIL_REGEX.matcher(email).matches()) return "Email inválido! Ex: nome@dominio.com";
        return null;
    }
    public String validarEstado(String estado) {
        String erro = validarCampo(estado, "Estado");
        if (erro != null) return erro;
        if (!CodRastreioGen.estadoValido(estado)) {
            return "Estado inválido! Use a sigla (ex: PE) ou o nome do estado completo.";
        }
        return null;
    }

    @Override
    public List<String> validar(Cliente cliente) {
        List<String> erros = new ArrayList<>();
        addSeErro(erros, validarNome(cliente.getName()));
        addSeErro(erros, validarCpf(cliente.getCpf()));
        addSeErro(erros, validarEmail(cliente.getEmail()));

        Endereco end = cliente.getEnd();
        if (end == null) {
            erros.add("Erro: Endereço é obrigatório!");
        } else {
            addSeErro(erros, validarEstado(end.getEstado()));
            addSeErro(erros, validarCidade(end.getCidade()));
            addSeErro(erros, validarCampo(end.getCep(), "CEP"));
            addSeErro(erros, validarCampo(end.getRua(), "Rua"));
            addSeErro(erros, validarCampo(end.getNumero(), "Número"));
        }
            return erros;
    }

    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) erros.add("Erro: " + erro);
    }
}