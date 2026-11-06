
package br.com.escalonador;

import com.google.gson.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, String> CENARIOS = Map.of(
            "1", "entradas/entrada_fcfs.json",
            "2", "entradas/entrada_sjf.json",
            "3", "entradas/cenario_ociosidade.json",
            "4", "entradas/cenario_duracoes.json",
            "5", "entradas/cenario_empates.json"
    );

    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);

            // MENU 1 - ESCOLHA DO CENÁRIO
            System.out.println("======================================");
            System.out.println("     SIMULADOR DE ESCALONAMENTO");
            System.out.println("======================================");
            System.out.println();
            System.out.println("ESCOLHA O CENÁRIO:");
            System.out.println("1 - Experimento principal");
            System.out.println("2 - Experimento principal (arquivo alternativo)");
            System.out.println("3 - Cenário de ociosidade");
            System.out.println("4 - Cenário de durações diferentes");
            System.out.println("5 - Cenário de empates");
            System.out.println();

            System.out.print("Digite a opção desejada: ");
            String opcaoCenario = scanner.nextLine().trim();

            String caminhoArquivo = CENARIOS.get(opcaoCenario);

            if (caminhoArquivo == null) {
                throw new IllegalArgumentException(
                        "Opção de cenário inválida."
                );
            }

            // MENU 2 - ESCOLHA DO ALGORITMO
            System.out.println();
            System.out.println("======================================");
            System.out.println("          ESCOLHA O ALGORITMO");
            System.out.println("======================================");
            System.out.println();
            System.out.println("1 - FCFS (First Come, First Served)");
            System.out.println("2 - SJF (Shortest Job First)");
            System.out.println();

            System.out.print("Digite a opção desejada: ");
            String opcaoAlgoritmo = scanner.nextLine().trim();

            String algoritmo;

            switch (opcaoAlgoritmo) {
                case "1":
                    algoritmo = "FCFS";
                    break;

                case "2":
                    algoritmo = "SJF";
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Opção de algoritmo inválida."
                    );
            }

            // LEITURA DO ARQUIVO JSON
            Path arquivo = Path.of(caminhoArquivo);

            if (!Files.exists(arquivo) || !Files.isRegularFile(arquivo)) {
                throw new IllegalArgumentException(
                        "Arquivo não encontrado: " + caminhoArquivo
                );
            }

            String json = Files.readString(
                    arquivo,
                    StandardCharsets.UTF_8
            );

            JsonElement elemento = JsonParser.parseString(json);

            if (!elemento.isJsonObject()) {
                throw new IllegalArgumentException(
                        "O conteúdo do arquivo deve ser um objeto JSON."
                );
            }

            JsonObject entrada = elemento.getAsJsonObject();

            // VALIDAÇÃO DOS PROCESSOS
            List<Processo> processos =
                    validarEObterProcessos(entrada);

            if (!entrada.has("configuracao")
                    || !entrada.get("configuracao").isJsonObject()) {

                throw new IllegalArgumentException(
                        "O campo configuracao deve ser um objeto JSON."
                );
            }

            // EXECUÇÃO DO ALGORITMO ESCOLHIDO
            Resultado resultado;

            switch (algoritmo) {

                case "FCFS":
                    resultado =
                            new EscalonadorFCFS().executar(processos);
                    break;

                case "SJF":
                    resultado =
                            new EscalonadorSJF().executar(processos);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Algoritmo não implementado."
                    );
            }

            // EXIBIÇÃO DOS RESULTADOS
            System.out.println();
            System.out.println("======================================");
            System.out.println("         RESULTADO DA SIMULAÇÃO");
            System.out.println("======================================");
            System.out.println("Cenário: " + caminhoArquivo);
            System.out.println("Algoritmo: " + algoritmo);
            System.out.println();
            System.out.println(GSON.toJson(resultado));

        } catch (Exception e) {

            System.err.println();
            System.err.println("========== ERRO ==========");
            System.err.println(
                    GSON.toJson(
                            Map.of("erro", mensagem(e))
                    )
            );

            System.exit(1);
        }
    }

    private static List<Processo> validarEObterProcessos(
            JsonObject entrada) {

        if (!entrada.has("processos")
                || !entrada.get("processos").isJsonArray()) {

            throw new IllegalArgumentException(
                    "O campo processos deve ser uma lista."
            );
        }

        JsonArray array = entrada.getAsJsonArray("processos");

        if (array.isEmpty()) {
            throw new IllegalArgumentException(
                    "A lista processos não pode ser vazia."
            );
        }

        List<Processo> processos = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        for (JsonElement elemento : array) {

            if (!elemento.isJsonObject()) {
                throw new IllegalArgumentException(
                        "Cada processo deve ser um objeto."
                );
            }

            JsonObject p = elemento.getAsJsonObject();

            String id = textoObrigatorio(p, "id");

            if (id.equals("OCIOSO")) {
                throw new IllegalArgumentException(
                        "O ID OCIOSO é reservado."
                );
            }

            if (!ids.add(id)) {
                throw new IllegalArgumentException(
                        "ID duplicado: " + id
                );
            }

            int chegada = inteiro(p, "chegada", 0);
            int cpu = inteiro(p, "cpu", 1);

            processos.add(new Processo(id, chegada, cpu));
        }

        return processos;
    }

    private static String textoObrigatorio(
            JsonObject obj, String campo) {

        if (!obj.has(campo)
                || !obj.get(campo).isJsonPrimitive()
                || !obj.getAsJsonPrimitive(campo).isString()
                || obj.get(campo).getAsString().isBlank()) {

            throw new IllegalArgumentException(
                    "O campo " + campo +
                            " deve ser uma string não vazia."
            );
        }

        return obj.get(campo).getAsString();
    }

    private static int inteiro(
            JsonObject obj, String campo, int minimo) {

        if (!obj.has(campo)
                || !obj.get(campo).isJsonPrimitive()
                || !obj.getAsJsonPrimitive(campo).isNumber()) {

            throw new IllegalArgumentException(
                    "O campo " + campo +
                            " deve ser um número inteiro."
            );
        }

        double valor = obj.get(campo).getAsDouble();

        if (!Double.isFinite(valor)
                || valor != Math.rint(valor)
                || valor < minimo
                || valor > Integer.MAX_VALUE) {

            throw new IllegalArgumentException(
                    "O campo " + campo +
                            " deve ser um inteiro maior ou igual a " +
                            minimo + "."
            );
        }

        return (int) valor;
    }

    private static String mensagem(Exception e) {

        return e.getMessage() == null
                ? "Entrada inválida."
                : e.getMessage();
    }
}
