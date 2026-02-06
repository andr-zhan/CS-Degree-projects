Esta Secção descreve, em linguagem próxima do utilizador final, o que cada tipo de utilizador pode fazer com o sistema Eventastic.

## Tipos de utilizador
- [**Administrador**](#ruAdministrador)
Utilizador responsável pela criação, configuração e gestão de eventos, bem como pelo controlo de inscrições e pagamentos.
- [**Participante**](#ruParticipante)
Pessoa que se inscreve em um ou mais eventos e realiza o pagamento da sua inscrição
- [**Operador de Check-In**](#ruOperadorCheckIn)
Utilizador que no dia do evento irá verificar o estado das inscrições/pagamentos e regista o check-in dos participantes.

**Decidimos considerar o operador de check-in como um utilizador distinto do Administrador de forma a poder restringir as suas permissões e ter acesso apenas ao necessário para realizar os check-in. Evitando dar permissões adicionais como gestão direta do evento.**

<a name="ruAdministrador"></a>
## RU - Administrador
### RU01 - Autenticar no sistema
O administrador deve poder autenticar-se no sistema de modo a aceder às funcionalidades de gestão dos eventos.

### RU02 - Consultar a lista de eventos
O administrador deve poder consultar a lista de todos os eventos, ativos ou expirados.

### RU03 - Criar/editar um evento
O administrador deve poder criar eventos indicando:
- nome;
- descrição;
- local;
- datas (início e fim);
- horário;
- número máximo de participantes;
- período global de inscrições (data de início e fim de inscriçôes).
Além disso, o administrador deve poder editar as informações de um evento já criado.

### RU04 - Inativar um evento
O administrador deve poder inativar eventos com inscrições abertas, exceto quando já estão a decorrer.
Se um evento for inativado, as inscrições associadas são canceladas e o administrador deve efetuar os reembolsos na totalidade.

### <a name="ru05Administrador"></a> RU05 - Definir fases de inscrição
O administrador deve poder definir múltiplas fases de inscrição distintas por evento, incluindo pelo menos:
- early (preço reduzido);
- late (preço regular ou aumentado);
- durante o evento (normalmente mais dispendioso ou sujeito a condições especiais).

cada fase tem que conter:
- nome;
- data de início;
- data de fim.

### RU06 - Definir tipos de inscrição e preço por fase
Para cada evento e fase, o administrador deve poder configurar:
- tipo de inscrição estudante;
- tipo de inscrição não estudante.

podendo definir para cada tipo, o preço para cada fase de inscrição definido em [RU05](#ru05Administrador).

### RU07 - Definir opções adicionais do evento
O administrador deve poder definir opções adicionais, como:
- almoço;
- jantar;
- coffee-break;
- outras atividades.

cada opção adicional tem:
- nome;
- descrição opcional;
- preço;
- indicador se é **opcional** ou **obrigatório**.

### RU08 - Ver lista de Participantes inscritos num evento
O administrador deve poder listar todos os participantes de um dado evento, incluindo:
- dados pessoais principais (nome, e-mail);
- tipo de inscrição;
- fase de inscrição aplicada;
- opções adicionais selecionadas;
- valor total a pagar;
- estado de pagamento.

### RU09 - Registar e atualizar pagamentos
O administrador deve poder registar e atualizar informações de pagamento por transferência bancária:
- valor transferido;
- data de transferência;
- notas internas;
- estado do pagamento (pendente, pago, cancelado).


### RU11 - Exportar ou imprimir listas de participantes
O administrador deve poder obter listas de participantes em formato adequado para:
- exportação;
- impressão.


<a name="ruParticipante"></a>
## RU - Participante
### RU01 - Consultar eventos com inscrições abertas
O participante deve poder consultar a lista de eventos que tem inscrições ainda abertas, com a informação associada a cada uma (nome, datas, local).

### RU02 - Consultar detalhes de um evento
O participante deve poder aceder á página de detalhes de um evento e visualizar:
- descrição;
- tipos de inscrição disponíveis;
- preços por fase temporal;
- opções adicionais disponíveis;
- instruções de pagamento. (transferência bancária, IBAN, descrição obrigatório, etc.).

### RU03 - Criar uma inscrição num evento
O participante deve poder preencher um formulário de inscrição fornecendo:
- dados pessoais mínimos (nome, e-mail,  NIF (opcional));
- tipo de inscrição (estudante ou não estudante);
- número de aluno (obrigatório se o tipo de inscrição for "estudante", sendo sujeito a verificação pelo sistema);
- seleção das opções adicionais pretendidas (exceto se forem obrigatórias).

após submeter o formulário, o sistema deve:
- criar a inscrição em estado pendente de pagamento;
- gerar um **identificador único da inscrição (ID da inscrição)**;
- apresentar esse **ID da inscrição** ao participante para futura referencia.


### RU04 - Ver cálculo automático do valor a pagar
Ao submeter ou durante o preenchimento da inscrição, o participante deve poder ver:
- a fase de inscrição em vigor (determinado automaticamente pela data atual);
- o valor base correspondente ao tipo de inscrição e fase;
- o valor adicional das opções selecionadas;
- o valor total a pagar.

### RU05 - Receber instruções de pagamento
Após efetuar a inscrição, o participante deve receber:
- os dados para a transferência bancária (IBAN, entidade beneficiária, etc.);
- descrição obrigatória a colocar na transferência; 
- valor total a transferir;
- o **ID da inscrição**.

**As instruções de pagamento devem ser imediatamente apresentadas e enviadas por e-mail após a submissão do formulário de inscrição. A inscrição fica em estado pendente de pagamento até o participante realizar a transferência e a mesma for verificada e registada pelos administradores.**

### RU06 - Verificar estado da inscrição
O participante deve poder consultar o estado da sua inscrição:
- pendente de pagamento;
- paga;
- cancelada.

o acesso ao estado da inscrição pode ser feito através:
- da introdução do **ID da inscrição** e outro dado de validação (por exemplo, e-mail).


<a name="ruOperadorCheckIn"></a>
## RU - Operador Check-In
### RU01 - Autenticar no sistema como operador
O operador deve poder autenticar-se para poder aceder ás funcionalidades de check-in.

### RU02 - Procurar participante
No dia do evento, o operador deve poder procurar participantes por:
- nome;
- e-mail;
- **ID da inscrição**.

### RU03 - Consultar informação relevante para Check-In
O operador deve poder consultar:
- tipo de inscrição;
- fase de inscrição;
- opções adicionais selecionadas;
- estado do pagamento (pago / pendente / cancelado).

### RU04 - Registar check-in caso pagamento tiver sido confirmado
Se o pagamento estiver confirmado, o operador deve poder registar o **check-in** do participante.

### RU05 - Registar pagamento excecional no local
Caso a organização permita excecionalmente pagamento no local, o operador deve poder:
- registar os dados do comprovativo de transferência efetuada no momento;
- marcar o estado do pagamento como "pago";
- registar o check-in.






