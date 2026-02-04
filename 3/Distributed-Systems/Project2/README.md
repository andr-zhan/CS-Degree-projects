# Sistema de Monitoramento Ambiental
Sistema distribuído para monitoramento ambiental com múltiplos clientes e broker MQTT, utilizando Java, Spring Boot, gRPC e PostgreSQL.
## Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8+**
- **Docker** e **Docker Compose**

## Arquitetura do Projeto

O projeto é composto pelos seguintes componentes:

- **server**: Servidor Spring Boot central (porta 8080)
- **client-grpc**: Cliente gRPC (comunicação com servidor)
- **client-mqtt**: Cliente MQTT (subscrição a tópicos MQTT)
- **client-rest**: Cliente REST (chamadas HTTP ao servidor)
- **admin-cli**: Interface de linha de comando para administração
- **mosquitto**: Broker MQTT (incluído no Docker Compose)
- **postgres**: Base de dados PostgreSQL (incluída no Docker Compose)

  
## Início
### 1. Preparar o Ambiente
Certifique-se de que tem o Java 17+ e Maven instalados:
```bash

java -version

mvn -version

```

  

### 2. Iniciar os Serviços de Infraestrutura

Inicie o Docker Compose para provisionar o broker MQTT e a base de dados PostgreSQL:

```bash

docker-compose up -d

```

Isto inicia:

- **Mosquitto** (MQTT Broker) na porta 1883
- **PostgreSQL** na porta 5432

Verifique o estado dos serviços:
```bash

docker-compose ps

```

  

### 3. Executar o Servidor

Execute o servidor Spring Boot:

```bash

cd server

mvn clean compile

mvn spring-boot:run

```

O servidor estará disponível em:

- **HTTP REST**: http://localhost:8080
- **gRPC**: localhost:9090

### 4. Executar os Clientes (em terminais separados)
#### Cliente gRPC

```bash

cd client-grpc

mvn clean compile exec:java

```

#### Cliente MQTT

```bash

cd client-mqtt

mvn clean compile exec:java

```

#### Cliente REST

```bash

cd client-rest

mvn clean compile exec:java

```

#### Admin CLI

```bash

cd admin-cli

mvn clean compile exec:java

```

## Configurar Clientes

###  Ficheiro "protocolClientCoonfig", protocol = Mqtt | Grpc | Rest

**Intervalo em segundos para envio de métricas**
public final int sendIntervalSeconds = 5;

**Intervalo em segundos para sincronização com registro de dispositivos**
public final int pollIntervalSeconds = 10;

**URL do servidor de registro de dispositivos**
public final String registryUrl = "http://localhost:8080";

**Número de threads para processamento concorrente**
public final int numberOfThreads = 5;

**Tempo de execução do cliente em segundos**
public final int runDurationSeconds = 60;

**Ativar/desativar execução por tempo limitado (true = ativa timer, false = execução contínua)**
public final boolean enableTimedExecution = false;

  
## 👥 Contribuidores

[André Gonçalves - 58392]
[André Zhu Zhan - 58762]