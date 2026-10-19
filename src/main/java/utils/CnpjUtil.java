package utils;

public class CnpjUtil {

    public static String limpar(String cnpj) {
        return cnpj == null ? "" : cnpj.replaceAll("\\D", "");
    }

    public static boolean valido(String cnpj) {
        String n = limpar(cnpj);
        if (n.length() != 14 || n.chars().distinct().count() == 1) return false;
        return calcularDigito(n, 12) == n.charAt(12) - '0'
            && calcularDigito(n, 13) == n.charAt(13) - '0';
    }

    private static int calcularDigito(String n, int tamanho) {
        int soma = 0;
        int peso = 2;
        for (int i = tamanho - 1; i >= 0; i--) {
            soma += (n.charAt(i) - '0') * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}