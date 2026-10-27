package validation;

import models.TipoCliente;
import models.Cliente;
import utils.*;
import models.Endereco;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteValid implements Valid<Cliente> {

    public static final Pattern EMAIL_REGEX =
        Pattern.compile(
            "^[\\w.+-]+@([\\w-]+\\.)+[a-z]{2,}$",
            Pattern.CASE_INSENSITIVE
        );
    public static final Pattern NOME_REGEX =
        Pattern.compile(
            "^[\\p{L}]+(?:(?:[\\p{Zs}]+[\\p{L}]+)|(?:[.\'’\\-][\\p{L}]+)|(?:\\.[\\p{Zs}]+[\\p{L}]+))*(?:\\.)?$"
        );
    public static final Pattern RAZAO_SOCIAL_REGEX =
        Pattern.compile("^[\\p{L}\\p{N}][\\p{L}\\p{N} .,&'/\\-]*$");
    public String validarCampo(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank()) {
            boolean feminino = List.of(
                "Razão social", "Rua", "Cidade", "Descrição"
            ).contains(nomeCampo);
            return nomeCampo
                + (feminino ? " é obrigatória!" : " é obrigatório!");
        }
        return null;
    }
    public String validarCampo(String valor, String nomeCampo, int max) {
        String erro = validarCampo(valor, nomeCampo);
        if (erro != null) {
            return erro;
        }
        if (valor.length() > max) {
            return nomeCampo
                + " deve ter no máximo "
                + max
                + " caracteres!";
        }
        return null;
    }
    public String validarNome(String nome) {
        String erro = validarCampo(nome, "Nome", 255);
        if (erro != null) {
            return erro;
        }
        if (!NOME_REGEX.matcher(nome.trim()).matches()) {
            return "Nome inválido! Use letras, espaços e pontuação simples (ponto, hífen ou apóstrofo).";
        }
        return null;
    }
    public String validarCidade(String cidade) {
        String erro = validarCampo(cidade, "Cidade", 100);
        if (erro != null) {
            return erro;
        }
        if (!NOME_REGEX.matcher(cidade.trim()).matches()) {
            return "Cidade inválida! Use letras, espaços e pontuação simples (ponto, hífen ou apóstrofo).";
        }
        return null;
    }
    public String validarCpf(String cpf) {
        String erro = validarCampo(cpf, "CPF");

        if (erro != null) {
            return erro;
        }
        if (!CpfUtil.valido(cpf)) {
            return "CPF inválido!";
        }
        return null;
    }
    public String validarRazaoSocial(String razao) {
        String erro = validarCampo(razao, "Razão social", 255);

        if (erro != null) {
            return erro;
        }
        if (!RAZAO_SOCIAL_REGEX.matcher(razao.trim()).matches()) {
            return "Razão social inválida! Use letras, números e os símbolos & . , - / '";
        }
        return null;
    }
    public String validarNome(String nome, TipoCliente tipo) {
        return tipo == TipoCliente.PJ
                ? validarRazaoSocial(nome)
                : validarNome(nome);
    }
    public String validarCnpj(String cnpj) {
        String erro = validarCampo(cnpj, "CNPJ");

        if (erro != null) {
            return erro;
        }
        if (!CnpjUtil.valido(cnpj)) {
            return "CNPJ inválido!";
        }
        return null;
    }
    public String validarDocumento(TipoCliente tipo, String documento) {
        return tipo == TipoCliente.PJ
                ? validarCnpj(documento)
                : validarCpf(documento);
    }
    public String validarEmail(String email) {
        String erro = validarCampo(email, "Email", 255);

        if (erro != null) {
            return erro;
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            return "Email inválido! Ex: nome@dominio.com";
        }
        return null;
    }
    public String validarEstado(String estado) {
        String erro = validarCampo(estado, "Estado");
        if (erro != null) {
            return erro;
        }
        if (!CodRastreioGen.estadoValido(estado)) {
            return "Estado inválido! Use a sigla (ex: PE) ou o nome do estado completo.";
        }
        return null;
    }
    public String validarCep(String cep) {
        String erro = validarCampo(cep, "CEP");

        if (erro != null) {
            return erro;
        }
        // Aceita 50000000 ou 50000-000, sem descartar letras/símbolos inválidos.
        if (!cep.trim().matches("\\d{5}-?\\d{3}")) {
            return "CEP inválido! Digite 8 números (ex: 50000-000).";
        }
        return null;
    }
    @Override
    public List<String> validar(Cliente cliente) {
        List<String> erros = new ArrayList<>();

        if (cliente.getTipo() == null) {
            erros.add("Erro: Tipo de cliente é obrigatório!");
        }
        addSeErro(
            erros,
            validarNome(cliente.getName(), cliente.getTipo())
        );
        addSeErro(
            erros,
            validarDocumento(cliente.getTipo(), cliente.getDocumento())
        );
        addSeErro(
            erros,
            validarEmail(cliente.getEmail())
        );
        List<Endereco> enderecos = cliente.getEnderecos();

        if (enderecos == null || enderecos.isEmpty()) {
            erros.add("Erro: Pelo menos um Endereço é obrigatório!");
        } else {
            for (Endereco end : enderecos) {
                addSeErro(erros, validarEstado(end.getEstado()));
                addSeErro(erros, validarCidade(end.getCidade()));
                addSeErro(erros, validarCep(end.getCep()));
                addSeErro(
                    erros,
                    validarCampo(end.getRua(), "Rua", 255)
                );
                addSeErro(
                    erros,
                    validarCampo(end.getNumero(), "Número", 10)
                );
                addSeErro(
                    erros,
                    validarCampo(end.getBairro(), "Bairro", 100)
                );
            }
        }
        return erros;
    }

    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) {
            erros.add("Erro: " + erro);
        }
    }
}