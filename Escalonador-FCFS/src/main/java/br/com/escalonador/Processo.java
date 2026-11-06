package br.com.escalonador;

public class Processo {
    private final String id;
    private final int chegada;
    private final int cpu;

    public Processo(String id, int chegada, int cpu) {
        this.id = id;
        this.chegada = chegada;
        this.cpu = cpu;
    }
    public String getId() { return id; }
    public int getChegada() { return chegada; }
    public int getCpu() { return cpu; }
}
