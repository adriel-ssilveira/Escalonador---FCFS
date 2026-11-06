# Simulador de Escalonamento de Processos

Projeto desenvolvido em Java para o Trabalho II da disciplina de Sistemas Operacionais, do curso de Análise e Desenvolvimento de Sistemas do Instituto Federal de Educação, Ciência e Tecnologia do Rio Grande do Sul (IFRS).

O objetivo é implementar, simular e comparar dois algoritmos de escalonamento de processos: **FCFS (First Come, First Served)** e **SJF (Shortest Job First)**.

## 1. Algoritmos implementados

### FCFS — First Come, First Served

O FCFS executa os processos na ordem em que chegam à fila de prontos.

Características:
- Algoritmo não preemptivo.
- O processo selecionado utiliza a CPU até sua conclusão.
- A ordem de chegada determina a prioridade de execução.
- Em caso de empate no instante de chegada, utiliza a ordem lexicográfica dos identificadores dos processos.

### SJF — Shortest Job First

O SJF seleciona, entre os processos disponíveis, aquele que possui o menor tempo total de execução.

Características:
- Algoritmo não preemptivo.
- O processo selecionado utiliza a CPU até sua conclusão.
- A cada liberação da CPU, o algoritmo verifica os processos disponíveis.
- Em caso de empate no tempo de execução, considera primeiro o instante de chegada e depois o identificador em ordem lexicográfica.

## 2. Tecnologias utilizadas

- **Java 17 ou superior:** linguagem de programação.
- **Maven:** gerenciamento de dependências e construção do projeto.
- **Gson 2.13.2:** leitura e manipulação de arquivos JSON.
- **JUnit Jupiter 5.13.4:** execução de testes automatizados.
- **IntelliJ IDEA:** ambiente de desenvolvimento.

O projeto foi desenvolvido e executado utilizando OpenJDK 25, com compatibilidade configurada para Java 17.

## 3. Regras da simulação

O simulador considera as seguintes condições:

- Existe apenas uma CPU.
- O tempo é representado por unidades inteiras, iniciando em zero.
- Os instantes de chegada e os tempos de execução são conhecidos.
- Não existem operações de entrada e saída ou bloqueios.
- O custo de troca de contexto é considerado zero.
- Um processo não pode executar antes de seu instante de chegada.
- A CPU permanece ociosa quando não existem processos disponíveis.
- Os intervalos de execução seguem a convenção `[início, fim)`.

Os algoritmos implementados são não preemptivos: uma vez iniciado, o processo permanece na CPU até terminar.

## 4. Funcionalidades

O sistema oferece:

- Seleção de cenários de simulação por meio de menu interativo.
- Seleção independente do algoritmo de escalonamento.
- Leitura de arquivos JSON.
- Validação dos dados de entrada.
- Simulação dos algoritmos FCFS e SJF.
- Construção da linha do tempo de execução.
- Identificação dos períodos de ociosidade da CPU.
- Cálculo das métricas individuais dos processos.
- Cálculo das médias das métricas.
- Apresentação dos resultados em formato JSON.
- Testes automatizados das implementações.

## 5. Estrutura do projeto

```text
Escalonador-FCFS/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── br/com/escalonador/
│   │           ├── Main.java
│   │           ├── Processo.java
│   │           ├── EscalonadorFCFS.java
│   │           ├── EscalonadorSJF.java
│   │           ├── Intervalo.java
│   │           ├── MetricaProcesso.java
│   │           ├── Medias.java
│   │           └── Resultado.java
│   │
│   └── test/
│       └── java/
│           └── br/com/escalonador/
│               └── EscalonadoresTest.java
│
├── entradas/
│   ├── entrada_fcfs.json
│   ├── entrada_sjf.json
│   ├── cenario_ociosidade.json
│   ├── cenario_duracoes.json
│   └── cenario_empates.json
│
├── pom.xml
└── README.md
```

## 6. Organização das classes

| Classe | Responsabilidade |
|---|---|
| `Main` | Apresenta os menus, seleciona o cenário e o algoritmo, valida a entrada e exibe os resultados. |
| `Processo` | Representa os processos e seus atributos. |
| `EscalonadorFCFS` | Implementa o algoritmo FCFS. |
| `EscalonadorSJF` | Implementa o algoritmo SJF. |
| `Intervalo` | Representa os intervalos de execução na linha do tempo. |
| `MetricaProcesso` | Armazena as métricas individuais de cada processo. |
| `Medias` | Armazena as médias das métricas calculadas. |
| `Resultado` | Reúne os dados produzidos pela simulação. |
| `EscalonadoresTest` | Contém os testes automatizados dos algoritmos. |

## 7. Arquivos de entrada e cenários

Os dados utilizados nas simulações são armazenados em arquivos JSON dentro da pasta `entradas`.

O sistema disponibiliza cinco cenários:

| Opção | Arquivo | Objetivo |
|---|---|---|
| 1 | `entrada_fcfs.json` | Experimento principal. |
| 2 | `entrada_sjf.json` | Arquivo alternativo do experimento principal. |
| 3 | `cenario_ociosidade.json` | Verificar períodos de ociosidade da CPU. |
| 4 | `cenario_duracoes.json` | Avaliar processos com diferentes tempos de execução e chegadas durante a execução de outros processos. |
| 5 | `cenario_empates.json` | Verificar critérios de desempate entre processos. |

### Formato dos arquivos JSON

Exemplo de entrada:

```json
{
  "algoritmo": "FCFS",
  "configuracao": {},
  "processos": [
    {"id": "P01", "chegada": 0, "cpu": 8},
    {"id": "P02", "chegada": 1, "cpu": 4},
    {"id": "P03", "chegada": 2, "cpu": 2},
    {"id": "P04", "chegada": 4, "cpu": 1}
  ]
}
```

Cada processo contém:

- `id`: identificador único do processo.
- `chegada`: instante em que o processo chega ao sistema.
- `cpu`: quantidade de unidades de tempo necessárias para sua execução.

Os identificadores devem ser únicos, os tempos de chegada devem ser inteiros não negativos e os tempos de CPU devem ser inteiros positivos.

O campo `configuracao` deve ser um objeto JSON. Para os algoritmos FCFS e SJF, utiliza-se um objeto vazio (`{}`).

**Importante:** a escolha do algoritmo é feita pelo menu interativo do programa. O campo `algoritmo` presente nos arquivos JSON não determina a execução.

## 8. Como executar o projeto

### 8.1. Pré-requisitos

- Java Development Kit (JDK) 17 ou superior.
- IntelliJ IDEA ou outra IDE compatível com projetos Maven.
- Dependências Maven carregadas pelo projeto.

### 8.2. Abrindo o projeto

1. Abra o IntelliJ IDEA.
2. Selecione a opção para abrir um projeto existente.
3. Escolha a pasta `Escalonador-FCFS`.
4. Aguarde o carregamento das dependências Maven.

### 8.3. Executando o simulador

Execute a classe `Main.java`, localizada em:

`src/main/java/br/com/escalonador/Main.java`

O programa apresentará dois menus.

**Primeiro menu — Seleção do cenário**

```text
======================================
     SIMULADOR DE ESCALONAMENTO
======================================

ESCOLHA O CENÁRIO:

1 - Experimento principal
2 - Experimento principal (arquivo alternativo)
3 - Cenário de ociosidade
4 - Cenário de durações diferentes
5 - Cenário de empates
```

**Segundo menu — Seleção do algoritmo**

```text
======================================
          ESCOLHA O ALGORITMO
======================================

1 - FCFS (First Come, First Served)
2 - SJF (Shortest Job First)
```

Após as duas escolhas, o sistema carrega os processos do cenário selecionado e executa o algoritmo escolhido.

O resultado é apresentado no console em formato JSON.

## 9. Métricas calculadas

O simulador calcula três métricas para cada processo:

| Métrica | Fórmula | Descrição |
|---|---|---|
| Turnaround | Conclusão − Chegada | Tempo total entre a chegada e a conclusão do processo. |
| Espera | Turnaround − Tempo de CPU | Tempo total em que o processo permaneceu aguardando na fila. |
| Resposta | Primeira execução − Chegada | Tempo entre a chegada e o primeiro atendimento pela CPU. |

Também são calculadas as médias de turnaround, espera e resposta considerando todos os processos da simulação.

## 10. Testes automatizados

O projeto utiliza JUnit Jupiter 5.13.4 para verificar o comportamento dos algoritmos.

A classe `EscalonadoresTest.java` contempla testes para:

1. Verificação da ordem de execução e das métricas do FCFS.
2. Verificação da seleção do menor processo disponível no SJF.
3. Verificação do tratamento de períodos de ociosidade da CPU.

### Executando os testes pelo IntelliJ IDEA

1. Abra a janela Maven.
2. Expanda a seção `Lifecycle`.
3. Execute `clean`.
4. Execute `test`.
5. Execute `package`.

O Maven realizará a limpeza, a execução dos testes automatizados e a construção do pacote do projeto.

## 11. Considerações finais

O simulador permite analisar e comparar o comportamento dos algoritmos FCFS e SJF em diferentes situações de escalonamento.

Por meio dos cenários propostos, é possível observar como a ordem de chegada, a duração dos processos, os períodos de ociosidade e os critérios de desempate influenciam o desempenho da CPU e as métricas dos processos.

A implementação foi organizada em classes com responsabilidades específicas, buscando facilitar a manutenção, a compreensão e a validação do sistema.

---

**Trabalho II — Sistemas Operacionais**  
**Curso:** Análise e Desenvolvimento de Sistemas  
**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia do Rio Grande do Sul (IFRS)