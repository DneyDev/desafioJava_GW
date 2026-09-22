package validation;

import src.models.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteValid implements Valid<Cliente>{
    
    public static final Pattern EMAIL_REGEX =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$");

    @Override 
    public List<String>validar(Cliente cliente){
        List<String>erros = new ArrayList<>();

        if(cliente.getName() == null || cliente.getName().isBlank()){
            erros.add("Erro: Nome é obrigatório");
        }
        if(cliente.getCpf() == null || cliente.getCpf().isBlank()){
            erros.add("Erro: CPF é obrigatório!");
        }
        if(cliente.getEmail() == null || cliente.getEmail().isBlank()){
            erros.add("Erro: Email é obrigatório!");
        }
        return erros;
    }
}
