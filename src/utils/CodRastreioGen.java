package src.utils;

import java.text.Normalizer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CodRastreioGen {
    private static final String PAIS = "BR";
    private static final int TOTAL_NUMEROS = 10_000; //entre 0 e 9999

    private static final Map<String , String> ESTADOS = Map.ofEntries(
        Map.entry("ACRE", "AC"),
        Map.entry("ALAGOAS", "AL"),
        Map.entry("AMAPA", "AP"),
        Map.entry("AMAZONAS", "AM"),
        Map.entry("BAHIA", "BA"),
        Map.entry("CEARA", "CE"),
        Map.entry("DISTRITO FEDERAL", "DF"),
        Map.entry("ESPIRITO SANTO", "ES"),
        Map.entry("GOIAS", "GO"),
        Map.entry("MARANHAO", "MA"),
        Map.entry("MATO GROSSO", "MT"),
        Map.entry("MATO GROSSO DO SUL", "MS"),
        Map.entry("MINAS GERAIS", "MG"),
        Map.entry("PARA", "PA"),
        Map.entry("PARAIBA", "PB"),
        Map.entry("PARANA", "PR"),
        Map.entry("PERNAMBUCO", "PE"),
        Map.entry("PIAUI", "PI"),
        Map.entry("RIO DE JANEIRO", "RJ"),
        Map.entry("RIO GRANDE DO NORTE", "RN"),
        Map.entry("RIO GRANDE DO SUL", "RS"),
        Map.entry("RONDONIA", "RO"),
        Map.entry("RORAIMA", "RR"),
        Map.entry("SANTA CATARINA", "SC"),
        Map.entry("SAO PAULO", "SP"),
        Map.entry("SERGIPE", "SE"),
        Map.entry("TOCANTINS", "TO")
    );
    private static String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")   // remove acentos
                .toUpperCase();
    }
    // Aceita "PE", "pe", "Pernambuco", "pernambuco"... e devolve "PE" (ou null se não existir)
    public static String paraSigla(String estado) {
        String n = normalizar(estado);
        if (ESTADOS.containsValue(n)) return n;
        return ESTADOS.get(n);
    }
    public static boolean estadoValido(String estado) {
        return paraSigla(estado) != null;
    }

    public static String gerar(String estado, Set<String> existentes) {
        String uf = paraSigla(estado);
        if (uf == null) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }

        int inicio = ThreadLocalRandom.current().nextInt(TOTAL_NUMEROS);
        for (int i = 0; i < TOTAL_NUMEROS; i++) {
            int numero = (inicio + i) % TOTAL_NUMEROS;
            String codigo = PAIS + String.format("%04d", numero) + uf;
            if (!existentes.contains(codigo)) {
                return codigo;
            }
        }
        throw new IllegalStateException("Não há códigos de rastreio disponíveis para " + uf);
    }
}
