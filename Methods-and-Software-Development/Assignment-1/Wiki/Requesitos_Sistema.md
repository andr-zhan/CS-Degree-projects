Esta secção descreve o comportamento do sistema Eventastic do ponto de vista técnico, incluindo regras de negócio, restrições e requisitos não funcionais.



[**Requisitos Funcionais**](#rsFuncionais)

[**Requisitos Não Funcionais**](#rsNFuncionais)

<a name="rsFuncionais"></a> 
## Requisitos Funcionais
### RF01 - Gestão de utilizadores e autenticação
- O sistema deve permitir autenticação de utilizadores com perfis distintos:
  - Administrador;
  - Operador de Check-In.
- O sistema deve garantir que apenas utilizadores autenticados com perfil adqueado podem:
  - criar/editar eventos (administradores),
  - registar pagamentos (administradores e em casos excecionais operadores),
  - efetuar check-in de participantes (operadores).

**Os participantes não tem necessidade de realizar autenticação funcionando como um "Guest user" uma vez que qualquer das suas funcionalidades não tem necessidade da existencia de um perfil único.**

### RF02 - Criação e edição de eventos
- O sistema deve permitir criar, alterar e inativar eventos.
- Cada evento deve armazenar:
  - nome,
  - descrição,
  - local,
  - data_inicio, data_fim, 
  - horário
  - data_inicio_inscriçoes, data_fim_inscriçoes,
  - Ao criar um novo evento, o sistema deve gerar automaticamente um **ID do evento** único.
- Ao inativar um evento, o sistema deve automaticamente cancelar as inscrições associadas e informar o administrador dos eventuais reembolsos a efetuar.
- Após inativar um evento, o sistema deve ainda notificar os inscritos de que o evento foi cancelado e que serão reembolsados, via e-mail por exemplo.

### RF03 - Definição de fases de inscrição
- O sistema deve permitir associar a cada evento uma ou mais fases de inscrição, cada uma com:
  - nome ("early", "late" ou "durante_o_evento"),
  - data_inicio_fase,
  - data_fim_fase.
- O sistema deve validar que não existem sobreposições de datas entre fases do mesmo evento.
- O sistema deve determinar automaticamente a fase em vigor com base na data atual e nas datas definidas.

### RF04 - Definição de tipos de inscrição e preços
- O sistema deve suportar os tipos de inscrição por evento:
  - estudante,
  - não estudante.
- Para cada combinação (tipo de inscrição, fase de inscrição) deve existir um preço definido.
- O sistema deve usar a data de criação da inscrição para determinar:
  - a fase de inscrição aplicável,
  - o preço base correspondente ao tipo de inscrição e fase.

### RF05 - Definição de opções adicionais
- O sistema deve permitir ao administrador criar, por evento, opções adicionais com:
  - nome,
  - descrição opcional,
  - preço,
  - indicador de obrigatoriedade (verdadeiro/falso)
- Se uma opção for obrigatória, o sistema deve forçar a inclusão dessa opção no cálculo do valor total da inscrição.

### RF06 - Criação de inscrições
- O sistema deve permitir registar inscrições de participantes num evento ativo com inscrições abertas (o evento deve ter vagas disponíveis).
- Cada inscrição deve incluir:
  - referência ao evento (**ID do evento**),
  - dados pessoais do participante (pelo menos: nome e e-mail),
  - tipo de inscrição (estudante ou não estudante),
  - número de aluno (obrigatório se o tipo de inscrição for estudante),
  - data e hora de inscrição,
  - fase de inscrição determinada automaticamente,
  - valor total calculado (preço base + opções),
  - um **identificador único da inscrição (ID da inscrição)**, gerado pelo sistema.
- Se o tipo de inscrição for estudante, o sistema deve proceder à validação do número de aluno (verifica se existe, e se os dados pessoais coincidem).
- O sistema deve colocar a inscrição inicialmente no estado **pendente de pagamento**.
- O sistema deve apresentar o **ID da inscrição** ao participante após a criação.

### RF07 - Cálculo automático do valor total
- Ao criar uma inscrição, o sistema deve:
  - determinar a fase de inscrição em vigor,
  - obter o preço base para o tipo de inscrição nessa fase,
  - somar o preço das opções adicionais selecionadas (e das obrigatórias),
  - registar o valor total a pagar.
- O cálculo deve ser reprodutível: alterações futuras a preços não devem alterar retroativamente o valor das inscrições já criadas.

### RF08 - Gestão de estados de inscrição
- Cada inscrição deve ter um estado entre, por exemplo:
  - pagamento_pendente,
  - paga,
  - cancelada,
- O sistema só deve permitir o check-in de inscrições com estado paga.
- O sistema deve permitir cancelar inscrições (ex: inativação de um evento, por decisão de um administrador).

### RF09 - Registo de pagamentos recebidos por transferência bancária
- O sistema não efetua o pagamento em si, apenas regista informação de transferências bancárias recebidas por ação manual do administrador.
- Para cada pagamento deve ser registado:
  - inscrição associada,
  - valor transferido,
  - data da transferência,
  - notas internas (texto livre)
- O administrador consequentemente deve poder alterar o estado da inscrição para **paga**.
- Posteriormente o sistema deve permitir que o administrador consulte e atualize os dados de pagamento associados a uma inscrição.

### RF10 - Instruções de pagamento ao participante
- Após criação de uma inscrição, o sistema deve gerar e apresentar ao participante:
  - IBAN e outros dados de transferencia,
  - Valor exato a transferir
  - descrição obrigatória a usar na transferência (ex: "EVENTO123 - INSCRIÇÃO 456")
- O sistema deve também enviar essas informações por e-mail.

### RF11 – Listagem e exportação de participantes
- O sistema deve fornecer, para cada evento:
  - lista de inscrições com filtros por estado (todas, pagas, pendentes, etc.),
  - possibilidade de exportar esta lista para formato adequado para eventual posterior impressão.

### RF12 – Check-in no evento
- O sistema deve permitir ao operador de check-in realizar o check-in através do seu **ID de inscrição** registando:
  - data,
  - hora.
- Se o pagamento não estiver confirmado, o sistema deve impedir o check-in, exceto em situações excecionais marcadas manualmente.

### RF13 – Pagamento excecional no local
- O sistema deve permitir, quando aplicável, registar pagamentos excecionais realizados no local:
  - registar dados do comprovativo de transferência efetuada no momento,
  - marcar a inscrição como paga,
  - permitir o check-in subsequente.

### RF14 – Identificação e consulta de inscrição por ID

- O sistema deve permitir identificar cada inscrição através de um **ID da inscrição**.
- O sistema deve permitir que:
  - administradores e operadores de check-in localizem inscrições usando o ID da inscrição;
  - participantes consultem o estado da sua inscrição usando o ID da inscrição e outro dado de validação, como o e-mail).



<a name="rsNFuncionais"></a> 
## Requisitos Não Funcionais
### RNF01 - Plantaforma e arquitetura
- O sistema será desenvolvido **como backend com API, permitindo futuras integrações com frontends web ou mobile.

### RNF02 - Segurança e controlo de acessos
- O sistema deve proteger operações sensíveis (gestão de eventos, pagamentos, check-in) através de autenticação e autorização.
- Dados pessoais dos participantes (nome, e-mail, número de aluno) devem ser protegidos e não acessíveis a utilizadores não autorizados.

### RNF03 - Registo de auditoria
- O sistema deve registar com data e hora:
  - quem criou/alterou eventos,
  - quem registou ou alterou pagamentos,
  - quem efetuou check-in dos participantes,

**Esta informação é importante para rastreabilidade**

### RNF04 - Escalabilidade básica
- O sistema deve suportar a gestão de múltiplos eventos em simultaneo sem interferencia entre eles
- O sistema deve estar preparado para um número moderado de inscrições por evento.

### RNF05 - Usabilidade
- As interfaces devem ser simples e focadas nas tarefas principais, minimizando os erros de operação.

