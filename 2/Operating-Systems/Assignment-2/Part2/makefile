# Nome do executável
EXEC = simulador

# Ficheiros fonte
SRC = simulador.c queue.c inputs_part2.c memoria.c

# Compilador e flags
CC = gcc
CFLAGS = -Wall -std=c99

# Regra principal (build por defeito)
all: $(EXEC)

# Compilação do programa
$(EXEC): $(SRC)
	$(CC) $(SRC) -o $(EXEC) $(CFLAGS)

# Executar o programa
run: $(EXEC)
	./$(EXEC)

# Limpar ficheiros gerados
clean:
	rm -f $(EXEC) output.out

# Para garantir que 'clean' e 'run' não entram em conflito com ficheiros
.PHONY: all clean run
