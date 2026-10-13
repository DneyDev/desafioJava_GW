package validation;

import models.Entrega;
import java.util.ArrayList;
import java.util.List;

public class EntregaValid implements Valid<Entrega>{

    public static final List<String> STATUS_VALIDOS = List.of("Pendente", "Em transito", "Entregue", "Cancelada");
    public static final List<String> STATUS_FINAIS = List.of("Entregue", "Cancelada"); //depois desses a entrega não muda mais

    //aceita "pendente", "EM TRANSITO"... e devolve no formato padrão (ou null se não existir)
    public static String statusPadrao(String status){
        if(status == null) return null;
        for(String s : STATUS_VALIDOS){
            if(s.equalsIgnoreCase(status.trim())) return s;
        }
        return null;
    }

    public String validarStatus(String status){
        if(status == null || status.isBlank()) return "Status é obrigatório!";
        if(statusPadrao(status) == null){
            return "Status inválido! Use: " + String.join(", ", STATUS_VALIDOS) + ".";
        }
        return null;
    }

    public String validarStatusAtual(String statusAtual){ //só dá pra atualizar se a entrega ainda não terminou
        if(statusAtual != null && STATUS_FINAIS.contains(statusAtual)){
            return "Essa entrega já está " + statusAtual + " e não pode mais ser alterada!";
        }
        return null;
    }

    public String validarAtualizacaoStatus(String statusAtual, String novoStatus){
        String erro = validarStatusAtual(statusAtual);
        if(erro != null) return erro;
        erro = validarStatus(novoStatus);
        if(erro != null) return erro;
        if(statusPadrao(novoStatus).equalsIgnoreCase(statusAtual)){
            return "A entrega já está com o status " + statusAtual + "!";
        }
        return null;
    }

    @Override
    public List<String>validar(Entrega entrega){
        List<String> erros = new ArrayList<>();
        if(entrega.getCliente() == null){
            erros.add("Erro: Cliente não encontrado!");
        }
        if(entrega.getItens() == null || entrega.getItens().isEmpty()){
            erros.add("Erro: A entrega precisa ter ao menos um item!");
        }
        String erroStatus = validarStatus(entrega.getStatus());
        if(erroStatus != null){
            erros.add("Erro: " + erroStatus);
        }
        return erros;
    }
}