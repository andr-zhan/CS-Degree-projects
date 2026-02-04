# Simulador Gestor de Memória - Parte 1

Este projeto é um simulador de gestão de memória com paginação e memória virtual.

## Estrutura do Projeto

- **main.c**: Arquivo principal que implementa o simulador.
- **memoria.c** e **memoria.h**: Implementação do gestor de memória.
- **queue.c** e **queue.h**: Implementação das filas utilizadas no simulador.
- **inputs_part1.c**: Contém os diferentes inputs que podem ser usados para simular o comportamento do sistema.
- **makefile**: Arquivo para automatizar a compilação e execução do projeto.
- **fifoXX.out** e **lruXX.out**: Arquivos gerados pelo simulador contendo o estado dos processos em cada instante.

## Requisitos de execução

1. Certifique-se de que todos os arquivos necessários estão no mesmo diretório:
   - `main.c`
   - `memoria.c`
   - `memoria.h`
   - `queue.c`
   - `queue.h`
   - `inputs_part1.c`
   - `makefile`

2. Certifique-se de ter o compilador GCC instalado no seu sistema.

## Como Compilar e Executar

1. Para compilar e executar o programa, basta executar os comandos:

```bash
make
```

Se pretender usar o algoritmo **FIFO**:
```bash
./main fifo
```

Se pretender usar o algoritmo **LRU**:
```bash
./main lru
```

## Como Testar o Simulador

1. Inicialmente será exibido um menu para selecionar o input desejado. Exemplo de Menu:

Escolha o input:\n"
0 - inputP1Mem00/inputP1Exec00
1 - inputP1Mem01/inputP1Exec01
2 - inputP1Mem02/inputP1Exec02
3 - inputP1Mem03/inputP1Exec03
4 - inputP1Mem04/inputP1Exec04
5 - inputP1Mem05/inputP1Exec05
Seleção:

2. O utilizador deve digitar o número respectivo ao input pretendido para fazer a escolha.

3. Após a escolha do input, o simulador vai correr e será gerado o ficheiro de output. Exemplo de output:

time inst    proc1          proc2          proc3          proc4          proc5
1            F0
2            F0
3            F0             F1
4            F0             F1
5            F0             F1,F2