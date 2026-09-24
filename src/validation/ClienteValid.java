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

    public String validarCampo(String valor, String nomeCampo){
        if (valor == null || valor.isBlank()) return nomeCampo + " é obrigatório!";
        return null;
    }

    public String validarNome(String nome){ 
        return validarCampo(nome, "Nome"); 
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
        addSeErro(erros, validarCampo(end.getEstado(), "Estado"));
        addSeErro(erros, validarCampo(end.getCidade(), "Cidade"));
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