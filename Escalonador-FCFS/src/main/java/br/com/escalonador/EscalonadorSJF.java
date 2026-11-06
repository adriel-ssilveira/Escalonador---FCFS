package br.com.escalonador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EscalonadorSJF {

    /**
     * SJF não preemptivo: quando a CPU fica livre, escolhe o processo
     * disponível com o menor tempo total de CPU.
     */
    public Resultado executar(List<Processo> entrada) {
        List<Processo> pendentes = new ArrayList<>(entrada);
        List<Intervalo> linha = new ArrayList<>();
        List<MetricaProcesso> metricas = new ArrayList<>();

        int relogio = 0;
        double somaTurnaround = 0;
        double somaEspera = 0;
        double somaResposta = 0;

        while (!pendentes.isEmpty()) {
            final int instanteAtual = relogio;

            Processo proximo = pendentes.stream()
                    .filter(p -> p.getChegada() <= instanteAtual)
                    .min(Comparator.comparingInt(Processo::getCpu)
                            .thenComparingInt(Processo::getChegada)
                            .thenComparing(Processo::getId))
                    .orElse(null);

            // Se nenhum processo chegou, avança o relógio até a próxima chegada.
            if (proximo == null) {
                int proximaChegada = pendentes.stream()
                        .mapToInt(Processo::getChegada)
                        .min()
                        .orElseThrow();

                adicionarIntervalo(linha, "OCIOSO", relogio, proximaChegada);
                relogio = proximaChegada;
                continue;
            }

            pendentes.remove(proximo);

            int inicio = relogio;
            int fim = inicio + proximo.getCpu();

            adicionarIntervalo(linha, proximo.getId(), inicio, fim);

            MetricaProcesso metrica = new MetricaProcesso(
                    proximo.getId(), inicio, fim,
                    proximo.getChegada(), proximo.getCpu()
            );
            metricas.add(metrica);

            int turnaround = fim - proximo.getChegada();
            int espera = inicio - proximo.getChegada();
            int resposta = inicio - proximo.getChegada();

            somaTurnaround += turnaround;
            somaEspera += espera;
            somaResposta += resposta;

            relogio = fim;
        }

        metricas.sort(Comparator.comparing(MetricaProcesso::getId));
        int quantidade = entrada.size();

        Medias medias = new Medias(
                somaTurnaround / quantidade,
                somaEspera / quantidade,
                somaResposta / quantidade
        );

        return new Resultado("SJF", linha, metricas, medias);
    }

    private void adicionarIntervalo(List<Intervalo> linha, String processo,
                                    int inicio, int fim) {
        if (fim <= inicio) {
            return;
        }

        if (!linha.isEmpty()) {
            Intervalo anterior = linha.get(linha.size() - 1);

            if (anterior.getProcesso().equals(processo)
                    && anterior.getFim() == inicio) {
                linha.set(linha.size() - 1,
                        new Intervalo(processo, anterior.getInicio(), fim));
                return;
            }
        }

        linha.add(new Intervalo(processo, inicio, fim));
    }
}
