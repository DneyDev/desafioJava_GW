package src.validation;

import src.models.Entrega;
import java.util.ArrayList;
import java.util.List;

public class EntregaValid implements Valid<Entrega>{
    @Override 
    public List<String>validar(Entrega entrega){
        List<String> erros = new ArrayList<>();
        if(entrega.getCliente() == null){
            erros.add("Erro: Cliente não encontrado!");
        }
        if(entrega.getItens() == null || entrega.getItens().isEmpty()){
            erros.add("Erro: A entrega precisa ter ao menos um item!");
        }
        return erros;
    }
}