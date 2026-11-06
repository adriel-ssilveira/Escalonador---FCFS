package br.com.escalonador;

import java.util.*;

public class EscalonadorFCFS {
    public Resultado executar(List<Processo> entrada) {
        List<Processo> fila = new ArrayList<>(entrada);
        fila.sort(Comparator.comparingInt(Processo::getChegada)
                .thenComparing(Processo::getId));

        List<Intervalo> linha = new ArrayList<>();
        List<MetricaProcesso> metricas = new ArrayList<>();
        int relogio = 0;
        double somaTurnaround = 0, somaEspera = 0, somaResposta = 0;

        for (Processo p : fila) {
            if (relogio < p.getChegada()) {
                adicionarIntervalo(linha, "OCIOSO", relogio, p.getChegada());
                relogio = p.getChegada();
            }
            int inicio = relogio;
            int fim = inicio + p.getCpu();
            adicionarIntervalo(linha, p.getId(), inicio, fim);
            MetricaProcesso m = new MetricaProcesso(
                    p.getId(), inicio, fim, p.getChegada(), p.getCpu());
            metricas.add(m);

            somaTurnaround += fim - p.getChegada();
            somaEspera += inicio - p.getChegada();
            somaResposta += inicio - p.getChegada();
            relogio = fim;
        }

        metricas.sort(Comparator.comparing(MetricaProcesso::getId));
        int n = entrada.size();
        Medias medias = new Medias(somaTurnaround / n, somaEspera / n, somaResposta / n);
        return new Resultado("FCFS", linha, metricas, medias);
    }

    private void adicionarIntervalo(List<Intervalo> linha, String processo, int inicio, int fim) {
        if (fim <= inicio) return;
        if (!linha.isEmpty()) {
            Intervalo anterior = linha.get(linha.size() - 1);
            if (anterior.getProcesso().equals(processo) && anterior.getFim() == inicio) {
                linha.set(linha.size() - 1,
                        new Intervalo(processo, anterior.getInicio(), fim));
                return;
            }
        }
        linha.add(new Intervalo(processo, inicio, fim));
    }
}
