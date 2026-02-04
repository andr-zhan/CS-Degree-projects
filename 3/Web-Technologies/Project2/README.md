# Room Rent - Aplicação Web + Server

## Descrição

**Room Rent** é uma aplicação web para gestão de anúncios de arrendamento de quartos. O sistema permite que utilizadores publiquem anúncios de oferta ou procura de quartos, contactem outros anunciantes, e conta com uma área administrativa para gestão de utilizadores e anúncios.

### Principais Funcionalidades

- **Autenticação e Autorização**: Sistema de login/registo com Spring Security
- **Gestão de Anúncios**: Criação e visualização de anúncios (OFERTA/PROCURA)
- **Sistema de Mensagens**: Utilizadores interessados podem contactar o anunciante
- **Pagamentos**: Geração de referências Multibanco para taxa de publicação
- **Área Administrativa**: Dashboard para gestão de utilizadores e anúncios
- **Paginação e Filtros**: Pesquisa avançada com filtros por tipo, zona e nome
- **Estados de Anúncio**: Ativo/Inativo (pode ainda ser apagado)

---

## Tecnologias Utilizadas

### Backend
- **Java 17**: Linguagem de programação
- **Spring Boot 3.3.3**: Framework principal
  - Spring Web MVC
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **Hibernate**: ORM para persistência de dados
- **PostgreSQL**: Base de dados relacional
- **REST API**: Comunicação com serviço externo de referências Multibanco

### Frontend
- **Thymeleaf**: Template engine para renderização server-side
- **HTML + CSS**: Estrutura e estilo
- **JavaScript**: Interatividade client-side
- **Thymeleaf Security Dialect**: Integração de segurança nos templates

### DevOps
- **Maven**: Gestão de dependências e build
- **Docker & Docker Compose**: Containerização da aplicação e BD

---

## Estrutura do Projeto

```
RoomRent/
├── src/
│   └── main/
│       ├── java/
│       │   └── roomrent/
│       │       ├── RoomRentApplication.java    # Classe principal
│       │       ├── controller/                 # Controllers MVC
│       │       ├── model/                     # Entidades JPA
│       │       ├── repository/                 # Repositórios Spring Data
│       │       ├── security/                   # Configuração Spring Security
│       │       └── service/                    # Lógica de negócio
│       └── resources/
│           ├── application.properties          # Configurações
│           ├── static/                         # CSS, JS, imagens
│           └── templates/                      # Templates Thymeleaf
├── docker-compose.yml                          # Orquestração Docker
├── Dockerfile                                  # Imagem Docker da app
└── pom.xml                                     # Configuração Maven
```

---

## Guia de Execução Passo a Passo

#### 1. Iniciar a base de dados
No diretório raiz do projeto:
```bash
docker-compose up -d db
```

#### 2. Compilar e executar a aplicação
No mesmo diretório ou em outro terminal:
```bash
mvn compile
mvn spring-boot:run
```

#### 3. Aceder à aplicação
Abra o navegador e aceda a: **http://localhost:8080**

#### 4. Parar os serviços
```bash
# Parar a aplicação: Ctrl+C no terminal onde está a correr

# Parar a base de dados:
docker-compose stop db
```

---

## Credenciais Iniciais

Ao iniciar a aplicação pela primeira vez, é criado automaticamente um utilizador administrador:

- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ROLE_ADMIN`

Este utilizador tem acesso à área administrativa em: **http://localhost:8080/admin**

---

## Configurações Importantes

### Porta do Servidor
Por padrão, a aplicação corre na porta **8080**. Para alterar:
```properties
# application.properties
server.port=9090
```

### Base de Dados
Configuração atual (funciona com Docker):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/roomrent
spring.datasource.username=roomrent
spring.datasource.password=roomrent
```

### Taxa de Publicação
```properties
roomrent.ad.publication.fee=5.99
```

### Paginação
```properties
roomrent.pagination.size=4
```

### Serviço de Referências Multibanco
```properties
roomrent.payment.mb.endpoint=https://magno.di.uevora.pt/tweb/t2/mbref4payment
```

---

## Funcionalidades por Perfil

### Utilizador Regular (ROLE_USER)
- ✅ Visualizar todos os anúncios ativos
- ✅ Criar novos anúncios (OFERTA/PROCURA)
- ✅ Enviar e receber mensagens sobre anúncios
- ✅ Área pessoal com dashboard de anúncios (Consulta de mensagens e dados de pagamentos)

### Administrador (ROLE_ADMIN)
- ✅ Aprovar/rejeitar novos utilizadores
- ✅ Gerir estados de todos os anúncios
- ✅ Remover qualquer anúncio
- ✅ Dashboard administrativo completo

---

## Endpoints Principais

| Rota | Método | Descrição | Autenticação |
|------|--------|-----------|--------------|
| `/` | GET | Página inicial com últimos anúncios | Pública |
| `/login` | GET/POST | Login de utilizadores | Pública |
| `/register` | GET/POST | Registo de novos utilizadores | Pública |
| `/ads` | GET | Lista de anúncios com filtros | Pública |
| `/ads/{id}` | GET | Detalhes de um anúncio | Pública (se anúncio ativo) |
| `/ads/new` | GET/POST | Criar novo anúncio | USER |
| `/ads/{id}/payment` | GET | Referência Multibanco | USER |
| `/my` | GET | Dashboard pessoal | USER |
| `/my/ads/{id}/messages` | GET | Mensagens de um anúncio | USER |
| `/my/ads/{id}/payment` | GET | Dados de pagamento de um anúncio | USER |
| `/messages/send/{id}` | POST | Enviar mensagem | USER |
| `/admin` | GET | Dashboard administrativo | ADMIN |
| `/admin/users/{id}/approve` | POST | Aprovar utilizador | ADMIN |
| `/admin/ads/{id}/activate` | POST | Ativar anúncio | ADMIN |
| `/admin/ads/{id}/deactivate` | POST | Inativar anúncio | ADMIN |
| `/admin/ads/{id}/delete` | POST | Apagar anúncio | ADMIN |

---

## Âmbito do projeto

Este projeto foi desenvolvido como trabalho académico para a disciplina de Tecnologias Web na Universidade de Évora.
