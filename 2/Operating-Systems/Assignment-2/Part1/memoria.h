#ifndef MEMORIA_H
#define MEMORIA_H


#include "queue.h" 


#define MEMORIA_TOTAL 21000 // 21 kB = 21000 Bytes
#define TAM_FRAME 3000  // 3kB = 3000 Bytes
#define FRAMES_TOTAL (MEMORIA_TOTAL / TAM_FRAME) // Nº máx de frames, serão sempre 7.


typedef struct {
    int id;            // ID da frame (ex: 0 a 6)
    int ocupada;       // Indica se a frame está livre (0) ou ocupada (1).
    int proc_id;       // Guarda o ID do processo que ocupa atualmente esta frame.
    int pag_num;       // Indica o número da página dentro do espaço do processo carregada.
    int timestamp;     // Usado para o algoritmo LRU, permitindo identificar a página que há mais tempo não é acedida.
} Frame;


// Funções para manipulação de frames
void inicializar_memoria(Frame memoria[]);
int carregar_pagina(Frame memoria[], int proc_id, int pag_num, int instante); 
void libertar_frames_processo(Frame memoria[], int proc_id); 
int pagina_esta_na_memoria(Frame memoria[], int proc_id, int pag_num, int instante, int usar_lru);

// Substituição por FIFO
int substituir_pagina_fifo(Frame memoria[], int proc_id, int pag_num, Queue *fifo_queue);

// Substituição por LRU
int substituir_pagina_lru(Frame memoria[], int proc_id, int pag_num, int instante);


#endif
