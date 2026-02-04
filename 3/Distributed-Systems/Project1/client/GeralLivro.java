package client;

import server.BibliotecaService;
import server.Livro;

import java.util.List;
import java.util.Scanner;

public class GeralLivro {

    /*
     * Módulo de operações relativas a livros para o cliente geral via RMI.
     * Permite registar livros, listar disponíveis, consultar estado/histórico e obter sugestões.
     */

    // Exibe menu de operações de livro e despacha conforme escolha
    public static void menu(BibliotecaService service, Scanner scanner) {
        while (true) {
            System.out.println("\n----- 📖 Livro -----");
            System.out.println("1) 📖 Registar");
            System.out.println("2) 📜 Listar livros disponíveis");
            System.out.println("3) 🔎 Consultar estado");
            System.out.println("4) 🔎 Consultar histórico");
            System.out.println("5) 💡 Sugestões");
            System.out.println("0) ↩️  Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro(scanner);
            switch (op) {
                case 1 -> registarLivro(service, scanner);
                case 2 -> listarLivrosDisponiveis(service, scanner);
                case 3 -> consultarEstadoLivro(service, scanner);
                case 4 -> consultarHistoricoLivro(service, scanner);
                case 5 -> sugerirLivrosPorTitulo(service, scanner);
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Regista um livro no sistema e fica pendente de aprovação administrativa
    private static void registarLivro(BibliotecaService service, Scanner scanner) {
        System.out.print("🏷️  Título: ");
        String titulo = scanner.nextLine();
        System.out.print("✍🏻 Autor: ");
        String autor = scanner.nextLine();
        System.out.print("📌 Categoria: ");
        String categoria = scanner.nextLine();

        try {
            boolean ok = service.registarLivro(titulo, autor, categoria);
            System.out.println(ok ? "✅ Livro registado!" : "❌ Falha ao registar");
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lista livros disponíveis, opcionalmente filtrados por categoria/autor
    private static void listarLivrosDisponiveis(BibliotecaService service, Scanner scanner) {
        System.out.print("📌 Categoria (ENTER para ignorar): ");
        String cf = scanner.nextLine();
        System.out.print("✍🏻 Autor (ENTER para ignorar): ");
        String af = scanner.nextLine();

        try {
            List<Livro> livros = service.listarLivrosDisponiveis(
                    cf.isBlank() ? null : cf,
                    af.isBlank() ? null : af
            );
            if (livros == null || livros.isEmpty()) {
                System.out.println("❌ Não existem livros disponíveis.");
                return;
            }
            System.out.println("\n--- 📖 Livros disponíveis ---");
            livros.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta e imprime o estado operacional de um livro
    private static void consultarEstadoLivro(BibliotecaService service, Scanner scanner) {
        System.out.print("📖 ID do livro: ");
        int id = ClienteAux.lerInteiro(scanner);
        try {
            System.out.println("⚙️  Estado: " + service.consultarEstadoLivro(id));
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta e imprime o histórico de empréstimos do livro
    private static void consultarHistoricoLivro(BibliotecaService service, Scanner scanner) {
        System.out.print("📖 ID do livro: ");
        int id = ClienteAux.lerInteiro(scanner);
        try {
            java.util.List<String> hist = service.consultarHistoricoLivro(id);
            System.out.println("\n--- 📃 HISTÓRICO ---");
            if (hist == null || hist.isEmpty()) {
                System.out.println("❌ Não existe histórico para este livro.");
                return;
            }
            hist.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Solicita ao serviço sugestões de livros com base no título de referência
    private static void sugerirLivrosPorTitulo(BibliotecaService service, Scanner scanner) {
        System.out.print("🏷️ Título do livro de referência: ");
        String tituloRef = scanner.nextLine();
        try {
            java.util.List<Livro> sugestoes = service.sugerirLivrosPorTitulo(tituloRef);
            if (sugestoes == null || sugestoes.isEmpty()) {
                System.out.println("❌ Nenhuma sugestão encontrada para o título fornecido.");
            } else {
                System.out.println("\n--- 💡 SUGESTÕES ---");
                sugestoes.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }
}
