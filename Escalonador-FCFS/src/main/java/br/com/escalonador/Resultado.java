package br.com.escalonador;

import java.util.List;

public class Resultado {
    private final String algoritmo;
    private final List<Intervalo> linha_do_tempo;
    private final List<MetricaProcesso> processos;
    private final Medias medias;

    public Resultado(String algoritmo, List<Intervalo> linhaDoTempo,
                     List<MetricaProcesso> processos, Medias medias) {
        this.algoritmo = algoritmo;
        this.linha_do_tempo = linhaDoTempo;
        this.processos = processos;
        this.medias = medias;
    }
}
