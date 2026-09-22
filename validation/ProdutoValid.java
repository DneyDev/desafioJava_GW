package validation;

import src.models.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoValid implements Valid<Produto>{

    @Override 
    public List<String>validar(Produto produto){
        List<String>erros = new ArrayList<>();
        if(produto.getProdName() == null || produto.getProdName().isBlank()){
            erros.add("Erro: Nome necessário");
        }
        if(produto.getPrice() <= 0){
            erros.add("Erro: Preço do produto é obrigatório");
        }
        if(produto.getDesc() == null || produto.getDesc().isBlank()){
            erros.add("Erro: Descrição é obrigatória.");
        }
        return erros;
    }
    
}
