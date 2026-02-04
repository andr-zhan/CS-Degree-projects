#include <stdio.h>
#include <stdlib.h>
#include "queue.h"
#include <string.h>

// Definição do estado do processo
typedef enum {
    STATE_NEW,
    STATE_READY,
    STATE_RUNNING,
    STATE_BLOCKED,
    STATE_EXIT
} ProcessState;

// Função para converter o estado do processo em string para imprimir no ficheiro de output
const char* stateToStr(ProcessState state) {
    switch(state) {
        case STATE_NEW: return "NEW";
        case STATE_READY: return "READY";
        case STATE_RUNNING: return "RUN";
        case STATE_BLOCKED: return "BLOCKED";
        case STATE_EXIT: return "EXIT";
        default: return "";
    }
}

// Definição da estrutura do processo
typedef struct {
    int id;
    ProcessState state; // Estado do processo
    int program; // programa a executar 1-20
    int pc; // instrução a executar 1-11
    int quantum;
    int wait_time; // tempo de espera no estado NEW para implementar os dois instantes
    int block_time; // Tempo que o processo está no estado BLOCKED
    int exit_time; // flag para indicar se o processo está no estado EXIT
    int terminated; // flag para indicar se o processo terminou e não imprimir mais EXIT's
} Process;

#define MAX_PROCESSES 20
#define MAX_TIME 100
#define QUANTUM 3

int totalProcesses = 0; // Contador de processos usado para determinar o id de cada processo
Process *processList[MAX_PROCESSES] = {NULL}; // Lista para armazenar os processos

// Matriz que vai armazenar o input a executar
int programas[11][20];

// Carregar os inputs do ficheiro inputs.c
extern int input00[5][20];
extern int input01[5][20];
extern int input02[4][20];
extern int input03[5][20];
extern int input04[11][20];
extern int input05[11][20];

// Função para selecionar o input do ficheiro inputs.c
int selectInput(int programas[11][20]) {
    int choice;
    printf("Selecione o input desejado:\n");
    printf("1 - input00\n");
    printf("2 - input01\n");
    printf("3 - input02\n");
    printf("4 - input03\n");
    printf("5 - input04\n");
    printf("6 - input05\n");
    printf("Escolha: ");
    scanf("%d", &choice);

    switch (choice) {
        case 1:
            memcpy(programas, input00, sizeof(input00));
            break;
        case 2:
            memcpy(programas, input01, sizeof(input01));
            break;
        case 3:
            memcpy(programas, input02, sizeof(input02));
            break;
        case 4:
            memcpy(programas, input03, sizeof(input03));
            break;
        case 5:
            memcpy(programas, input04, sizeof(input04));
            break;
        case 6:
            memcpy(programas, input05, sizeof(input05));
            break;
        default:
            printf("Opção inválida. Usando input00 por padrão.\n");
            memcpy(programas, input00, sizeof(input00));
            choice = 1; // Define o padrão como input00
            break;
    }
    return choice; // Retorna o número do input escolhido para nomear o ficheiro de output
}

Queue *newQueue;
Queue *readyQueue;
Queue *blockedQueue;
Process *runningProcess = NULL;

// Função para obter a instrução atual do processo
int getInstruction(Process *process) {
    if (process->pc >= 11) return 0;
    return programas[process->pc][process->program - 1];
}

// Função para criar um novo processo
Process* createProcess(int program, int id) {
    Process *p = (Process*)malloc(sizeof(Process));
    if (p == NULL) {
        fprintf(stderr, "Erro ao alocar memória para processo.\n");
        exit(1);
    }
    p->id = id;
    p->program = program;
    p->pc = 0;
    p->quantum = QUANTUM;
    p->wait_time = 0;
    p->block_time = 0;
    p->exit_time = 0;
    p->state = STATE_NEW;
    p->terminated = 0;
    return p;
}

// Função para imprimir o estado dos processos
void print_output(FILE *out, int t) {
    fprintf(out, "%-13d", t);
    for (int i = 0; i < MAX_PROCESSES; i++) {
        if (processList[i] == NULL) {
            fprintf(out, "%-15s", "");
        } else {
            if (processList[i]->terminated) {
                fprintf(out, "%-15s", "   ");
            } else {
                fprintf(out, "%-15s", stateToStr(processList[i]->state));
            }
        }
    }
    fprintf(out, "\n");
}

int main(void) {

    // Seleciona o input a executar
    int inputChoice = selectInput(programas);

    // Cria o nome do ficheiro de output com base no input escolhido
    char outputFileName[20];
    sprintf(outputFileName, "output%02d.out", inputChoice - 1);

    // Abre o ficheiro de output
    FILE *out = fopen(outputFileName, "w");
    if (out == NULL) {
        fprintf(stderr, "Erro ao abrir o ficheiro %s\n", outputFileName);
        return 1;
    }

    // Cabeçalho do output
    fprintf(out, "%-13s", "time inst");
    for (int i = 1; i <= MAX_PROCESSES; i++) {
        char procHeader[10];
        sprintf(procHeader, "proc%d", i);
        fprintf(out, "%-15s", procHeader);
    }
    fprintf(out, "\n");

    // Inicializa as filas
    newQueue = createQueue();
    readyQueue = createQueue();
    blockedQueue = createQueue();

    // Cria o primeiro processo
    totalProcesses++;
    Process *p = createProcess(1, totalProcesses);
    processList[p->id - 1] = p;
    enqueue(newQueue, p);

    for (int t = 1; t <= MAX_TIME; t++) {

        // Algumas atualizações de estado e variáveis para imprimir os estados certos em cada instante
        for (int i = 0; i < MAX_PROCESSES; i++) {
            if (processList[i] != NULL) {
                if (processList[i]->block_time > 0) {
                    processList[i]->state = STATE_BLOCKED; // Atualização do estado para BLOCKED
                }

                if (processList[i]->block_time <= 0 && processList[i]->state == STATE_BLOCKED) {
                    processList[i]->state = STATE_READY; // Atualização do estado para READY
                }

                if (processList[i]->quantum > 3) {
                    processList[i]->state = STATE_READY; // Atualização do estado para READY após ele sair do estado RUNNING
                    processList[i]->quantum = QUANTUM; // Atualização do quantum pois foi necessário atribuir QUANTUM + 1
                }

                if (processList[i]->exit_time == 1) {
                    processList[i]->state = STATE_EXIT; // Atualização do estado para EXIT
                }


            }
        }

        // Utilização da readyQueue para atualizar o estado do processo para READY após 2 instantes no estado NEW
        Queue *tempQueue = createQueue(); // Fila temporária para preservar os elementos da readyQueue
        while (!isEmpty(readyQueue)) {
            Process *proc = (Process*)dequeue(readyQueue);

            // Atualização do estado para READY
            if (proc->state != STATE_READY) {
            proc->state = STATE_READY;
            }

            enqueue(tempQueue, proc);
        }

        // Restaura os elementos para a readyQueue
        while (!isEmpty(tempQueue)) {
            enqueue(readyQueue, dequeue(tempQueue));
        }
        deleteQueue(tempQueue);

        // READY → RUNNING
        if (runningProcess == NULL && !isEmpty(readyQueue)) {
            runningProcess = (Process*)dequeue(readyQueue);
            runningProcess->state = STATE_RUNNING;
            runningProcess->quantum = QUANTUM;
        }

        // RUNNING (Instruções)
        if (runningProcess != NULL && runningProcess->quantum > 0) {
            int instr = getInstruction(runningProcess);
            // Início de debug para saber as instruções que estão a ser executadas em cada instante
            fprintf(stderr, "DEBUG t=%d: RUNNING Proc %d (PC=%d, Prg=%d, Q=%d) -> Executing instruction: %d\n",
                    t,
                    runningProcess->id,
                    runningProcess->pc,
                    runningProcess->program,
                    runningProcess->quantum,
                    instr);
            // Fim de debug
            runningProcess->quantum--; // Decrementa o quantum do processo

            // HALT
            if (instr == 0) {
                runningProcess->exit_time = 1;
                runningProcess->pc++;
                runningProcess = NULL;
            }

            // JUMP
            else if (instr >= 101 && instr <= 199) {
                int jump = instr % 100;
                runningProcess->pc -= jump;
                if (runningProcess->pc < 0) runningProcess->pc = 0;
            }

            // EXEC
            else if (instr >= 201 && instr <= 299) {
                int progToExec = instr % 100;
                if (totalProcesses < MAX_PROCESSES) {
                    totalProcesses++;
                    Process *newProc = createProcess(progToExec, totalProcesses);
                    processList[newProc->id - 1] = newProc;
                    enqueue(newQueue, newProc);
                }
                runningProcess->pc++;
            }

            // I/O
            else if (instr < 0) {
                runningProcess->block_time = abs(instr) + 1;
                runningProcess->pc++;
                enqueue(blockedQueue, runningProcess);
                runningProcess = NULL;
            }

            // Outra instrução positiva
            else {
                runningProcess->pc++;
            }  
        }

        // BLOCKED → READY (1ª Prioridade)
        Queue *tempBlocked = createQueue();
        while (!isEmpty(blockedQueue)) {
            Process *proc = (Process*)dequeue(blockedQueue);
            proc->block_time--; // Decrementa o tempo de bloqueio do processo
            if (proc->block_time <= 0) {
                enqueue(readyQueue, proc);
            } else {
                enqueue(tempBlocked, proc);
            }
        }
        deleteQueue(blockedQueue);
        blockedQueue = tempBlocked;

        // NEW → READY (2ª Prioridade)
        Queue *tempNew = createQueue();
        while (!isEmpty(newQueue)) {
            Process *proc = (Process*)dequeue(newQueue);
            proc->wait_time++; // Incrementa o tempo de espera para implementar os dois instantes no estado NEW em processos recém-criados
            if (proc->wait_time >= 2) {
                enqueue(readyQueue, proc);
            } else {
                enqueue(tempNew, proc);
            }
        }
        deleteQueue(newQueue);
        newQueue = tempNew;

        // RUNNING → READY (Se quantum = 0) (3ª Prioridade)
        if (runningProcess != NULL && runningProcess->quantum <= 0) {
            runningProcess->quantum = QUANTUM + 1; // +1 Necessário como flag, para fazer a atualização do estado para READY no início do loop
            enqueue(readyQueue, runningProcess);
            runningProcess = NULL;
        }

        // Imprimir o output
        print_output(out, t);

        // EXIT → terminated (para não aparecer mais exit's ao imprimir os estados)
        for (int i = 0; i < totalProcesses; i++) {
            if (processList[i] != NULL && processList[i]->state == STATE_EXIT) {
                processList[i]->exit_time--;
                if (processList[i]->exit_time <= 0) {
                    processList[i]->terminated = 1;
                }
            }
        }
    }

    // Cleanup
    for (int i = 0; i < totalProcesses; i++) {
        if (processList[i] != NULL) {
            free(processList[i]);
        }
    }

    deleteQueue(newQueue);
    deleteQueue(readyQueue);
    deleteQueue(blockedQueue);
    fclose(out);

    return 0;
}
