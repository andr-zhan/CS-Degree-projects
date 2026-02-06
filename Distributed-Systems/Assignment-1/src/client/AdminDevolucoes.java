package client;

import server.Utilizador;

import java.util.List;

public class AdminDevolucoes {

    /*
     * Classe de interface de administração para devoluções e pedidos de remoção de suspensão.
     * Contém métodos de menu e ações que um administrador usa via cliente socket (ClienteAdmin).
     * As respostas ao servidor podem ser booleanas ou arrays com informação adicional.
     */

    /**
     * Exibe o menu principal de "Devoluções & Suspensões" para o administrador.
     * Mostra opções para gerir devoluções pendentes e pedidos de remoção de suspensão
     * e encaminha para os métodos correspondentes conforme a opção escolhida.
     */
    public static void menu() {
        while (true) {
            System.out.println("\n----- ⚙️  Devoluções & Suspensões -----");
            System.out.println("1) 🔄 Gerir devolução (✅/❌)");
            System.out.println("2) ⚠️  Gerir pedidos de remoção de suspensão");
            System.out.println("0) ↩️  Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro();
            switch (op) {
                case 1 -> gerirDevolucoes();
                case 2 -> gerirPedidosRemocaoSuspensao();
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

    /**
     * Lista devoluções pendentes consultando o servidor e permite ao admin
     * aprovar ou negar uma devolução indicada pelo ID do empréstimo.
     * Ao negar, envia um motivo e o servidor pode suspender/bloquear o utilizador.
     */
    private static void gerirDevolucoes() {
        // pede ao servidor a lista de devoluções pendentes
        Object resposta = ClienteAdmin.enviarComando("LISTAR_DEVOLUCOES_PENDENTES", null);

        // Se o servidor devolveu uma lista, imprime cada entrada (texto com informações do empréstimo)
        if (resposta instanceof List<?> lista) {
            lista.forEach(System.out::println);
        }

        // Ler id do empréstimo a tratar
        System.out.print("📚 ID do empréstimo: ");
        int id = ClienteAux.lerInteiro();
        System.out.print("✅ Aprovar (A) ou ❌ Negar (N)? ");

        String opt;
        do {
            opt = ClienteAdmin.sc.nextLine().trim().toUpperCase();
            if (!opt.equals("A") && !opt.equals("N")) {
                System.out.print("❌ Opção inválida. Introduza 'A' para aprovar ou 'N' para negar: ");
                opt = ClienteAdmin.sc.nextLine().trim().toUpperCase();
            }
        } while (!opt.equals("A") && !opt.equals("N"));

        if (opt.equals("A")) {
            // envia pedido de aprovação ao servidor (recebe booleano)
            resposta = ClienteAdmin.enviarComando("APROVAR_DEVOLUCAO", id);
        } else {
            // envia pedido de negação com motivo (resulta na suspensão do utilizador)
            System.out.print("Motivo: ");
            String motivo = ClienteAdmin.sc.nextLine();
            resposta = ClienteAdmin.enviarComando("NEGAR_DEVOLUCAO", new Object[]{id, motivo});
        }

        // Tratar duas formas de resposta usadas historicamente: boolean ou Object[]{boolean, Utilizador}
        if (resposta instanceof Boolean) {
            boolean ok = (Boolean) resposta;
            System.out.println(ok ? "✅ Operação concluída" : "❌ Falha na operação.");
        } else if (resposta instanceof Object[]) {
            Object[] arr = (Object[]) resposta;
            boolean ok = (Boolean) arr[0];
            Object extra = arr[1];
            System.out.println(ok ? "✅ Operação concluída" : "❌ Falha na operação.");

            // Se o servidor devolveu também um Utilizador, informa sobre bloqueios automáticos
            if (ok && extra instanceof Utilizador) {
                Utilizador u = (Utilizador) extra;
                if ("bloqueado".equalsIgnoreCase(u.getEstadoOperacional()) || u.getSuspensoesCount() >= 3) {
                    System.out.println("⚠️ Utilizador ID:" + u.getId() + " automaticamente bloqueado, ultrapassou limite suspensões.");
                }
            }
        } else {
            System.out.println("❌ Resposta inesperada do servidor.");
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Lista pedidos de remoção de suspensão criados pelos utilizadores (estado 'aguarda_suspensao')
     * e permite ao admin aprovar ou negar cada pedido, enviando o motivo se negar.
     */
    private static void gerirPedidosRemocaoSuspensao() {
        Object resposta = ClienteAdmin.enviarComando("LISTAR_PEDIDOS_REMOVER_SUSPENSAO", null);

        if (resposta instanceof List<?> pedidos) {
            if (pedidos.isEmpty()) {
                System.out.println("❌ Sem pedidos.");
                return;
            }
            pedidos.forEach(System.out::println);
        }

        // Ler id do utilizador cujo pedido será tratado
        System.out.print("👤 ID do utilizador: ");
        int id = ClienteAux.lerInteiro();
        System.out.print("✅ Aprovar (A) ou ❌ Negar (N)? ");
        String opt = ClienteAdmin.sc.nextLine().toUpperCase();

        if (opt.equals("A")) {
            // envia pedido de aprovação
            resposta = ClienteAdmin.enviarComando("APROVAR_REMOVER_SUSPENSAO", id);
        } else {
            // envia pedido de negação com motivo
            System.out.print("Motivo: ");
            String motivo = ClienteAdmin.sc.nextLine();
            resposta = ClienteAdmin.enviarComando("NEGAR_REMOVER_SUSPENSAO", new Object[]{id, motivo});
        }

        System.out.println((boolean) resposta ? "✅ Sucesso!" : "❌ Erro.");
    }

}
