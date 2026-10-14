package models;

public enum TipoCliente {
    PF("Nome", "CPF"),
    PJ("Razão social", "CNPJ");

    private final String rotuloNome;
    private final String rotuloDocumento;

    TipoCliente(String rotuloNome, String rotuloDocumento) {
        this.rotuloNome = rotuloNome;
        this.rotuloDocumento = rotuloDocumento;
    }

    public String getRotuloNome() { return rotuloNome; }
    public String getRotuloDocumento() { return rotuloDocumento; }
}