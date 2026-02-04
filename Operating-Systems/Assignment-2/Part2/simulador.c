#include <stdio.h>
#include <stdlib.h>
#include "simulador.h"
#include "queue.h"
#include "memoria.h"
#include <string.h>

// Carregar os inputs do ficheiro inputs_part2.c
extern int input00[8][20];
extern int input01[6][20];
extern int input02[5][20];
extern int input03[6][20];
extern int input04[6][20];
extern int input05[6][20];
extern int input06[5][20];
extern int input07[12][20];
extern int input08[12][20];
extern int input09[12][20];
extern int input10[12][20];
extern int input11[12][20];


// Matriz que vai armazenar o input a executar
int programas[12][20];


int totalProcesses = 0;                         // Contador de processos usado para determinar o id de cada processo
Process *processList[MAX_PROCESSES] = {NULL};   // Lista para armazenar os processos
Queue *newQueue;                                // Fila para processos no estado NEW
Queue *readyQueue;                              // Fila para processos no estado READY
Queue *blockedQueue;                            // Fila para processos no estado BLOCKED
Process *runningProcess = NULL;                 // Processo atualmente em execução


/**
 * Converte o estado de um processo para uma string legível.
 *
 * @param state Estado do processo.
 * @return Ponteiro para string constante correspondente ao estado.
 */
const char* stateToStr(ProcessState state) {
    switch(state) {
        case STATE_NEW: return "NEW";
        case STATE_READY: return "READY";
        case STATE_RUNNING: return "RUN";
        case STATE_BLOCKED: return "BLOCKED";
        case STATE_EXIT: return "EXIT";
        case STATE_SIGSEGV: return "SIGSEGV";
        case STATE_SIGILL: return "SIGILL";
        case STATE_SIGEOF: return "SIGEOF";
        default: return "";
    }
}


/**
 * Seleciona o input a ser usado pelo simulador, copia-o para a matriz 'programas'
 * e devolve o número de linhas (número de instruções) e colunas (número de programas) 
 * desse input.
 *
 * @param programas Matriz destino para o input selecionado.
 * @param num_linhas Ponteiro para inteiro onde será guardado o número de linhas do input.
 * @param num_colunas Ponteiro para inteiro onde será guardado o número de colunas do input.
 * @return Número do input escolhido (para nomear o ficheiro de output).
 */
int selectInput(int programas[12][20], int *num_linhas, int *num_colunas) {
    int choice;
    printf("Selecione o input desejado:\n");
    printf("0 - input00\n");
    printf("1 - input01\n");
    printf("2 - input02\n");
    printf("3 - input03\n");
    printf("4 - input04\n");
    printf("5 - input05\n");
    printf("6 - input06\n");
    printf("7 - input07\n");
    printf("8 - input08\n");
    printf("9 - input09\n");
    printf("10 - input10\n");
    printf("11 - input11\n");
    printf("Escolha: ");
    scanf("%d", &choice);

    switch (choice) {
        case 0:
            memcpy(programas, input00, sizeof(input00)); // Copia o conteúdo do array input00
            *num_linhas = sizeof(input00) / sizeof(input00[0]);
            *num_colunas = sizeof(input00[0]) / sizeof(int);
            break;
        case 1:
            memcpy(programas, input01, sizeof(input01));
            *num_linhas = sizeof(input01) / sizeof(input01[0]);
            *num_colunas = sizeof(input01[0]) / sizeof(int);
            break;
        case 2:
            memcpy(programas, input02, sizeof(input02));
            *num_linhas = sizeof(input02) / sizeof(input02[0]);
            *num_colunas = sizeof(input02[0]) / sizeof(int);
            break;
        case 3:
            memcpy(programas, input03, sizeof(input03));
            *num_linhas = sizeof(input03) / sizeof(input03[0]);
            *num_colunas = sizeof(input03[0]) / sizeof(int);
            break;
        case 4:
            memcpy(programas, input04, sizeof(input04));
            *num_linhas = sizeof(input04) / sizeof(input04[0]);
            *num_colunas = sizeof(input04[0]) / sizeof(int);
            break;
        case 5:
            memcpy(programas, input05, sizeof(input05));
            *num_linhas = sizeof(input05) / sizeof(input05[0]);
            *num_colunas = sizeof(input05[0]) / sizeof(int);
            break;
        case 6:
            memcpy(programas, input06, sizeof(input06));
            *num_linhas = sizeof(input06) / sizeof(input06[0]);
            *num_colunas = sizeof(input06[0]) / sizeof(int);
            break;
        case 7:
            memcpy(programas, input07, sizeof(input07));
            *num_linhas = sizeof(input07) / sizeof(input07[0]);
            *num_colunas = sizeof(input07[0]) / sizeof(int);
            break;
        case 8:
            memcpy(programas, input08, sizeof(input08));
            *num_linhas = sizeof(input08) / sizeof(input08[0]);
            *num_colunas = sizeof(input08[0]) / sizeof(int);
            break;
        case 9:
            memcpy(programas, input09, sizeof(input09));
            *num_linhas = sizeof(input09) / sizeof(input09[0]);
            *num_colunas = sizeof(input09[0]) / sizeof(int);
            break;
        case 10:
            memcpy(programas, input10, sizeof(input10));
            *num_linhas = sizeof(input10) / sizeof(input10[0]);
            *num_colunas = sizeof(input10[0]) / sizeof(int);
            break;
        case 11:
            memcpy(programas, input11, sizeof(input11));
            *num_linhas = sizeof(input11) / sizeof(input11[0]);
            *num_colunas = sizeof(input11[0]) / sizeof(int);
            break;
        default:
            printf("Opção inválida.\n");
            choice = -1;
            break;
    }
    return choice; // Retorna o número do input escolhido
}


/**
 * Obtém a instrução atual do processo, de acordo com o seu program counter (pc) e programa.
 *
 * @param process Ponteiro para o processo.
 * @return Valor da instrução atual, ou 0 se o program counter estiver fora dos limites.
 */
int getInstruction(Process *process) {
    if (process->pc < 0 || process->pc >= process->program_length) return 0;
    return programas[process->pc][process->program - 1];
}


/**
 * Cria e inicializa um novo processo.
 *
 * @param program Número do programa a executar.
 * @param id Identificador único do processo.
 * @param num_instr Número de instruções (linhas) do programa.
 * @return Ponteiro para o novo processo criado, ou NULL em caso de erro.
 */
Process* createProcess(int program, int id, int num_instr) {
    Process *p = (Process*)malloc(sizeof(Process));

    if (p == NULL) {
        fprintf(stderr, "Erro ao alocar memória para processo.\n");
        exit(1);
    }

    p->id = id;
    p->program = program;
    p->pc = 1;
    p->quantum = QUANTUM;
    p->wait_time = 0;
    p->block_time = 0;
    p->exit_time = 0;
    p->state = STATE_NEW;
    p->terminated = 0;
    p->address_space = programas[0][program - 1];
    p->program_length = num_instr;

    // Verifica se o espaço de endereço é válido
    if (p->address_space < 0 || p->address_space > MAX_ADDRESS_SPACE) {
        free(p);
        return NULL;
    }

    return p;
}


/**
 * Lista as frames ocupadas por um processo, ordenadas pelo número da página,
 * no output.
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo.
 * @param buffer Buffer de destino para a string resultante.
 * @param bufsize Tamanho do buffer.
 */
void listar_frames_processo(Frame memoria[], int proc_id, char *buffer, size_t bufsize) {
    int frames[FRAMES_TOTAL];
    int paginas[FRAMES_TOTAL];
    int count = 0;

    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (memoria[i].ocupada && memoria[i].proc_id == proc_id) {
            frames[count] = memoria[i].id;
            paginas[count] = memoria[i].pag_num;
            count++;
        }
    }

    if (count == 0) {
        snprintf(buffer, bufsize, "[]");
        return;
    }

    // Ordenar por ordem crescente do número da página
    for (int i = 0; i < count - 1; i++) {
        for (int j = 0; j < count - i - 1; j++) {
            if (paginas[j] > paginas[j + 1]) {
                // Troca páginas
                int tmp_pag = paginas[j];
                paginas[j] = paginas[j + 1];
                paginas[j + 1] = tmp_pag;
                // Troca frames correspondentes
                int tmp_fr = frames[j];
                frames[j] = frames[j + 1];
                frames[j + 1] = tmp_fr;
            }
        }
    }

    // Construir a string
    int pos = 0;
    pos += snprintf(buffer + pos, bufsize - pos, "[");
    for (int i = 0; i < count; i++) {
        pos += snprintf(buffer + pos, bufsize - pos, "F%d%s", frames[i], (i < count - 1) ? "," : "");
    }
    snprintf(buffer + pos, bufsize - pos, "]");
}


/**
 * Imprime o estado de todos os processos e as frames associadas a cada um, 
 * para um dado instante.
 *
 * @param out Ficheiro de output.
 * @param t Instante de tempo atual.
 * @param memoria Vetor de frames da memória.
 */
void print_output(FILE *out, int t, Frame memoria[]) {
    fprintf(out, "%-13d", t);

    for (int i = 0; i < MAX_PROCESSES; i++) {
        if (processList[i] == NULL) {
            fprintf(out, "%-20s", "");

        } else if (processList[i]->terminated) {
            fprintf(out, "%-20s", "");

        } else {
            const char *estado = stateToStr(processList[i]->state);

            if (processList[i]->state == STATE_NEW) {
                fprintf(out, "%-20s", estado);

            } else {
                char frames_str[64];
                listar_frames_processo(memoria, processList[i]->id, frames_str, sizeof(frames_str));
                char estado_frames[80];
                snprintf(estado_frames, sizeof(estado_frames), "%s %s", estado, frames_str);
                fprintf(out, "%-20s", estado_frames);
            }
        }
    }

    fprintf(out, "\n");
}


int main(void) {

    // Inicializar a memória
    Frame memoria[FRAMES_TOTAL];
    inicializar_memoria(memoria);

    // Selecionar o input a executar
    int num_instr = 0, num_programas = 0;
    memset(programas, 0, sizeof(programas));
    int inputChoice = -1;
    do {
        inputChoice = selectInput(programas, &num_instr, &num_programas);
    } while (inputChoice == -1);

    // Criar o nome do ficheiro de output com base no input escolhido
    char outputFileName[20];
    sprintf(outputFileName, "output2T%02d.out", inputChoice);

    // Abrir o ficheiro de output
    FILE *out = fopen(outputFileName, "w");
    if (out == NULL) {
        fprintf(stderr, "Erro ao abrir o ficheiro %s\n", outputFileName);
        return 1;
    }

    // Cabeçalho do output
    fprintf(out, "%-13s", "time inst");
    for (int i = 1; i <= MAX_PROCESSES; i++) {
        char procHeader[16];
        sprintf(procHeader, "proc%d", i);
        fprintf(out, "%-20s", procHeader);
    }
    fprintf(out, "\n");

    // Inicializar as filas
    newQueue = createQueue();
    readyQueue = createQueue();
    blockedQueue = createQueue();

    // Criar o primeiro processo
    totalProcesses++;
    Process *p = createProcess(1, totalProcesses, num_instr);
    processList[p->id - 1] = p;
    enqueue(newQueue, p);

    // Ciclo principal de simulação
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

                if (processList[i]->exit_time > 0) {
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
            if (runningProcess->pc >= runningProcess->program_length) {
                runningProcess->state = STATE_SIGEOF;
                runningProcess->exit_time = EXIT_TIME;
                runningProcess = NULL;
            } else {
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
                    runningProcess->exit_time = EXIT_TIME;
                    runningProcess->pc++;
                    runningProcess = NULL;
                }

                // JUMPB
                else if (instr >= 101 && instr <= 199) {
                    int jump = instr % 100;
                    runningProcess->pc -= jump;
                    if (runningProcess->pc < 0) {
                        // Erro SIGILL
                        runningProcess->state = STATE_SIGILL;
                        runningProcess->exit_time = EXIT_TIME;
                        runningProcess = NULL;
                    }
                }

                // JUMPF
                else if (instr >= 1 && instr <= 100) {
                    int jump = instr % 100;
                    runningProcess->pc += jump;
                    if (runningProcess->pc < 0 || runningProcess->pc >= runningProcess->program_length) {
                        // Erro SIGILL
                        runningProcess->state = STATE_SIGILL;
                        runningProcess->exit_time = EXIT_TIME;
                        runningProcess = NULL;
                    }
                }

                // EXEC
                else if (instr >= 201 && instr <= 299) {
                    int progToExec = instr % 100;
                    if (totalProcesses < MAX_PROCESSES && progToExec <= num_programas) {
                        totalProcesses++;
                        Process *newProc = createProcess(progToExec, totalProcesses, num_instr);
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

                // LOAD/STORE
                else if (instr >= 1000 && instr <= 15999) {
                    int endereço = instr - 1000;

                    if (endereço >= runningProcess->address_space) {
                        // Erro SIGSEGV
                        runningProcess->state = STATE_SIGSEGV;
                        runningProcess->exit_time = EXIT_TIME;
                        runningProcess = NULL;

                    } else {
                        int pagina = endereço / TAM_FRAME;
                        if (!pagina_esta_na_memoria(memoria, runningProcess->id, pagina, t, 1)) {
                            int frame = carregar_pagina(memoria, runningProcess->id, pagina, t);
                            if (frame == -1) {
                                substituir_pagina_lru(memoria, runningProcess->id, pagina, t);
                            }
                        }
                        runningProcess->pc++;
                    }
                }

                // SWAP/MEMCPY
                else if (instr >= 1000000000 && instr <= 2109999999) {
                    int primeiro_end = (instr / 100000) % 100000 - 10000;
                    int segundo_end = instr % 100000;

                    if (primeiro_end < 0 || primeiro_end >= runningProcess->address_space ||
                        segundo_end < 0 || segundo_end >= runningProcess->address_space) {
                        // Erro SIGSEGV
                        runningProcess->state = STATE_SIGSEGV;
                        runningProcess->exit_time = EXIT_TIME;
                        runningProcess = NULL;

                    } else {
                        int pag1 = primeiro_end / TAM_FRAME;
                        int pag2 = segundo_end / TAM_FRAME;

                        // Verifica se as páginas estão na memória, sem atualizar o timestamp
                        int pag1_na_mem = pagina_esta_na_memoria(memoria, runningProcess->id, pag1, t, 0);
                        int pag2_na_mem = pagina_esta_na_memoria(memoria, runningProcess->id, pag2, t, 0);

                        // Se as páginas são diferentes
                        if (pag1 != pag2) {

                            // CASO 1: ambas as páginas estão na memória
                            if (pag1_na_mem && pag2_na_mem) {
                                // Atualizar o timestamp das páginas
                                pagina_esta_na_memoria(memoria, runningProcess->id, pag1, t, 1);
                                pagina_esta_na_memoria(memoria, runningProcess->id, pag2, t, 1);
                            }

                            // CASO 2: nenhuma está na memória
                            if (!pag1_na_mem && !pag2_na_mem) {
                                int frame1 = carregar_pagina(memoria, runningProcess->id, pag1, t);
                                if (frame1 == -1) {
                                    frame1 = substituir_pagina_lru(memoria, runningProcess->id, pag1, t);
                                }
                                int frame2 = carregar_pagina(memoria, runningProcess->id, pag2, t);
                                if (frame2 == -1) {
                                    frame2 = substituir_pagina_lru(memoria, runningProcess->id, pag2, t);
                                }
                            }

                            // CASO 3: apenas uma página está na memória
                            if (pag1_na_mem && !pag2_na_mem) { // pagina 2 não está na memória
                                pagina_esta_na_memoria(memoria, runningProcess->id, pag1, t, 1);
                                int frame2 = carregar_pagina(memoria, runningProcess->id, pag2, t);
                                if (frame2 == -1) {
                                    frame2 = substituir_pagina_lru_excluindo(memoria, runningProcess->id, pag2, t, pag1);
                                }
                            } else if (!pag1_na_mem && pag2_na_mem) { // pagina 1 não está na memória
                                int frame1 = carregar_pagina(memoria, runningProcess->id, pag1, t);
                                if (frame1 == -1) {
                                    frame1 = substituir_pagina_lru_excluindo(memoria, runningProcess->id, pag1, t, pag2);
                                }
                                pagina_esta_na_memoria(memoria, runningProcess->id, pag2, t, 1);
                            }

                        } else {
                            // Se as páginas são iguais, carregar apenas a primeira, para evitar redundância
                            if (!pagina_esta_na_memoria(memoria, runningProcess->id, pag1, t, 1)) {
                                int frame = carregar_pagina(memoria, runningProcess->id, pag1, t);
                                if (frame == -1) {
                                    substituir_pagina_lru(memoria, runningProcess->id, pag1, t);
                                }
                            }
                        }
                        runningProcess->pc++;
                    }
                }

                // Outra instrução positiva
                else {
                    runningProcess->pc++;
                }
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
            if (proc->wait_time >= NEW_TIME) {
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
        print_output(out, t, memoria);

        // EXIT → terminated (para não aparecer mais exit's ao imprimir os estados)
        for (int i = 0; i < totalProcesses; i++) {
            if (processList[i] != NULL && processList[i]->state == STATE_EXIT) {
                processList[i]->exit_time--;
                if (processList[i]->exit_time <= 0) {
                    processList[i]->terminated = 1;
                    libertar_frames_processo(memoria, processList[i]->id);
                }
            }
        }

        // Terminar a simulação se todos os processos estiverem terminados
        int todos_terminados = 1;
        for (int i = 0; i < totalProcesses; i++) {
            if (processList[i] != NULL && !processList[i]->terminated) {
                todos_terminados = 0;
                break;
            }
        }
        if (todos_terminados) break;
    }

    // Cleanup
    for (int i = 0; i < totalProcesses; i++) {
        if (processList[i] != NULL) {
            libertar_frames_processo(memoria, processList[i]->id);
            free(processList[i]);
        }
    }

    deleteQueue(newQueue);
    deleteQueue(readyQueue);
    deleteQueue(blockedQueue);
    fclose(out);

    return 0;
}
