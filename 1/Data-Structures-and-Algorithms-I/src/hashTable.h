#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

enum KindOfEntry { Occupied, Empty }; // Estado de cada entrada das hash tables. Ou está ocupada ou vazia

// Estrutura de cada entrada das hash tables
struct HashEntry { 
    char Element[50];
    enum KindOfEntry String;
};
typedef struct HashEntry Cell;

// Estrutura de cada hash table
struct HashTbl {
    int Ocupados;
    int Colisoes;
    int TableSize;
    Cell *TheCells;
};
    
#ifndef _HashQuad_H
#define _HashQuad_H

typedef unsigned int Index;
typedef Index Position;

struct HashTbl;
typedef struct HashTbl *HashTable;

bool CheckPrime(int N);
int NextPrime(int N);
Index HashQuadratica(const char *Key, int TableSize, int i);
Index HashLinear(const char *Key, int TableSize, int i);
HashTable InitializeTable( int TableSize );
int Find(char *palavra, HashTable H );
void Insert( const char *palavra, HashTable H, int a );
float LoadFactor(HashTable H);
HashTable Rehash( HashTable H, int a );
char *Retrieve( Position P, HashTable H );
void DestroyTable( HashTable H );
void PrintTable(HashTable H);
void TableData(HashTable H);


#endif