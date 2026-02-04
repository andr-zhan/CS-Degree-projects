#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "hashTable.h"
#include "fatal.h"

#define DICTIONARY_FILE "/home/andre/p1/trabalho eda/temp/portuguese.txt" // Ficheiro com o dicionário
#define TEST_FILE "/home/andre/p1/trabalho eda/temp/teste.txt" // Ficheiro de texto
#define OUTPUT_FILE "/home/andre/p1/trabalho eda/temp/output.txt" // Ficheiro de output com erros e sugestões respetivas
#define INITIAL_TABLE_SIZE 21 // Tamanho inicial de cada tabela inicializada
#define MAX_WORD_LEN 256 // Comprimento máximo das palavras

//Função para ler o dicionario e inserir na tabela de hash
HashTable lerDicionario(HashTable H) {
    printf("Inserindo as palavras do dicionário na tabela...\n");
    char word[MAX_WORD_LEN];
    FILE *file = fopen(DICTIONARY_FILE, "r");
    if (file == NULL) {
        FatalError("Could not open dictionary file");
        exit(1);
    }

    while (fgets(word, sizeof(word), file)) { // Lê cada linha do arquivo apontado por "file" e a armazena no array "word"
        word[strcspn(word, "\n")] = '\0'; // Substitui o caractere de nova linha "\n" pelo caractere nulo "\0", terminando a string corretamente.
        Insert(word, H, 0);
        if (LoadFactor(H) > 0.5) { // Rehash se loadfactor > 0.5
            H = Rehash(H, 0);
        }
    }

    fclose(file);
    printf("Sucesso!\n");
    return H;
}

// Função para gerar sugestões para os erros
char *spellCheck(const char *word, HashTable H, HashTable Erradas) {
int len = strlen(word);
char modified[MAX_WORD_LEN];
char *result = NULL;

    // Regra 1: adicionar um carácter
    for (int i = 0; i <= len; ++i) {
    for (char c = 'a'; c <= 'z'; ++c) {

    int j = 0;
    for (; j < i; ++j) {
        modified[j] = word[j];
    }
    modified[j++] = c;
    for (; j <= len; ++j) {
        modified[j] = word[j - 1];
    }
    modified[len + 1] = '\0';

    if (Find(modified, H) != -1) {
        result = strdup(modified); // Copiar a palavra modificada para outra variável
        return result;
    }
    }
    }

    // Regra 2: remover um carácter
    for (int i = 0; i < len; ++i) {
    int j = 0;

    for (; j < i; ++j) {
    modified[j] = word[j];
    }
    for (; j < len; ++j) {
    modified[j] = word[j + 1];
    }
    modified[len-1] = '\0';

    if (Find(modified, H) != -1) {
    result = strdup(modified); // Copiar a palavra modificada para outra variável
    return result;             
    }
    }

    // Regra 3: Trocar carácteres adjacentes
    for (int i = 0; i < len - 1; ++i) {

    int j = 0;
    for (; j < i; ++j) {
    modified[j] = word[j];
    }
    modified[j] = word[j + 1];
    modified[j + 1] = word[j];
    for (j = j + 2; j <= len; ++j) {
    modified[j] = word[j];
    }
    modified[len] = '\0';

    if (Find(modified, H) != -1) {
    result = strdup(modified); // Copiar a palavra modificada para outra variável
    return result;
    }

    }
    // Regra 4 (ADICIONAL): Trocar letras - Exemplo: conpras -> compras
    for (int i = 0; i < len; ++i) {
    for (char c = 'a'; c <= 'z'; ++c) {
    if (word[i]!= c) { // Evitar trocar pela mesma letra
        for (int j = 0; j < len; ++j) {
        modified[j] = word[j];
        }
        modified[i] = c;
        modified[len] = '\0';

        if (Find(modified, H)!= -1) {
            result = strdup(modified); // Copiar a palavra modificada para outra variável
            return result;
        }
    }
    }
    }

    return NULL; // Não foram encontradas sugestões
}


// Função para encontrar palavras erradas no ficheiro de texto e inseri-las em uma hash table linear.
void palavrasErradas(const char *filename, HashTable H, HashTable Erradas) {
    int indice;
    char line[MAX_WORD_LEN];
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        FatalError("Could not open file");
        exit(1);
    }

    printf("Incorrect words found:\n");

    while (fgets(line, sizeof(line), file)) { // Lê cada palavra do arquivo apontado por "file" e a armazena no array "word"
        char *word = strtok(line, " \n"); // Divide as strings em "tokens", que serão as palavras que estão divididas por espaços
        while (word != NULL) {
            indice = Find(word, H); // Procura a palavra no dicionário
            if (indice == -1) { // Não encontrou a palavra no dicionário se indice == -1
                printf("-%s\n", word); // Print da palavra errada
                Insert(word, Erradas, 1);
                if (LoadFactor(Erradas) > 0.5) { // Rehash se load factor exceder 0.5
                    H = Rehash(Erradas, 1);
                }   
            }
            word = strtok(NULL, " \n"); // Continua a dividir string original em tokens a partir do ponto onde a última chamada a strtok parou
        }
    }

    fclose(file);
}

//Função para gerar o output, cria um ficheiro de texto em que vai apresentar os erros e as respetivas sugestões
void geraOutput(const char *filename, HashTable H, HashTable Erros) {
    char *correctword;
    FILE *file = fopen(OUTPUT_FILE, "w");
    if (file == NULL) {
        FatalError("Could not open file");
        exit(1);
    }

    for (int i = 0; i < Erros->TableSize; i++) {
        if (Erros->TheCells[i].String == Occupied) {
            correctword = spellCheck(Erros->TheCells[i].Element, H, Erros); // Chamada da função spellcheck para corrigir os erros
            if (correctword != NULL) {
                fprintf(file, "%s -> %s\n", Erros->TheCells[i].Element, correctword);
            } else {
                fprintf(file, "%s -> Sem sugestões\n", Erros->TheCells[i].Element);
            }
        }
    }

    fclose(file);
}


int main(int argc, char *argv[]) {

    printf("Inicializando tabela...\n");
    printf("Tamanho inicial da Tabela: %d\n", INITIAL_TABLE_SIZE);
    HashTable TabelaDicionario = InitializeTable(INITIAL_TABLE_SIZE);  // Inicializa a hash table quadrática que vai incluir todas as palavras do dicionario
    HashTable TabelaErros = InitializeTable(INITIAL_TABLE_SIZE);  // Inicializa a hash table linear que vai incluir as palavras erradas provenientes do ficheiro de texto

    if (TabelaDicionario == NULL || TabelaErros == NULL) {  // Verifica se as tabelas foram inicializadas com sucesso
        FatalError("Falha ao inicializar as tabelas...\n");
    } else {printf("Sucesso!\n");}

    //Inserir o ficheiro portuguese.txt com as palavras do dicionário dentro da hashtable quadrática
    TabelaDicionario = lerDicionario(TabelaDicionario);

    //Mostra as informações da tabela criada já com as palavras do dicionário
    TableData(TabelaDicionario);

    // Procura por palavras erradas com erros e sugere palavras corretas no output
    palavrasErradas(TEST_FILE, TabelaDicionario, TabelaErros);
    geraOutput(OUTPUT_FILE, TabelaDicionario, TabelaErros);

    // Destrói as tabelas para libertar memória
    DestroyTable(TabelaDicionario);
    DestroyTable(TabelaErros);

    return 0;    
}
