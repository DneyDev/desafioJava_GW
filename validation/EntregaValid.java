package validation;

import src.models.Entrega;
import java.util.ArrayList;
import java.util.List;

public class EntregaValid implements Valid<Entrega>{
    @Override 
    public List<String>validar(Entrega entrega){
        List<String> erros = new ArrayList<>();
        if(entrega.getCliente() == null){
            erros.add("Erro: CLiente não encontrado!");
        }
        if(entrega.getProdutos() == null || entrega.getProdutos().isEmpty()){
            erros.add("Erro: Não há produto(s) cadastrado(s)");
        }
        return erros;
    }
}
