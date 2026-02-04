#ifndef SIMULADOR_H
#define SIMULADOR_H


#include <stdio.h>
#include <stddef.h>
#include "memoria.h"


#define MAX_PROCESSES 20        // Número máximo de processos
#define MAX_TIME 100            // Tempo máximo de simulação
#define QUANTUM 3               // Quantum para o escalonamento de processos
#define MAX_ADDRESS_SPACE 11000 // Espaço máximo de endereços para cada processo (11kB = 11000 Bytes)
#define NEW_TIME 2              // Tempo que o processo fica no estado NEW antes de ir para a readyQueue
#define EXIT_TIME 3             // Tempo que o processo fica no estado EXIT


// Estados possíveis de um processo
typedef enum {
    STATE_NEW,
    STATE_READY,
    STATE_RUNNING,
    STATE_BLOCKED,
    STATE_EXIT,
    STATE_SIGSEGV,
    STATE_SIGILL,
    STATE_SIGEOF
} ProcessState;

// Estrutura para representar um processo
typedef struct {
    int id;             // ID do processo (1-20)
    ProcessState state; // Estado do processo
    int program;        // programa a executar
    int pc;             // instrução a executar
    int quantum;        // Quantum restante do processo
    int wait_time;      // tempo de espera no estado NEW para implementar os dois instantes
    int block_time;     // Tempo que o processo está no estado BLOCKED
    int exit_time;      // flag para indicar se o processo está no estado EXIT
    int terminated;     // flag para indicar se o processo terminou e não imprimir mais EXIT's
    int address_space;  // Espaço de endereço do processo
    int program_length; // Comprimento do programa (número de instruções)
} Process;


// Função para selecionar o input a executar
int selectInput(int programas[12][20], int *num_linhas, int *num_colunas);

// Funções para manipulação de processos
Process* createProcess(int program, int id, int num_instr);
int getInstruction(Process *process);

// Funções para imprimir o output
void listar_frames_processo(Frame memoria[], int proc_id, char *buffer, size_t bufsize);
void print_output(FILE *out, int t, Frame memoria[]);
const char* stateToStr(ProcessState state);

// Main
int main(void);

#endif