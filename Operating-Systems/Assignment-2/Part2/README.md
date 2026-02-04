# Simulador de escalonamento e memória - Parte 2

Este projeto é um simulador de processos baseado em um modelo de 5 estados (`NEW`, `READY`, `RUNNING`, `BLOCKED`, `EXIT`), com integração do gestor de memória desenvolvido na Parte 1 deste trabalho.

## Estrutura do Projeto

- **simulador.c** e **simulador.h**: Arquivos principais que implementam o simulador.
- **memoria.c** e **memoria.h**: Implementação do gestor de memória.
- **queue.c** e **queue.h**: Implementação das filas utilizadas no simulador.
- **inputs_part2.c**: Contém os diferentes inputs que podem ser usados para simular o comportamento do sistema.
- **makefile**: Arquivo para automatizar a compilação e execução do projeto.
- **output2TXX.out**: Arquivos gerados pelo simulador contendo o estado dos processos em cada instante.

## Requisitos de execução

1. Certifique-se de que todos os arquivos necessários estão no mesmo diretório:
   - `simulador.c`
   - `simulador.h`
   - `memoria.c`
   - `memoria.h`
   - `queue.c`
   - `queue.h`
   - `inputs_part2.c`
   - `makefile`

2. Certifique-se de ter o compilador GCC instalado no seu sistema.

## Como Compilar e Executar

1. Para compilar e executar o programa, basta executar os comandos:

```bash
make
```

```bash
./simulador
```

## Como Testar o Simulador

1. Inicialmente será exibido um menu para selecionar o input desejado. Exemplo de Menu:

Selecione o input desejado:
0 - input00
1 - input01
2 - input02
3 - input03
4 - input04
5 - input05
...
Escolha:

2. O utilizador deve digitar o número respectivo ao input pretendido para fazer a escolha.

3. Após a escolha do input, o simulador vai correr e será gerado o ficheiro de output. Exemplo de output:

time inst    proc1          proc2          proc3          proc4          proc5
1            NEW
2            NEW
3            RUN []           NEW
4            RUN []           NEW
5            RUN []           READY []

4. Além disso, o simulador também imprime mensagens de debug no terminal para acompanhar as instruções executadas por cada processo em cada instante. Exemplo de Debug:

DEBUG t=3: RUNNING Proc 1 (PC=2, Prg=1, Q=2) -> Executing instruction: 203
DEBUG t=4: RUNNING Proc 1 (PC=3, Prg=1, Q=1) -> Executing instruction: 4
DEBUG t=5: RUNNING Proc 2 (PC=1, Prg=3, Q=3) -> Executing instruction: -3