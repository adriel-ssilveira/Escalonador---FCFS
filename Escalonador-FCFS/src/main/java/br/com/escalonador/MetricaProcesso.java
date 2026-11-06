package br.com.escalonador;

public class MetricaProcesso {

    private final String id;
    private final int primeira_execucao;
    private final int conclusao;
    private final int turnaround;
    private final int espera;
    private final int resposta;

    public MetricaProcesso(String id, int primeiraExecucao, int conclusao,
                           int chegada, int cpu) {

        this.id = id;
        this.primeira_execucao = primeiraExecucao;
        this.conclusao = conclusao;

        this.turnaround = conclusao - chegada;
        this.espera = this.turnaround - cpu;
        this.resposta = primeiraExecucao - chegada;
    }

    public String getId() {
        return id;
    }
}