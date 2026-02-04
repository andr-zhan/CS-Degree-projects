package client;

import java.sql.Date;
import java.util.List;

public class AdminUtilizadores {

    /*
     * Interface administrativa para gerir utilizadores.
     * Permite listar por estado, aprovar utilizadores, alterar estado operacional
     * e atualizar dados básicos (nome, email, data nascimento).
     */

    // Menu principal deste módulo; despacha para a ação escolhida
    public static void menu() {
        while (true) {
            System.out.println("\n----- 👤 Gestão de utilizadores -----");
            System.out.println("1) 📜 Listar por estado administrativo");
            System.out.println("2) ✅ Aprovar");
            System.out.println("3) ✏️  Alterar estado operacional");
            System.out.println("4) ✏️  Alterar dados");
            System.out.println("0) ↩️  Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro();
            switch (op) {
                case 1 -> listarUtilizadoresPorEstado();
                case 2 -> aprovarUtilizador();
                case 3 -> alterarEstadoUtilizador();
                case 4 -> alterarDadosUtilizador();
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Solicita ao servidor a lista de utilizadores filtrada pelo estado administrador
    private static void listarUtilizadoresPorEstado() {
        System.out.print("👤 Estado Utilizador (aprovado / nao_aprovado): ");

        String estado;
        do {
            estado = ClienteAdmin.sc.nextLine();
            if (!estado.equals("aprovado") && !estado.equals("nao_aprovado")) {
                System.out.print("❌ Estado inválido. Introduza 'aprovado' ou 'nao_aprovado': ");
            }
        } while (!estado.equals("aprovado") && !estado.equals("nao_aprovado"));

        Object resposta = ClienteAdmin.enviarComando("LISTAR_UTILIZADORES_POR_ESTADO", estado);

        if (resposta instanceof List<?> lista) {
            if (lista.isEmpty()) System.out.println("❌ Nenhum utilizador encontrado.");
            else lista.forEach(System.out::println);
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lista os utilizadores pendentes de aprovação e aprova o utilizador escolhido
    private static void aprovarUtilizador() {
        Object resposta1 = ClienteAdmin.enviarComando("LISTAR_UTILIZADORES_POR_ESTADO", "nao_aprovado");
        if (resposta1 instanceof List<?> lista) {
            if (lista.isEmpty()) {
                System.out.println("❌ Nenhum utilizador pendente de aprovação.");
                return;
            } else {
                lista.forEach(System.out::println);
            }
        }

    System.out.print("👤 ID do utilizador: ");
    int id = ClienteAux.lerInteiro();

        Object resposta2 = ClienteAdmin.enviarComando("APROVAR_UTILIZADOR", id);

        System.out.println(resposta2.equals(true) ? "✅ Utilizador aprovado!" : "❌ Falha ao aprovar utilizador.");
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Altera estado operacional de um utilizador (ativo / suspenso / bloqueado)
    // Quando for suspender/bloquear, pergunta e envia o motivo ao servidor
    private static void alterarEstadoUtilizador() {
    System.out.print("👤 ID do utilizador: ");
    int id = ClienteAux.lerInteiro();
        System.out.print("⚙️  Novo estado (ativo / suspenso / bloqueado): ");

        String estado;
        do {
            estado = ClienteAdmin.sc.nextLine();
            if (!estado.equals("ativo") && !estado.equals("suspenso") && !estado.equals("bloqueado")) {
                System.out.print("❌ Estado inválido. Introduza 'ativo', 'suspenso' ou 'bloqueado': ");
                estado = ClienteAdmin.sc.nextLine();
            }
        } while (!estado.equals("ativo") && !estado.equals("suspenso") && !estado.equals("bloqueado"));

        String motivo = null;
        if ("suspenso".equalsIgnoreCase(estado) || "bloqueado".equalsIgnoreCase(estado)) {
            System.out.print("Motivo: ");
            motivo = ClienteAdmin.sc.nextLine();
        }

        Object resposta = ClienteAdmin.enviarComando("ALTERAR_ESTADO_UTILIZADOR", new Object[]{id, estado, motivo});

        System.out.println((boolean) resposta ? "✅ Estado alterado!" : "❌ Falha ao alterar estado.");
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Atualiza os dados básicos do utilizador (nome, email, data nascimento)
    private static void alterarDadosUtilizador() {
    System.out.print("👤 ID do utilizador: ");
    int id = ClienteAux.lerInteiro();
    System.out.print("👤 Novo nome (ENTER mantém nome atual): ");
    String nome = ClienteAux.lerOuNull();
    System.out.print("📩 Novo email (ENTER mantém email atual): ");
    String email = ClienteAux.lerOuNull();
    System.out.print("📅 Nova data nascimento (YYYY-MM-DD ou ENTER para manter data atual): ");
    String dn = ClienteAux.lerOuNull();
        Date data = (dn == null ? null : Date.valueOf(dn));

        Object[] dados = {id, nome, email, data};

        Object resposta = ClienteAdmin.enviarComando("ALTERAR_DADOS_UTILIZADOR", dados);

        System.out.println((boolean) resposta ? "✅ Atualizado!" : "❌ Falha ao atualizar.");
    }

}
