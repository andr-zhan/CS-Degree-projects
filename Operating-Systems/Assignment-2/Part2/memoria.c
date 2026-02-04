#include "memoria.h"
#include <stdio.h>
#include <string.h>
#include "queue.h"


/**
 * Inicializa todas as frames da memória como livres.
 *
 * @param memoria Vetor de frames da memória.
 */
void inicializar_memoria(Frame memoria[]) {
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        memoria[i].id = i;    
        memoria[i].ocupada = 0;
        memoria[i].proc_id = -1;
        memoria[i].pag_num = -1;
        memoria[i].timestamp = -1;
    }
}


/**
 * Carrega uma página na primeira frame livre encontrada, procurando
 * por ordem crescente do índice.
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo que vai ocupar a página.
 * @param pag_num Número da página a carregar.
 * @param instante Instante de tempo atual (para LRU).
 * @return Índice da frame onde a página foi carregada, ou -1 se não houver frames livres.
 */
int carregar_pagina(Frame memoria[], int proc_id, int pag_num, int instante) {
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (!memoria[i].ocupada) {
            memoria[i].ocupada = 1;
            memoria[i].proc_id = proc_id;
            memoria[i].pag_num = pag_num;
            memoria[i].timestamp = instante;
            return i; // Sucesso, retorna índice da frame
        }
    }
    return -1; // Falha, memória cheia
}


/**
 * Liberta todas as frames associadas a um determinado processo.
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo cujas frames devem ser libertadas.
 */
void libertar_frames_processo(Frame memoria[], int proc_id) {
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (memoria[i].proc_id == proc_id) {
            memoria[i].ocupada = 0;
            memoria[i].proc_id = -1;
            memoria[i].pag_num = -1;
            memoria[i].timestamp = -1;
        }
    }
}


/**
 * Verifica se uma página de um processo está atualmente na memória.
 * Se usar LRU, atualiza o timestamp da frame.
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo.
 * @param pag_num Número da página.
 * @param instante Instante de tempo atual (para LRU).
 * @param usar_lru Se diferente de zero, atualiza o timestamp.
 * @return 1 se a página está na memória, 0 caso contrário.
 */
int pagina_esta_na_memoria(Frame memoria[], int proc_id, int pag_num, int instante, int usar_lru) {
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (memoria[i].ocupada &&
            memoria[i].proc_id == proc_id &&
            memoria[i].pag_num == pag_num) {
            
            if (usar_lru) memoria[i].timestamp = instante; // Atualiza timestamp, se for usado LRU
            return 1;
        }
    }
    return 0;
}


/**
 * Substitui a página segundo o algoritmo FIFO.
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo.
 * @param pag_num Número da página a carregar.
 * @param fifo_queue Fila FIFO das frames.
 * @return Índice da frame substituída, ou -1 se não for possível substituir.
 */
int substituir_pagina_fifo(Frame memoria[], int proc_id, int pag_num, Queue *fifo_queue) {
    int *frame_id_ptr = (int *) dequeue(fifo_queue); // Remove da queue
    if (frame_id_ptr == NULL) return -1;

    int idx = *frame_id_ptr;
    free(frame_id_ptr); // Liberta o ponteiro da queue

    // Substituir a frame
    memoria[idx].proc_id = proc_id;
    memoria[idx].pag_num = pag_num;
    memoria[idx].timestamp = 0;
    memoria[idx].ocupada = 1;

    // Adicionar de novo à queue
    int *new_id = malloc(sizeof(int));
    *new_id = idx;
    enqueue(fifo_queue, new_id);

    return idx;
}


/**
 * Substitui a página menos recentemente usada (LRU).
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo.
 * @param pag_num Número da página a carregar.
 * @param instante Instante de tempo atual (para LRU).
 * @return Índice da frame substituída, ou -1 se não for possível substituir.
 */
int substituir_pagina_lru(Frame memoria[], int proc_id, int pag_num, int instante) {
    int menor_timestamp = __INT_MAX__;
    int frame_menos_usada = -1;

    // Encontrar a frame com o menor timestamp
    // Em caso de empate, escolhe a primeira encontrada, que por sua vez será a de menor índice
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (memoria[i].ocupada && memoria[i].timestamp < menor_timestamp) {
            menor_timestamp = memoria[i].timestamp;
            frame_menos_usada = i;
        }
    }

    if (frame_menos_usada == -1) return -1; // Falha

    // Substituir a frame
    memoria[frame_menos_usada].proc_id = proc_id;
    memoria[frame_menos_usada].pag_num = pag_num;
    memoria[frame_menos_usada].timestamp = instante;
    memoria[frame_menos_usada].ocupada = 1;

    return memoria[frame_menos_usada].id;  
}

/**
 * Substitui a página LRU, garantindo que uma página específica não é substiuída 
 * pelo algoritmo de substituição (usado em SWAP/MEMCPY).
 *
 * @param memoria Vetor de frames da memória.
 * @param proc_id ID do processo.
 * @param pag_num Número da página a carregar.
 * @param instante Instante de tempo atual (para LRU).
 * @param pag_excluida Número da página que não pode ser substituída.
 * @return Índice da frame substituída, ou -1 se não for possível substituir.
 */
int substituir_pagina_lru_excluindo(Frame memoria[], int proc_id, int pag_num, int instante, int pag_excluida) {
    int menor_timestamp = __INT_MAX__;
    int frame_menos_usada = -1;

    // Encontrar a frame com o menor timestamp
    // Em caso de empate, escolhe a primeira encontrada, que por sua vez será a de menor índice
    for (int i = 0; i < FRAMES_TOTAL; i++) {
        if (memoria[i].ocupada && memoria[i].timestamp < menor_timestamp && !(memoria[i].proc_id == proc_id && memoria[i].pag_num == pag_excluida)) {
            menor_timestamp = memoria[i].timestamp;
            frame_menos_usada = i;
        }
    }

    if (frame_menos_usada == -1) return -1; // Falha

    // Substituir a frame
    memoria[frame_menos_usada].proc_id = proc_id;
    memoria[frame_menos_usada].pag_num = pag_num;
    memoria[frame_menos_usada].timestamp = instante;
    memoria[frame_menos_usada].ocupada = 1;

    return memoria[frame_menos_usada].id;  
}

