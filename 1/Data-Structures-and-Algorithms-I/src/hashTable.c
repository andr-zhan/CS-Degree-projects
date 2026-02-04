#include "hashTable.h"
#include "fatal.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MinTableSize (10) // Tamanho mínimo para cada hash table inicializada

// Função que verifica se um número é primo
bool CheckPrime(int N) {
    if (N == 2 || N == 3) {
        return true;
    }
    if (N == 1 || N % 2 == 0) {
        return false;
    }
    for (int i = 3; i * i <= N; i += 2) { // Verifica todos os números ímpares a partir de 3 até a raiz quadrada de "N"
        if (N % i == 0) {
            return false;
        }
    }
    return true;
}

// Função que obtém o próximo número primo maior que N, e que verifica também se N é primo
int NextPrime(int N) {
    int i = N;
    while (1) {
        if (CheckPrime(i)) {
            return i;
        }
        i++;
    }
}

// Função que faz o hash para tabelas de acesso quadrático
Index HashQuadratica(const char *Key, int TableSize, int i) {
    unsigned int HashVal = 0;
    while (*Key != '\0') {
        HashVal = (HashVal << 5) + *Key++;
    }
    return ((HashVal % TableSize) + i * i) % TableSize;
}

// Função que faz o hash para tabelas de acesso linear
Index HashLinear(const char *Key, int TableSize, int i) {
    unsigned int HashVal = 0;
    while (*Key != '\0') {
        HashVal = (HashVal << 5) + *Key++;
    }
    return ((HashVal % TableSize) + i) % TableSize;
}

// Função para inicializar as hash tables
HashTable InitializeTable(int TableSize) {
    int DefinitiveTableSize = TableSize;
    HashTable H;
    int i;

    if (TableSize < MinTableSize) {
        Error("Tabela demasiado pequena");
        return NULL;
    }

    if (!CheckPrime(DefinitiveTableSize)) {
        DefinitiveTableSize = NextPrime(DefinitiveTableSize);
    }

    H = malloc(sizeof(struct HashTbl));
    if (H == NULL) {
        FatalError("Out of space!!!");
    }

    H->TableSize = DefinitiveTableSize;
    H->TheCells = malloc(sizeof(Cell) * H->TableSize);
    H->Ocupados = 0;
    H->Colisoes = 0;

    if (H->TheCells == NULL) {
        FatalError("Out of space!!!");
    }

    for (i = 0; i < H->TableSize; i++) {
        H->TheCells[i].String = Empty;
    }

    return H;
}

// Função para encontrar palavras na hash table com o dicionário, de acesso quadrático
int Find(char *palavra, HashTable H) {
    int a = -1;
    int indice;
    for (int i = 0; i < H->TableSize; i++) {
        indice = HashQuadratica(palavra, H->TableSize, i);
        if (H->TheCells[indice].String == Occupied) {
            if (strcmp(H->TheCells[indice].Element, palavra) == 0) {
                return indice;
            }
        }
    }
    return a;
}

// Função para inserir palavras nas hash tables
void Insert(const char *palavra, HashTable H, int a) { // Quando a == 0 vai fazer hash para tabelas de acesso quadrático. Quando a == 1 faz hash para acesso linear
 int indice;

    for (int i = 0; i < H->TableSize; i++) {
        if (a == 0) {
            indice = HashQuadratica(palavra, H->TableSize, i);
        }
        else if (a == 1) {indice = HashLinear(palavra, H->TableSize, i);}
        if (H->TheCells[indice].String == Empty) {
            strcpy(H->TheCells[indice].Element, palavra);
            H->TheCells[indice].String = Occupied;
            H->Ocupados += 1;
            break;
        } 
        else if (H->TheCells[indice].String == Occupied) {
            H->Colisoes += 1;
        }
    }
}

// Função para calcular o load factor de uma hash table
float LoadFactor(HashTable H) {
    return (float)H->Ocupados / H->TableSize;
}

// Função para fazer rehash às hash tables
HashTable Rehash(HashTable H, int a) { // "a" vai determinar se na função insert vai se realizar hash para acesso quadrático ou linear
    int i, OldSize;
    Cell *OldCells;

    OldCells = H->TheCells;
    OldSize = H->TableSize;

    H = InitializeTable(2 * OldSize);

    for (i = 0; i < OldSize; i++) {
        if (OldCells[i].String == Occupied) {
            Insert(OldCells[i].Element, H, a); // Realoca os elementos na nova hash table
        }
    }

    free(OldCells);

    return H;
}

// Função para retornar a palavra guardada no indice "P"
/*char *Retrieve(Position P, HashTable H) {
    return H->TheCells[P].Element;
}*/

// Função para destruir as hash tables e libertar memória
void DestroyTable(HashTable H) {
    if (H != NULL) {
        if (H->TheCells != NULL) {
            free(H->TheCells);
        }
        free(H);
    }
}

// Função para dar print à hash table
/*void PrintTable(HashTable H) {
    for (int i = 0; i < H->TableSize; i++) {
        if (H->TheCells[i].String == Occupied) {
            printf("%s\n", H->TheCells[i].Element);
        }
    }
}*/

// Função para mostrar algumas informações da hash table, para verificar se foi implementada com sucesso
void TableData(HashTable H) {
    printf("==================================================\n");
    printf("Tamanho final da Tabela: %d\n", H->TableSize);
    printf("Palavras inseridas: %d\n", H->Ocupados);
    printf("Número de colisões: %d\n", H->Colisoes);
    printf("Load Factor: %.2f\n", LoadFactor(H));
    printf("==================================================\n");
}

