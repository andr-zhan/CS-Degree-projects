<a id="uc1"></a>
## UC1 - Autenticar-se no sistema

**Atores principais:** Administrador, Operador de Check-in.  
**Objetivo:** Permitir que utilizadores com perfil de administrador ou operador de check-in iniciem sessão e acedam às funcionalidades respetivas.

### Pré-Condições
 - O utilizador (administrador ou operador) já está registado no sistema e possui credencias válidas.

### Pós-Condições
 - O utilizador fica autenticado e com sessão iniciada.  
 - O sistema identifica o perfil do utilizador (Administrador ou Operador de Check-In) e apresenta a área correspondente.

### Fluxo Principal
1. O utilizador acede à página de autenticação.  
2. Introduz as credenciais (por exemplo e-mail e palavra-passe).  
3. O sistema valida as credenciais.  
4. O sistema identifica o perfil do utilizador autenticado:
   - se for **Administrador**, apresenta a área de administração (gestão de eventos, inscrições, pagamentos, etc.).
   - se for **Operador de Check-In**, apresenta a área de check-in (pesquisa de participantes, registo de entrada, etc.).

### Extensões/Exceções
 - 3.a - Credenciais inválidas:
   - 3.a.1 - O sistema informa que as credenciais são inválidas, e pede para reintroduzir as credenciais.
   - 3.a.2 - Retorna ao passo 2.

<a id="uc2"></a>
## UC2 - Lista de Eventos

**Atores principais:** Administrador  
**Objetivo:** Aceder à lista administrativa de eventos.

### Pré-Condições
 - O administrador está autenticado.

### Pós-Condições
 - Nenhuma (use case apenas de leitura).

### Fluxo Principal
1. O administrador acede à área de gestão de eventos.  
2. O sistema apresenta a lista de eventos ativos e expirados, e um botão para criar um novo evento.

### Extensões/Exceções
 - 2.a - Não há eventos para o sistema apresentar:
   - 2.a.1 - O sistema informa que não há eventos.

<a id="uc3"></a>
## UC3 - Gerir Evento

**Atores principais:** Administrador  
**Objetivo:** Criar, alterar ou inativar eventos.

### Pré-Condições
 - O administrador está autenticado.

### Pós-Condições
 - Existe um novo evento registado com um **ID de evento** único, ou  
 - Um evento existente foi atualizado ou inativado.

### Fluxo Principal (Criar novo evento)
1. O administrador acede à lista de eventos ([UC2](#uc2)).  
2. O administrador seleciona a opção "Criar novo evento".  
3. O sistema apresenta um formulário de criação de evento.  
4. O administrador preenche os dados do evento:
   - nome,
   - descrição,
   - local,
   - datas de início e fim,
   - horário,
   - número máximo de participantes,
   - data de início e fim de inscrições.  
5. O administrador submete o formulário.  
6. O sistema valida os dados.  
7. O sistema **cria o evento**, atribui-lhe um **ID de evento** único e regista-o na base de dados.  
8. O sistema apresenta a página de detalhes do evento, incluindo o **ID de evento**.

### Extensões/Exceções
 - 6.a - Dados inválidos:
   - 6.a.1 - O sistema informa que há dados inválidos e pede para reintroduzi-los.
   - 6.a.2 - O administrador reintroduz os dados inválidos e retorna ao passo 5.

### Fluxo Alternativo — Edição de Evento
A.1. O administrador acede à lista de eventos ([UC2](#uc2)).  
A.2. O administrador seleciona um evento ativo.  
A.3. O sistema apresenta os dados atuais do evento.  
A.4. O administrador edita os dados pretendidos (ex. nome ou local), e guarda as alterações.  
A.5. O sistema valida e atualiza o evento, mantendo o mesmo **ID de evento**.

### Fluxo Alternativo — Inativação de Evento
B.1. O administrador acede à lista de eventos ([UC2](#uc2)).  
B.2. O administrador seleciona um evento ativo.  
B.3. O administrador clica no botão "Inativar Evento".  
B.4. O sistema pede confirmação.  
B.5. O administrador confirma.  
B.6. O sistema valida e atualiza o evento, mantendo o mesmo **ID de evento**.  
B.7. O sistema informa ao administrador os eventuais reembolsos a efetuar, e notifica os inscritos de que o evento foi cancelado.

<a id="uc4"></a>
## UC4 - Configurar Fases de Inscrição

**Atores principais:** Administrador  
**Objetivo:** Definir e gerir as fases de inscrição de um evento.

### Pré-Condições
 - O administrador está autenticado.  
 - Existe um evento ativo.

### Pós-Condições
 - As fases de inscrição do evento estão registadas e sem sobreposição de datas.

### Fluxo Principal
1. O administrador acede à lista de eventos ([UC2](#uc2)).  
2. O administrador seleciona um evento ativo.  
3. O administrador acede à gestão de fases de inscrição do evento selecionado.  
4. O sistema apresenta a lista de fases existentes (se houver).  
5. O administrador adiciona uma nova fase, indicando:
   - nome
   - data de início,
   - data de fim.  
6. O administrador guarda a fase.  
7. O sistema valida as datas, assegurando que não há sobreposição com outras fases do mesmo evento.  
8. O sistema regista a nova fase associada ao respetivo **ID do evento**.

### Extensões/Exceções
 - 7.a - Datas inválidas ou sobrepostas:
   - 7.a.1 - O sistema informa o erro.
   - 7.a.2 - O administrador corrige as datas e retorna ao passo 6.

<a id="uc5"></a>
## UC5 - Configurar Tipos de Inscrição e Preços

**Atores principais:** Administrador  
**Objetivo:** Definir os preços para cada tipo de inscrição e fase de um evento.

### Pré-Condições
 - O administrador está autenticado.  
 - O evento e as respetivas fases de inscrição estão definidos.

### Pós-Condições
 - Para cada tipo de inscrição (estudante/não estudante) e fase (early/late/durante o evento), existe um preço definido para o evento em questão.

### Fluxo Principal
1. O administrador acede à lista de eventos ([UC2](#uc2)).  
2. O administrador seleciona um evento ativo.  
3. O administrador acede à configuração de preços do evento selecionado.  
4. O sistema apresenta a lista de fases e tipos de inscrição.  
5. O administrador introduz ou atualiza os preços:
   - preço para estudante em cada fase;
   - preço para não estudante em cada fase.  
6. O administrador guarda as configurações.  
7. O sistema valida os valores (não negativos, por exemplo) e regista os preços associados ao **ID do evento**.

### Extensões/Exceções
 - 7.a - Valores inválidos:
   - 7.a.1 - O sistema informa o erro.
   - 7.a.2 - O administrador corrige os valores e retorna ao passo 6.

<a id="uc6"></a>
## UC6 - Configurar Opções Adicionais

**Atores principais:** Administrador  
**Objetivo:** Definir opções adicionais (almoço, jantar, etc.) de um evento.

### Pré-Condições
 - O administrador está autenticado.  
 - O evento está criado (com ID de evento).

### Pós-Condições
 - As opções adicionais do evento estão registadas no sistema e associadas ao respetivo **ID de evento**.

### Fluxo Principal
1. O administrador acede à lista de eventos ([UC2](#uc2)).  
2. O administrador seleciona um evento ativo.  
3. O administrador acede à gestão de opções adicionais do evento selecionado.  
4. O sistema apresenta as opções já existentes, se houver.  
5. O administrador cria uma nova opção indicando:
   - nome,
   - descrição opcional,
   - preço,
   - indicador de obrigatoriedade.  
6. O administrador guarda a nova opção.  
7. O sistema valida os dados e regista a opção adicional associada ao **ID de evento**.

### Extensões/Exceções
 - 7.a - Dados inválidos:
   - 7.a.1 - O sistema informa o erro.
   - 7.a.2 - O administrador corrige os dados inválidos e retorna ao passo 6.

<a id="uc7"></a>
## UC7 - Consultar Lista de Participantes

**Atores principais:** Administrador  
**Objetivo:** Visualizar a lista de participantes inscritos num evento.

### Pré-Condições
 - O administrador está autenticado.

### Pós-Condições
 - Nenhuma (Use case apenas de leitura).

### Fluxo Principal
1. O administrador acede à lista de eventos ([UC2](#uc2)).  
2. O administrador seleciona um evento ativo.  
3. O administrador acede à opção "Lista de participantes".  
4. O sistema apresenta uma lista com, para cada inscrição:
   - **ID de inscrição**,
   - nome do participante,
   - e-mail,
   - tipo de inscrição,
   - indicação se é estudante e, se aplicável, o **número de aluno**,
   - fase de inscrição aplicada,
   - opções adicionais selecionadas,
   - valor total,
   - estado do pagamento,
   - resultado da validação de estatuto de estudante (se aplicável).

### Extensões/Exceções
 - 4.a - Ainda não há inscrições no evento:
   - 4.a.1 - O sistema informa que ainda não há inscrições.

<a id="uc8"></a>
## UC8 - Consultar Estado do Pagamento

**Atores principais:** Administrador  
**Objetivo:** Consultar o estado e dados de pagamento associados a uma inscrição.

### Pré-Condições
 - O administrador está autenticado.

### Pós-Condições
 - Nenhuma (Use case apenas de leitura).

### Fluxo Principal
1. O administrador acede à área de gestão dos pagamentos.  
2. O administrador procura a inscrição, podendo usar o **ID de inscrição**, ou nome e e-mail do participante.  
3. O administrador seleciona a inscrição desejada.  
4. O sistema apresenta o estado e dados de pagamento associados à inscrição.

### Extensões/Exceções
 - 4.a - Ainda não há nenhum registo de pagamento associado à inscrição:
   - 4.a.1 - O sistema informa que ainda não há nenhum registo de pagamento associado à inscrição.
   - 4.a.2 - O fluxo termina.

<a id="uc9"></a>
## UC9 - Registar Pagamento

**Atores principais:** Administrador  
**Objetivo:** Registar manualmente a informação de um pagamento por transferência bancária recebida.

### Pré-Condições
 - O administrador está autenticado.  
 - Existe uma inscrição em estado pendente de pagamento.

### Pós-Condições
 - Um registo de pagamento estará associado à inscrição.

### Fluxo Principal
1. O administrador acede aos registos de pagamento de uma inscrição ([UC8](#uc8)).  
2. O administrador escolhe a opção "Registar pagamento".  
3. O sistema apresenta um formulário de registo de pagamento.  
4. O administrador introduz:
   - valor transferido,
   - data da transferência,
   - notas internas (por exemplo, referência da transferência),
   - estado do pagamento (pendente/pago).  
5. O administrador guarda os dados.  
6. O sistema valida os dados e regista a informação de pagamento associada à inscrição.

### Extensões/Exceções
 - 2.a - Já existe um registo de pagamento associado à inscrição:
   - 2.a.1 - O administrador escolhe a opção "Atualizar dados de pagamento".
   - 2.a.2 - Retorna ao passo 4.
 - 6.a - Dados inválidos:
   - 6.a.1 - O sistema informa o erro.
   - 6.a.2 - O administrador reintroduz os dados e retorna ao passo 5.

<a id="uc10"></a>
## UC10 - Exportar Lista de Participantes

**Atores principais:** Administrador  
**Objetivo:** Exportar ou imprimir a lista de participantes para apoio logístico.

### Pré-Condições
 - O administrador está autenticado.  
 - Existem inscrições no evento selecionado.

### Pós-Condições
 - Um ficheiro exportado é gerado ou uma página de impressão é apresentada.

### Fluxo Principal
1. O administrador acede à lista de participantes de um evento específico ([UC7](#uc7)).  
2. O administrador seleciona a opção "Exportar" ou "Imprimir".  
3. O sistema gera o ficheiro ou página para impressão contendo, para cada inscrição:
   - **ID de inscrição**,
   - nome e e-mail do participante,
   - tipo de inscrição,
   - indicação de estudante/não estudante e número de aluno (se aplicável),
   - opções adicionais,
   - estado de pagamento.

<a id="uc11"></a>
## UC11 - Consultar Eventos com Inscrições Abertas

**Atores principais:** Participante  
**Objetivo:** Ver a lista de eventos cuja inscrição está disponível.

### Pré-Condições
 - Nenhuma.

### Pós-Condições
 - Nenhuma (Use case apenas de leitura).

### Fluxo Principal
1. O participante acede à página pública de eventos.  
2. O sistema verifica quais os eventos com período de inscrições aberto (com base nas datas e no estado ativo) e apresenta uma lista com:
   - nome,
   - local,
   - datas principais,
   - **ID de evento**.

### Extensões/Exceções
 - 2.a - Não há eventos disponíveis:
   - 2.a.1 - O sistema informa que não há eventos ativos com inscrições abertas.

<a id="uc12"></a>
## UC12 - Consultar Detalhes de Evento

**Atores principais:** Participante  
**Objetivo:** Ver detalhes completos de um evento específico.

### Pré-Condições
 - O evento existe (com ID de evento válido).

### Pós-Condições
 - Nenhuma (Use case apenas de leitura).

### Fluxo Principal
1. O participante acede à lista de eventos com inscrições abertas ([UC11](#uc11)).  
2. O participante seleciona um evento da lista.  
3. O sistema apresenta:
   - nome, descrição e **ID de evento**
   - local, datas e horário,
   - tipos de inscrição disponíveis (estudante / não estudante),
   - opções adicionais disponíveis (almoço, jantar, etc.),
   - instruções gerais de pagamento (transferência bancária),
   - período de inscrições (datas de inicio e fim),
   - botão de inscrição.

<a id="uc13"></a>
## UC13 - Efetuar Inscrição

**Atores principais:** Participante  
**Objetivo:** Submeter uma inscrição num evento.

### Pré-Condições
 - O evento está com inscrições abertas (dentro do período definido).  
 - Existem vagas disponíveis (número de inscrições abaixo do número máximo de participantes do evento).

### Pós-Condições
 - É criada uma nova inscrição associada ao **ID de evento**.  
 - A inscrição é criada com estado **pendente de pagamento**.  
 - É gerado um **ID de inscrição** único e associado ao registo.  
 - O participante recebe o **ID de inscrição** e as instruções de pagamento.

### Fluxo Principal
1. O participante acede à página de detalhes do evento pretendido ([UC12](#uc12)).  
2. O participante clica no botão "Inscrever".  
3. O sistema apresenta o formulário de inscrição.  
4. O participante preenche:
   - dados pessoais (nome, e-mail, etc.),
   - tipo de inscrição - não estudante (opção padrão),
   - opções adicionais desejadas (respeitando opções obrigatórias).  
5. O sistema determina automaticamente:
   - a fase de inscrição em vigor (com base na data/hora atual),
   - o preço base correspondente ao tipo de inscrição e fase.  
6. O sistema calcula:
   - o valor das opções adicionais selecionadas (e das obrigatórias),
   - o **valor total** da inscrição.  
7. O sistema apresenta ao participante um resumo com:
   - dados inseridos,
   - tipo de inscrição,
   - fase aplicada,
   - valor total a pagar.  
8. O participante confirma os dados e submete a inscrição.  
9. O sistema:
   - cria o registo de inscrição,
   - atribui um **ID de inscrição** único,
   - guarda o número de aluno (se inserido),
   - regista o estado inicial como **pendente de pagamento**,
   - associa a inscrição ao **ID de evento** correspondente.  
10. O sistema apresenta ao participante:
    - o **ID de inscrição**,
    - o valor total,
    - os dados para transferência bancária (IBAN, entidade beneficiária, etc.),
    - a descrição obrigatória a colocar na transferência (podendo incluir o **ID de inscrição** e **ID de evento**).  
11. O sistema envia estas informações por e-mail para o endereço fornecido pelo participante.

### Extensões/Exceções
 - 4.a – O participante escolhe tipo “estudante”:
   - 4.a.1 – O sistema informa que o número de aluno é obrigatório para inscrições de estudante.
   - 4.a.2 – O participante deve preencher o número de aluno para continuar.
   - 4.a.3 - O sistema procede à validação automática do número de aluno ([UC18](#uc18)).  
   - 4.a.4 - Retorna ao passo 5.

<a id="uc14"></a>
## UC14 - Consultar Estado da Inscrição

**Ator principal:** Participante  
**Objetivo:** Verificar estado atual da inscrição.

### Pré-condições
 - A inscrição existe e o participante consegue identificá-la (através do **ID de inscrição** e do email).

### Pós-condições
 - Nenhuma (Use case apenas de leitura).

### Fluxo principal
1. O participante acede à sua página pública de consulta de inscrições.  
2. O sistema pede identificação da inscrição (por exemplo, **ID de inscrição**).  
3. O participante introduz os dados pedidos.  
4. O sistema verifica se existe uma inscrição com esses dados.  
5. O sistema apresenta:
   - dados principais da inscrição (evento, tipo de inscrição, opções),
   - **ID de inscrição**,
   - instruções e dados de pagamento,
   - estado atual (pendente, pago, cancelado).

### Extensões/Exceções
 - 4.a – Dados inválidos ou inscrição não encontrada:
   - 4.a.1 – O sistema informa que não foi encontrada nenhuma inscrição com os dados indicados.
   - 4.a.2 - Retorna ao passo 3.

<a id="uc15"></a>
## UC15 - Procurar Participante

**Atores principais:** Operador de Check-In  
**Objetivo:** Encontrar um participante no dia do evento para efeitos de check-in ou registo de pagamento excecional.

### Pré-condições
 - O operador está autenticado ([UC1](#uc1)).  
 - Existem inscrições para o evento em questão.

### Pós-condições
 - Nenhuma (consulta).

### Fluxo principal
1. O operador acede ao módulo de check-in.  
2. O operador procura pelo evento através do seu nome ou ID.  
3. O operador seleciona o evento atual.  
4. O operador introduz critérios de pesquisa, podendo usar:
   - nome do participante,
   - e-mail,
   - **ID de inscrição**.  
5. O sistema apresenta a lista de inscrições correspondentes com:
   - **ID de inscrição**,
   - nome,
   - e-mail,
   - estado de pagamento.  
6. O operador seleciona um participante para ver detalhes.

### Extensões/Exceções
 - 5.a – Não foram encontradas inscrições com os critérios de pesquisa usados:
   - 5.a.1 – O sistema informa que não foi encontrada nenhuma inscrição com os critérios indicados.
   - 5.a.2 - Retorna ao passo 4.

<a id="uc16"></a>
## UC16 - Registar Check-In

**Atores principais:** Operador de Check-In  
**Objetivo:** Registar a entrada do participante no evento.

### Pré-condições
 - O operador está autenticado ([UC1](#uc1)).  
 - A inscrição do participante foi localizada ([UC15](#uc15)). 
 - A inscrição está em estado **paga**.

### Pós-condições
 - O check-in do participante fica registado com data e hora.

### Fluxo principal
1. O operador acede aos detalhes da inscrição do respetivo participante ([UC15](#uc15)).  
2. O sistema apresenta os detalhes da inscrição, incluindo:
   - **ID de inscrição**,
   - nome do participante,
   - tipo de inscrição,
   - opções adicionais,
   - dados de pagamento,
   - estado de pagamento.  
3. O sistema confirma que o estado de pagamento permite o check-in (o estado deve ser "pago").  
4. O operador seleciona "Registar check-in".  
5. O sistema regista:
   - a data e hora do check-in do participante,
   - o utilizador (operador) que realizou o check-in.  
6. O sistema marca a inscrição como presente no evento.

### Extensões/Exceções
 - 3.a – Pagamento não confirmado:
   - 3.a.1 – O sistema informa que o check-in não é permitido porque o estado do pagamento não é "pago".
   - 3.a.2 – O operador pode iniciar ([UC17](#uc17)) (se aplicável) – Registar Pagamento Excecional no Local - ao clicar em "Registar Pagamento".
   - 3.a.3 - Após [UC17](#uc17), o fluxo retorna ao passo 4.

<a id="uc17"></a>
## UC17 - Registar Pagamento Excecional no Local

**Atores principais:** Operador de Check-In  
**Objetivo:** Tratar situações excecionais em que o pagamento é efetuado no dia do evento.

### Pré-condições
 - O operador está autenticado ([UC1](#uc1)).  
 - A inscrição do participante existe (identificada, por exemplo, pelo **ID de inscrição** via [UC15](#uc15)).
 - O estado de pagamento da inscrição é "pagamento_pendente".  
 - O participante apresenta comprovativo de transferência bancária efetuada no momento ou imediatamente antes.

### Pós-condições
 - Um registo de pagamento é associado à inscrição.  
 - O estado do pagamento é atualizado para **paga** (se tudo estiver conforme).  
 - O check-in pode ser efetuado de seguida ([UC16](#uc16)).

### Fluxo principal
1. O sistema apresenta um formulário para registar:
   - valor,
   - data da transferência,
   - notas (por exemplo, referência do comprovativo).  
2. O operador introduz os dados providenciados pelo participante (comprovativo de transferência) e confirma.  
3. O sistema regista o pagamento associado ao **ID de inscrição** e, se estiver de acordo com o valor devido, atualiza o estado da inscrição para "**paga**".

### Extensões/Exceções
 - 3.a – Valor do pagamento não conforme:
   - 3.a.1 – O sistema informa que o valor transferido não coincide com o valor devido.
   - 3.a.2 - O operador deve informar o valor que falta transferir ao participante.
   - 3.a.3 - Após uma nova transferência do participante, retorna ao passo 2.


<a id="uc18"></a>
## UC18 - Verificação do Número de Aluno

**Ator principal:** Sistema  
**Objetivo:** Validar o número de aluno e autorizar a inscrição associada.

### Pré-condições
 - Um participante tentou submeter uma inscrição do tipo "estudante".

### Pós-condições
 - O número de aluno é validado e consequentemente a inscrição também.

### Fluxo principal
1. O sistema verifica que o número de aluno inserido existe e está ativo.  
2. O sistema verifica que o número de aluno inserido está associado aos dados pessoais fornecidos (nome, e-mail).  
3. O sistema valida e autoriza a respetiva inscrição.

### Extensões/Exceções
 - 1.a – Número de aluno não existe ou está inativo:
   - 1.a.1 – O sistema informa o erro e pede ao participante para reintroduzir o número de aluno.
   - 1.a.2 - O participante reintroduz o número de aluno.
   - 1.a.3 - Retorna ao passo 1.
 - 2.a – Número de aluno não coincide com os dados pessoais fornecidos:
   - 2.a.1 – O sistema informa o erro e pede ao participante para reintroduzir o número de aluno ou os dados pessoais.
   - 2.a.2 - O participante reintroduz os dados que considere que estão errados.
   - 2.a.3 - Retorna ao passo 1.
