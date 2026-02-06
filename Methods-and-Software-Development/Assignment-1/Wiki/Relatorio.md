# 1. Introdução
Este documento sumariza as principais decisões de design e interpretações de requisitos realizadas durante a fase de especificação do sistema. As escolhas descritas abaixo visam garantir a segurança, usabilidade e a consistência dos dados, respeitando o enunciado original.

# 2. Decisões
## 2.1. Autenticação
Uma das principais decisões prende-se com a distinção entre os tipos de utilizadores e a necessidade de autenticação.
O participante interage com o sistema como um utilizador não autenticado ("Guest"). Já os utilizadores "administrador" e "operador check-in" têm de efetuar autenticação de modo a ter acesso às suas funcionalidades individuais.

Esta escolha facilita o processo de inscrição e utilização do participante. A segurança e o acompanhamento da inscrição são garantidos através de um ID de Inscrição único gerado pelo sistema e enviado por e-mail, que serve como chave de acesso para consultas e operações futuras.

## 2.2. Utilizador Operador de Check-in
Separação de Administrador e Operador de Check-In: Decidiu-se criar um perfil específico para o "Operador de Check-In", distinto do Administrador.

Aplicação do princípio do privilégio mínimo. O operador apenas necessita de validar entradas e registar pagamentos locais excecionais, não devendo ter acesso à gestão global de eventos, criação de preços ou visualização de dados financeiros sensíveis fora do contexto do check-in.

## 2.3 Geração automática de ID de evento/inscrição
O sistema gera automaticamente um identificador único para cada evento/inscrição, garantindo unicidade e evitando conflitos, o que facilita referencias futuras.

## 2.4. Inativação de Eventos
Definiu-se que a inativação de um evento não é apenas uma mudança de estado visual, leva ao cancelamento de todas as inscrições associadas e notificação automática aos participantes sobre reembolsos. Garante que não existem inscrições "ativas" em eventos que já não vão ocorrer, mantendo a consistência da base de dados.

## 2.5. Sobreposição de Fases
O sistema implementa uma validação cronológica que impede a criação de fases de inscrição com datas sobrepostas (ex: Early e Late acontecerem ao mesmo tempo). Evita ambiguidade no cálculo automático do preço a aplicar no momento da inscrição.

## 2.6 Tipos de inscrição
Foram criados apenas dois tipos de inscrição, "Estudante" e "Não Estudante", de forma a simplificar a gestão do sistema.

## 2.7. Validação de Estudante
Para evitar fraudes nas tarifas reduzidas, especificou-se que o sistema deve validar o número de aluno cruzando-o com os dados pessoais (nome/e-mail).
No ato de efetuar inscrição com tipo estudante é obrigatório introduzir o número de aluno sujeito a verificação automática pelo sistema e correspondência dos dados para garantir que o número pertence ao participante inscrito.

## 2.8. Cálculo valor total
O cálculo do valor total a pagar é realizado no momento da submissão da inscrição e fica registado.
Alterações futuras nos preços ou nas fases pelo administrador não devem afetar retroativamente inscrições já registadas ("preço fechado").

## 2.9. Fluxo de Pagamentos e Estados
Dada a restrição do enunciado de que os pagamentos são exclusivamente por transferência bancária, o sistema foi desenhado para gerir a assincronia entre a inscrição e o pagamento.

O sistema não processa pagamentos bancários automaticamente. Foi decidido que a mudança de estado de "Pendente" para "Pago" depende exclusivamente de uma ação manual do Administrador após conferência bancária externa.

## 2.10. Pagamento Excecional no Local:
Foi incluída uma exceção ao fluxo normal para permitir pagamentos no dia do evento.
Embora o foco seja a transferência prévia, situações reais exigem flexibilidade. O operador pode validar a entrada mediante a apresentação de um comprovativo de transferência realizado no momento.

# 3. Diagramas
## 3.1. Diagrama de Use Cases
Para desenhar o diagrama de use cases associámos cada use case ao seu ator principal e seguimos as descrições dos use cases que já haviam sido escritas por nós para indicar as relações de "includes" e "extends". Não usamos relações de "herança".

## 3.2. Diagrama de Classes
Para desenhar o diagrama de classes começámos por criar as classes "administrador" e "operador de check-in" que são generalizações da classe "utilizador", que tem dois atributos (as credenciais de autenticação) e uma operação (o ato de autenticação). De seguida criámos a classe "Evento" com todos os atributos solicitados no enunciado e decidimos que as fases de inscrição do evento, assim como as opções adicionais e inscrições do mesmo seriam composições, sendo a classe "dona" o evento, pois sem o evento estas classes não podem existir. O administrador e o operador de check-in possuem todas as operações necessárias para a gestão do sistema. Para complementar a estrutura do diagrama, criámos três classes de enumeração, que indicam o tipo de inscrição, o estado da inscrição e o estado do pagamento.