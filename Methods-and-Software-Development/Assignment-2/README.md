# Eventastic - Relatório do Projeto

## Descrição

Eventastic é uma biblioteca Java implementada como backend modular para gerenciamento completo de eventos, inscrições de participantes e pagamentos. O projeto foi desenvolvido com foco em robustez, validação de dados, e separação clara de responsabilidades através de camadas (Layered Architecture). A biblioteca oferece uma API pública centralizada (`EventasticAPI`) que agrega os serviços de eventos, inscrições e pagamentos, facilitando a integração em aplicações externas.

**Linguagem:** Java 17  
**Build Tool:** Maven 3.11.0

---

## Métodos de Acesso Público Centralizado (API)

A classe `EventasticAPI` expõe os seguintes **17 métodos públicos** organizados em 3 categorias:

### **Métodos de Eventos (7)**

| Método | Descrição |
|--------|-----------|
| `criarEvento()` | Cria um novo evento com fases de inscrição e opções adicionais. |
| `editarEvento()` | Edita dados de um evento futuro (bloqueado após início). |
| `inativarEvento()` | Inativa um evento (gera reembolsos e notificações, quando aplicável). |
| `obterListaEventos()` | Retorna todos os eventos ativos, removendo automaticamente os expirados. |
| `consultarEventosDisponiveis()` | Retorna eventos ativos com inscrições abertas e vagas disponíveis. |
| `detalhesEvento()` | Exibe informações completas de um evento. |
| `procurarEvento()` | Procura um evento por ID com verificação de expiração. |

### **Métodos de Inscrições (6)**

| Método | Descrição |
|--------|-----------|
| `inscrever()` | Registra uma nova inscrição com validações (e-mail, lotação, opções obrigatórias). |
| `listarInscricoes()` | Retorna todas as inscrições registadas em memória. |
| `consultarInscricao()` | Consulta detalhes de uma inscrição (requer e-mail para segurança). |
| `obterListaParticipantes()` | Retorna participantes de um evento específico. |
| `procurarParticipante()` | Procura participantes por critérios (nome, email, IdInscrição). |
| `exportarParticipantesParaCSV()` | Exporta participantes de um evento para ficheiro CSV. |

### **Métodos de Pagamentos (4)**

| Método | Descrição |
|--------|-----------|
| `consultarPagamento()` | Consulta detalhes e exibe estado atual do pagamento de uma inscrição. |
| `registarPagamento()` | Regista/atualiza dados de pagamento (valor, data, notas). |

---

## Estrutura do Projeto

```
/home/andre/MDS/
├── pom.xml                              # Configuração Maven
├── README.md                            # Relatório
├── src/main/java/com/eventastic/
│   ├── api/
│   │   └── EventasticAPI.java           # API: Agregador de serviços públicos
│   │
│   ├── enums/
│   │   ├── TipoInscricao.java           # ESTUDANTE, NAO_ESTUDANTE
│   │   ├── TipoFase.java                # EARLY, LATE, DURING
│   │   ├── EstadoInscricao.java         # PENDENTE_PAGAMENTO, PAGA, CANCELADA
│   │   └── EstadoPagamento.java         # PENDENTE, CONFIRMADO
│   │
│   ├── model/
│   │   ├── Event.java                   # Entidade: Evento
│   │   ├── Inscricao.java               # Entidade: Inscrição de participante
│   │   ├── Pagamento.java               # Entidade: Pagamento associado à inscrição
│   │   ├── FaseInscricao.java           # Entidade: Fase de inscrição com preços
│   │   ├── OpcaoAdicional.java          # Entidade: Opção extra (obrigatória/opcional)
│   │   └── ConfiguracaoPreco.java       # Entidade: Preço por tipo de inscrição
│   │
│   ├── service/
│   │   ├── EventService.java            # SERVICE: Lógica de eventos
│   │   ├── InscricaoService.java        # SERVICE: Lógica de inscrições
│   │   └── PagamentoService.java        # SERVICE: Lógica de pagamentos
│   │
│   └── demo/
│       └── Main.java                    # DEMO: Script de teste completo
```

## Lógica do Sistema

### **Arquitetura Geral**

A arquitetura segue o padrão **Camadas (Layered Architecture)**:

```
┌────────────────────────┐
│   EventasticAPI        │  ← Ponto de entrada único
└──────────────┬─────────┘
               │
     ┌─────────┼─────────┐
     ▼         ▼         ▼
┌─────────────────────────────────────┐
│        Services (Lógica)            │
│ EventService                        │
│ InscricaoService                    │
│ PagamentoService                    │
└──────────────┬──────────────────────┘
               │
     ┌─────────┴──────────┐
     ▼                    ▼
┌──────────────┐    ┌──────────────┐
│   Models     │    │   Enums      │
│  (Entidades) │    │  (Tipos)     │
└──────────────┘    └──────────────┘
```

### **Fluxo de Uma Inscrição Completa**

1. **Criar Evento** → `EventService.criarEvento()`
   - Valida fases (1-3, sem fases duplicadas, sem sobre-posição)
   - Valida opções adicionais
   - Gera ID único sequencial
   - Armazena em lista interna

2. **Fazer Inscrição** → `InscricaoService.inscrever()`
   - Valida email (único por evento)
   - Valida opções obrigatórias
   - Valida lotação (não exceder maxParticipantes)
   - Valida tipo de inscrição (estudante → número obrigatório)
   - Calcula valor total (preço fase + opções)
   - Cria `Inscricao` com `Pagamento` associado

3. **Consultar/Registar Pagamento** → `PagamentoService`
   - `consultarPagamento()` mostra dados e estado atual
   - `registarPagamento()` atualiza valor, data e notas

4. **Listar/Procurar** → Múltiplos métodos
   - Filtragem por critérios (nome, email, etc.)
   - Exportar lista de participantes para CSV

5. **Inativar Evento** → `EventService.inativarEvento()`
   - Se futuro: inativa, mostra reembolsos, notifica inscritos do cancelamento, apaga inscrições e remove evento da lista
   - Se a decorrer: bloqueia
   - Se expirado: inativa, apaga inscrições e remove evento da lista


### **Ligações entre Serviços**

```
EventService ←→ InscricaoService
  │                   │
  ├─ Valida eventos   ├─ Cria inscrições
  ├─ Limpa inscrições ├─ Valida opções
  └─ Trata expiração  └─ Gera CSVs
  
InscricaoService ←→ PagamentoService
  │                   │
  └─ Fornece dados   ─┘─ Consulta/registra pagos
```

---

## Principais Decisões Tomadas

### **Linguagem Java**

**Decisão:** Utilizar Java como linguagem de programação para implementação da biblioteca Eventastic.

**Justificação:**
- Orientação a objetos permite modelar entidades do domínio de forma clara (Event, Inscricao, Pagamento)
- API moderna de datas/horas (`LocalDate`, `LocalDateTime`) ideal para gestão de eventos
- Streams para operações de filtragem e consulta
- Sobretudo preferência pessoal por parte do grupo

---

### **Armazenamento em Memória**

**Decisão:** Usar `ArrayList` para armazenar eventos e inscrições em memória.

---

### **Relação entre FaseInscricao e ConfiguracaoPreco**

**Decisão:** Cada `FaseInscricao` contém um `Map<TipoInscricao, ConfiguracaoPreco>` que associa cada tipo de inscrição ao seu preço específico nessa fase.

**Justificação:**
- Permite preços diferenciados por tipo (ESTUDANTE vs NAO_ESTUDANTE) em cada fase
- Estrutura flexível: fases EARLY podem ter descontos, LATE pode ter preços maiores
- Encapsulamento: cada fase conhece os seus próprios preços
- Facilita consulta: `fase.obterPreco(TipoInscricao.ESTUDANTE)` retorna valor diretamente
- Validação garantida: construtor de `FaseInscricao` obriga a ter preços para ambos os tipos

**Funcionamento:**
- Cada `FaseInscricao` guarda um Map que associa cada `TipoInscricao` a um `ConfiguracaoPreco`:
    Chave: `TipoInscricao` (ESTUDANTE ou NAO_ESTUDANTE)
    Valor: `ConfiguracaoPreco` (contém o tipo e o preço)
- Por exemplo, na fase EARLY pode haver:
    ESTUDANTE → ConfiguracaoPreco(ESTUDANTE, 10.0€)
    NAO_ESTUDANTE → ConfiguracaoPreco(NAO_ESTUDANTE, 20.0€)

---

### **Relação `OneToOne` entre Inscrição e Pagamento**

**Decisão:** Cada `Inscricao` cria automaticamente uma instância de `Pagamento` no seu construtor, estabelecendo uma relação 1:1 obrigatória.

**Justificação:**
- Pagamento é parte integrante da inscrição, não existe pagamento sem inscrição
- Inicialização garantida: o objeto `Pagamento` é criado no momento da inscrição com estado `PENDENTE`
- `inscricao.getPagamento()` retorna sempre um objeto válido (nunca `null`)
- ID do pagamento corresponde ao ID da inscrição, facilitando auditoria

**Funcionamento:**
- `PagamentoService` acede ao pagamento via `inscricao.getPagamento()` para consultar/atualizar dados do pagamento
- Não é possível criar `Pagamento` independente
- Garante integridade: toda inscrição tem exatamente um pagamento associado

---

### **Campos mutáveis em `Event.java` (exceto ID)**

**Decisão:** Todos os campos do `Event` (exceto `idEvento`) são mutáveis através de setters, permitindo edição do evento após criação.

**Justificação:**
- Realismo: eventos precisam ser editáveis (mudança de local, datas, lotação, etc.)
- `idEvento` é `final` pois identifica unicamente o evento (não deve mudar)

**Funcionamento:**
- Validação nos setters garante integridade (ex: `setNome()` valida string não vazia)
- `editarEvento()` em `EventService` valida regras de negócio (não editar após início do evento)
- Trade-off: perde imutabilidade total mas ganha flexibilidade necessária
- Setters usam `List.copyOf()` para prevenir modificações externas de coleções

---

### **Separação: `consultarPagamento()` e `registarPagamento()`**

**Decisão:** Inicialmente estava definido um único método, `gerirPagamento()`, que consultava o pagamento e pedia inputs ao utilizador para registar dados de pagamento. A decisão foi separar esse método em dois, `consultarPagamento()` e `registarPagamento()`, sem interação com o utilizador.

**Justificação:**
- Método anterior misturava consulta + input do utilizador
- Novo design permite uso programático
- `consultarPagamento()` só lê e imprime
- `registarPagamento()` recebe dados como parâmetros

---

### **Acesso Público Centralizado (EventasticAPI)**

**Decisão:** Centralizar 17 métodos públicos numa única classe.

**Justificação:**
- Aplicações externas veem apenas 1 classe (não 3 serviços)
- Encapsula inicialização de serviços
- Facilita documentação e uso

---

### **Tratamento de Eventos Expirados**

**Decisão:** Auto-remover eventos expirados ao listar ou procurar.

**Justificação:**
- Demonstra tratamento de ciclo de vida
- Eventos antigos não devem permanecer visíveis
- Otimização do espaço em memória

**Funcionamento:**
- `tratarEventoSeInativo()` é chamado em: `inativarEvento()`, `obterListaEventos()` e `findEventoById()`
- Eventos expirados teem as suas inscrições removidas com `removerInscricoesDoEvento()`
- Por fim, o evento é removido da lista interna

---

### **Reembolsos e Notificações (Inativação de evento futuro)**

**Decisão:** Após a inativação manual de um evento futuro (pelo administrador), mostrar reembolsos a efetuar na consola e simular a notificação dos inscritos do cancelamento.

**Funcionamento:**
- Inativação de um evento futuro
- Print dos reembolsos a efetuar (dados de pagamento dos inscritos)
- Simulação da notificação (em produção seria email/SMS)
- Remoção das inscrições e do evento


---

### **Validação do Número de Aluno**

**Decisão:** Por forma a simular a validação por parte do sistema do número de aluno em inscrições do tipo **ESTUDANTE**, decidimos que seriam números de aluno válidos todos os números entre **58000** e **58999**.

---

### **Observação: Estado da Inscrição - `CANCELADA`**

**Descrição:** Por questões de consistência entre o **backend** implementado e os **diagramas** de use cases e de classes desenhados na 1ª parte do trabalho, decidimos incluir o estado da inscrição `CANCELADA` em `EstadoInscricao.java`, porém este estado nunca é aplicado na implementação atual, pois como decidimos remover as inscrições em memória após inativar um evento, o estado nunca chega a ser aplicado.

## Como Executar

### **Compilar**
```bash
mvn clean compile
```

### **Executar Script**
```bash
mvn exec:java
```

### **Saída Esperada**
- Todos os métodos de acesso público são experimentados
- 19+ testes executados sequencialmente
- Cada teste mostra seu resultado (OK ou bloqueado conforme esperado)
- CSV gerado: `participantes_techconf.csv`

---

## Âmbito do Projeto

Este projeto foi desenvolvido como trabalho académico para a disciplina de Metodologias e Desenvolvimento de Software na Universidade de Évora.  

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05