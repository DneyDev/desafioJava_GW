package validation;

import models.Endereco;
import java.util.ArrayList;
import java.util.List;

public class EnderecoValid implements Valid<Endereco> {

    private final ClienteValid clienteValid = new ClienteValid();

    @Override
    public List<String> validar(Endereco endereco) {
        List<String> erros = new ArrayList<>();
        if (endereco == null) {
            erros.add("Erro: Endereço é obrigatório!");
            return erros;
        }
        addSeErro(erros, clienteValid.validarEstado(endereco.getEstado()));
        addSeErro(erros, clienteValid.validarCidade(endereco.getCidade()));
        addSeErro(erros, clienteValid.validarCep(endereco.getCep()));
        addSeErro(erros, clienteValid.validarCampo(endereco.getRua(), "Rua", 255));
        addSeErro(erros, clienteValid.validarCampo(endereco.getNumero(), "Número", 10));
        addSeErro(erros, clienteValid.validarCampo(endereco.getBairro(), "Bairro", 100));
        return erros;
    }

    private void addSeErro(List<String> erros, String erro) {
        if (erro != null) erros.add("Erro: " + erro);
    }
}