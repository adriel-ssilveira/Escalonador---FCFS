
package br.com.escalonador;

import br.com.escalonador.EscalonadorFCFS;
import br.com.escalonador.EscalonadorSJF;
import br.com.escalonador.Processo;
import br.com.escalonador.Resultado;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EscalonadoresTest {

    private final Gson gson = new Gson();

    @Test
    void fcfsDeveExecutarPorOrdemDeChegada() {

        List<Processo> processos = List.of(
                new Processo("P01", 0, 3),
                new Processo("P02", 1, 2)
        );

        Resultado resultado =
                new EscalonadorFCFS().executar(processos);

        JsonObject json = JsonParser
                .parseString(gson.toJson(resultado))
                .getAsJsonObject();

        assertEquals("FCFS",
                json.get("algoritmo").getAsString());

        JsonArray linha =
                json.getAsJsonArray("linha_do_tempo");

        assertEquals(2, linha.size());

        assertIntervalo(linha.get(0).getAsJsonObject(),
                "P01", 0, 3);

        assertIntervalo(linha.get(1).getAsJsonObject(),
                "P02", 3, 5);
    }

    @Test
    void sjfDeveEscolherMenorProcessoDisponivel() {

        List<Processo> processos = List.of(
                new Processo("P01", 0, 5),
                new Processo("P02", 1, 3),
                new Processo("P03", 2, 2),
                new Processo("P04", 8, 4)
        );

        Resultado resultado =
                new EscalonadorSJF().executar(processos);

        JsonObject json = JsonParser
                .parseString(gson.toJson(resultado))
                .getAsJsonObject();

        assertEquals("SJF",
                json.get("algoritmo").getAsString());

        JsonArray linha =
                json.getAsJsonArray("linha_do_tempo");

        assertEquals(4, linha.size());

        assertIntervalo(linha.get(0).getAsJsonObject(),
                "P01", 0, 5);

        assertIntervalo(linha.get(1).getAsJsonObject(),
                "P03", 5, 7);

        assertIntervalo(linha.get(2).getAsJsonObject(),
                "P02", 7, 10);

        assertIntervalo(linha.get(3).getAsJsonObject(),
                "P04", 10, 14);
    }

    @Test
    void sjfDeveRegistrarCpuOciosa() {

        List<Processo> processos = List.of(
                new Processo("P01", 3, 2)
        );

        Resultado resultado =
                new EscalonadorSJF().executar(processos);

        JsonObject json = JsonParser
                .parseString(gson.toJson(resultado))
                .getAsJsonObject();

        JsonArray linha =
                json.getAsJsonArray("linha_do_tempo");

        assertEquals(2, linha.size());

        assertIntervalo(linha.get(0).getAsJsonObject(),
                "OCIOSO", 0, 3);

        assertIntervalo(linha.get(1).getAsJsonObject(),
                "P01", 3, 5);
    }

    private void assertIntervalo(
            JsonObject intervalo,
            String processo,
            int inicio,
            int fim) {

        assertEquals(processo,
                intervalo.get("processo").getAsString());

        assertEquals(inicio,
                intervalo.get("inicio").getAsInt());

        assertEquals(fim,
                intervalo.get("fim").getAsInt());
    }
}
