package client;

import java.util.List;

public class AdminLivros {

    /*
     * Interface de administração para gestão de livros.
     * Contém menus para listar por estado administrativo, aprovar livros,
     * alterar estado operacional, atualizar dados básicos e consultar histórico.
     */

    // Mostra o menu deste módulo e despacha para as ações correspondentes
    public static void menu() {
        while (true) {
            System.out.println("\n----- 📚 Gestão de livros -----");
            System.out.println("1) 📜 Listar por estado administrativo");
            System.out.println("2) ✅ Aprovar");
            System.out.println("3) ✏️  Alterar estado operacional");
            System.out.println("4) ✏️  Alterar dados");
            System.out.println("5) 🔎 Consultar histórico");
            System.out.println("0) ↩️  Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro();
            switch (op) {
                case 1 -> listarLivrosPorEstado();
                case 2 -> aprovarLivro();
                case 3 -> alterarEstadoLivro();
                case 4 -> alterarDadosLivro();
                case 5 -> consultarHistoricoLivro();
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Pede ao servidor (via ClienteAdmin) a lista de livros filtrada pelo estado administrador
    private static void listarLivrosPorEstado() {
        System.out.print("📖 Estado do livro (aprovado / nao_aprovado): ");

        String estado;
        do {
            estado = ClienteAdmin.sc.nextLine();
            if (!estado.equals("aprovado") && !estado.equals("nao_aprovado")) {
                System.out.print("❌ Estado inválido. Introduza 'aprovado' ou 'nao_aprovado': ");
                estado = ClienteAdmin.sc.nextLine();
            }
        } while (!estado.equals("aprovado") && !estado.equals("nao_aprovado"));

        Object resposta = ClienteAdmin.enviarComando("LISTAR_LIVROS_POR_ESTADO", estado);

        if (resposta instanceof List<?> lista) {
            if (lista.isEmpty()) System.out.println("❌ Nenhum livro encontrado.");
            else lista.forEach(System.out::println);
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Aprova um livro pendente: lista pendentes e envia comando de aprovação
    private static void aprovarLivro() {
        Object resposta1 = ClienteAdmin.enviarComando("LISTAR_LIVROS_POR_ESTADO", "nao_aprovado");
        if (resposta1 instanceof List<?> lista) {
            if (lista.isEmpty()) {
                System.out.println("❌ Nenhum livro pendente de aprovação.");
                return;
            } else {
                lista.forEach(System.out::println);
            }
        }

    System.out.print("📖 ID do livro: ");
    int id = ClienteAux.lerInteiro();

        Object resposta2 = ClienteAdmin.enviarComando("APROVAR_LIVRO", id);

        System.out.println(resposta2.equals(true) ? "✅ Livro aprovado!" : "❌ Falha ao aprovar livro.");
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Altera o estado operacional de um livro (disponivel / emprestado / manutencao)
    private static void alterarEstadoLivro() {
    System.out.print("📖 ID do livro: ");
    int id = ClienteAux.lerInteiro();
        System.out.print("⚙️ Novo estado (disponivel / emprestado / manutencao): ");

        String estado;
        do {
            estado = ClienteAdmin.sc.nextLine();
            if (!estado.equals("disponivel") && !estado.equals("emprestado") && !estado.equals("manutencao")) {
                System.out.print("❌ Estado inválido. Introduza 'disponivel', 'emprestado' ou 'manutencao': ");
                estado = ClienteAdmin.sc.nextLine();
            }
        } while (!estado.equals("disponivel") && !estado.equals("emprestado") && !estado.equals("manutencao"));

        Object resposta = ClienteAdmin.enviarComando("ALTERAR_ESTADO_LIVRO", new Object[]{id, estado});

        System.out.println((boolean) resposta ? "✅ Estado alterado!" : "❌ Falha ao alterar estado.");
    }

    // Atualiza os atributos do livro (título, autor, categoria)
    private static void alterarDadosLivro() {
    System.out.print("📖 ID do livro: ");
    int id = ClienteAux.lerInteiro();
    System.out.print("🏷️ Novo título (ENTER mantém título atual): ");
    String titulo = ClienteAux.lerOuNull();
    System.out.print("✍🏻 Novo autor (ENTER mantém autor atual): ");
    String autor = ClienteAux.lerOuNull();
    System.out.print("📌 Nova categoria (ENTER mantém categoria atual): ");
    String categoria = ClienteAux.lerOuNull();
        Object resposta = ClienteAdmin.enviarComando("ALTERAR_DADOS_LIVRO", new Object[]{id, titulo, autor, categoria});

        System.out.println((boolean) resposta ? "✅ Atualizado!" : "❌ Falha ao atualizar.");
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta e imprime o histórico de empréstimos de um livro pelo seu ID
    private static void consultarHistoricoLivro() {
    System.out.print("📖 ID do livro: ");
    int id = ClienteAux.lerInteiro();

        Object resposta = ClienteAdmin.enviarComando("HISTORICO_LIVRO", id);

        if (resposta instanceof List<?> lista) {
            if (lista.isEmpty()) System.out.println("❌ Sem histórico.");
            else lista.forEach(System.out::println);
        }
    }

}
