JFLAGS = -cp .:lib/postgresql-42.7.3.jar
JC = javac
JVM = java

SOURCES = $(shell find . -name "*.java")
CLASSES = $(SOURCES:.java=.class)

all: compile

compile:
	@echo "🔨 A compilar projeto..."
	$(JC) $(JFLAGS) $(SOURCES)
	@echo "✅ Compilação concluída!"

run-server:
	@echo "🚀 A iniciar o servidor (RMI + ADMIN SOCKET)..."
	$(JVM) -cp .:lib/postgresql-42.7.3.jar server.Servidor

run-cliente-geral:
	@echo "🧑‍💻 A iniciar Cliente Geral..."
	$(JVM) -cp .:lib/postgresql-42.7.3.jar client.ClienteGeral

run-cliente-admin:
	@echo "🧑‍💼 A iniciar Cliente Admin (Sockets)..."
	$(JVM) -cp .:lib/postgresql-42.7.3.jar client.ClienteAdmin

clean:
	@echo "🧹 A apagar .class..."
	find . -name "*.class" -delete
	@echo "✅ Limpeza concluída!"
