package src.validation;

import java.util.List;

public interface Valid<Type> {
    List<String>validar(Type objeto);
}
