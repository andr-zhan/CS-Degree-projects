# Nome do executável
EXEC = main

# Ficheiros fonte necessários para a Parte 1
SRC = main.c memoria.c queue.c

# Compilador e flags
CC = gcc
CFLAGS = -Wall -std=c99 -g

# Regra principal (build por defeito)
all: $(EXEC)

# Compilação do programa
$(EXEC): $(SRC)
	$(CC) $(CFLAGS) -o $(EXEC) $(SRC)

# Executar o programa com FIFO (exemplo)
run_fifo: $(EXEC)
	./$(EXEC) fifo

# Executar o programa com LRU (exemplo)
run_lru: $(EXEC)
	./$(EXEC) lru

# Limpar ficheiros gerados
clean:
	rm -f $(EXEC) fifo*.out lru*.out

# Para garantir que 'clean' e 'run' não entram em conflito com ficheiros
.PHONY: all clean