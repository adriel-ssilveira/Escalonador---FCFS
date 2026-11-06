package br.com.escalonador;

public class Intervalo {
    private final String processo;
    private final int inicio;
    private final int fim;

    public Intervalo(String processo, int inicio, int fim) {
        this.processo = processo;
        this.inicio = inicio;
        this.fim = fim;
    }
    public String getProcesso() { return processo; }
    public int getInicio() { return inicio; }
    public int getFim() { return fim; }
}
