#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "memoria.h"
#include "queue.h"
#include "inputs_part1.c"


/**
 * Imprime o estado da memória e dos processos num dado instante.
 * Mostra, para cada processo, as frames ocupadas (ordenadas pelo número da página)
 * ou "SIGSEGV" se o processo terminou por violação de memória.
 *
 * @param instante Instante de tempo atual.
 * @param memoria Vetor de frames da memória.
 * @param terminado Vetor que indica se cada processo terminou (e em que instante).
 * @param num_proc Número total de processos.
 */
void imprimir_estado(int instante, Frame memoria[], int terminado[], int num_proc) {
    printf("%-10d", instante); // Imprime o instante de tempo

    // Percorrer todos os processos
    for (int pid = 1; pid <= num_proc; pid++) {
        // Se o processo já terminou
        if (terminado[pid]) {
            // Se terminou neste instante, imprime "SIGSEGV"
            if (terminado[pid] == instante)
                printf("%-19s", "SIGSEGV");
            else
                // Caso contrário, imprime espaço vazio
                printf("%-19s", "");
            continue;
        }

        // Arrays auxiliares para guardar os índices das frames e os números das páginas
        int frames[FRAMES_TOTAL], pags[FRAMES_TOTAL], count = 0;

        // Procurar todas as frames ocupadas por este processo
        for (int i = 0; i < FRAMES_TOTAL; i++) {
            if (memoria[i].ocupada && memoria[i].proc_id == pid) {
                frames[count] = i;                  // Guarda o índice da frame
                pags[count++] = memoria[i].pag_num; // Guarda o número da página
            }
        }

        // Ordena as frames por ordem crescente do número da página
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (pags[j] > pags[j + 1]) {
                    int tmp = pags[j]; pags[j] = pags[j + 1]; pags[j + 1] = tmp;
                    tmp = frames[j]; frames[j] = frames[j + 1]; frames[j + 1] = tmp;
                }
            }
        }

        // Constrói a string com as frames ocupadas, no formato F0,F2,F5,...
        char buffer[64] = "";
        for (int i = 0; i < count; i++) {
            char temp[8];
            sprintf(temp, "%sF%d", i == 0 ? "" : ",", frames[i]);
            strcat(buffer, temp);
        }
        printf("%-19s", buffer);
    }
    printf("\n");
}


/**
 * Função principal do simulador de memória.
 * Lê argumentos, seleciona o input, executa as instruções e imprime o estado da memória.
 *
 * @param argc Número de argumentos da linha de comandos.
 * @param argv Vetor de argumentos da linha de comandos.
 * @return 0 em caso de sucesso, 1 em caso de erro.
 */
int main(int argc, char *argv[]) {

    // Verificar se foi passado o argumento "fifo" ou "lru"
    if (argc < 2) {
        printf("Uso: %s <fifo|lru>\n", argv[0]);
        return 1;
    }

    int usar_fifo = strcmp(argv[1], "fifo") == 0;
    int usar_lru  = strcmp(argv[1], "lru")  == 0;

    if (!usar_fifo && !usar_lru) {
        printf("Erro: argumento inválido. Use 'fifo' ou 'lru'\n");
        return 1;
    }

    // Menu de seleção de input
    int escolha = 0;
    do {
    printf("Escolha o input:\n");
    printf("0 - inputP1Mem00/inputP1Exec00\n");
    printf("1 - inputP1Mem01/inputP1Exec01\n");
    printf("2 - inputP1Mem02/inputP1Exec02\n");
    printf("3 - inputP1Mem03/inputP1Exec03\n");
    printf("4 - inputP1Mem04/inputP1Exec04\n");
    printf("5 - inputP1Mem05/inputP1Exec05\n");
    printf("Seleção: ");
    scanf("%d", &escolha);
    if (escolha < 0 || escolha > 5) {printf("Seleção inválida. Tente novamente.\n");}
    } while (escolha < 0 || escolha > 5);
    
    // Definir o nome do ficheiro de output conforme o input escolhido
    char nome_ficheiro[32];
    snprintf(nome_ficheiro, sizeof(nome_ficheiro), "%s%02d.out", usar_fifo ? "fifo" : "lru", escolha);
    freopen(nome_ficheiro, "w", stdout);

    // Seleciona os arrays de input e o número de processos conforme a escolha
    int *inputMem = NULL, *inputExec = NULL, num_proc = 0, tam_exec = 0;
    switch (escolha) {
        case 0:
            inputMem = inputP1Mem00; inputExec = inputP1Exec00; num_proc = 5;
            tam_exec = sizeof(inputP1Exec00) / sizeof(int);
            break;
        case 1:
            inputMem = inputP1Mem01; inputExec = inputP1Exec01; num_proc = 5;
            tam_exec = sizeof(inputP1Exec01) / sizeof(int);
            break;
        case 2:
            inputMem = inputP1Mem02; inputExec = inputP1Exec02; num_proc = 5;
            tam_exec = sizeof(inputP1Exec02) / sizeof(int);
            break;
        case 3:
            inputMem = inputP1Mem03; inputExec = inputP1Exec03; num_proc = 10;
            tam_exec = sizeof(inputP1Exec03) / sizeof(int);
            break;
        case 4:
            inputMem = inputP1Mem04; inputExec = inputP1Exec04; num_proc = 20;
            tam_exec = sizeof(inputP1Exec04) / sizeof(int);
            break;
        case 5:
            inputMem = inputP1Mem05; inputExec = inputP1Exec05; num_proc = 3;
            tam_exec = sizeof(inputP1Exec05) / sizeof(int);
            break;
        default:
            printf("Erro ao selecionar o input.\n");
            return 1;
    }

    // Vetor para marcar se cada processo terminou (e em que instante)
    int terminado[num_proc + 1];
    memset(terminado, 0, sizeof(terminado));

    // Inicializar a memória e a fila FIFO (se necessário)
    Frame memoria[FRAMES_TOTAL];
    inicializar_memoria(memoria);
    Queue* fifo_queue = usar_fifo ? createQueue() : NULL;

    int instante = 0;

    // Imprime o cabeçalho da tabela de output
    printf("%-10s", "time inst");
    char proc_name[16];
    for (int i = 1; i <= num_proc; i++) {
        snprintf(proc_name, sizeof(proc_name), "proc%d", i);
        printf("%-19s", proc_name);
    }
    printf("\n");

    // Ciclo principal de execução
    for (int i = 0; i < tam_exec; i += 2) {
        int proc_id = inputExec[i];
        int endereco = inputExec[i + 1];

        // Se o processo já terminou, imprime apenas o estado
        if (terminado[proc_id]) {
            imprimir_estado(instante++, memoria, terminado, num_proc);
            continue;
        }

        int limite = inputMem[proc_id - 1];

        // Se o endereço é inválido, termina o processo e liberta as suas frames
        if (endereco >= limite) {
            terminado[proc_id] = instante;
            libertar_frames_processo(memoria, proc_id);
            imprimir_estado(instante++, memoria, terminado, num_proc);
            continue;
        }

        int pag_num = endereco / TAM_FRAME;

        // Se a página não está na memória, tenta carregá-la
        if (!pagina_esta_na_memoria(memoria, proc_id, pag_num, instante, usar_lru)) {
            int frame = carregar_pagina(memoria, proc_id, pag_num, instante);

            // Se a frame foi carregada com sucesso
            if (frame != -1) {
                // Se for FIFO, adiciona a frame à fila
                if (usar_fifo) {
                    int *frame_id = malloc(sizeof(int));
                    *frame_id = frame;
                    enqueue(fifo_queue, frame_id);
                }
            } else {
                 // Se não houver frames livres, faz substituição
                if (usar_fifo) {
                    substituir_pagina_fifo(memoria, proc_id, pag_num, fifo_queue);
                } else if (usar_lru) {
                    substituir_pagina_lru(memoria, proc_id, pag_num, instante);
                }
            }
        }

        // Imprime o estado da memória após cada instrução
        imprimir_estado(instante++, memoria, terminado, num_proc);
    }

    // Cleanup
    if (fifo_queue != NULL) {deleteQueue(fifo_queue);}

    return 0;
}
