package src.utils;

public class CpfUtil {

    public static String limpar(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    public static boolean valido(String cpf) {
        String n = limpar(cpf);
        if (n.length() != 11 || n.chars().distinct().count() == 1) return false;
        return calcularDigito(n, 9) == n.charAt(9) - '0'
            && calcularDigito(n, 10) == n.charAt(10) - '0';
    }

    private static int calcularDigito(String n, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (n.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}