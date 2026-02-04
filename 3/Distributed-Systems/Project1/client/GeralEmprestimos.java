package client;

import server.BibliotecaService;
import server.Emprestimo;
import server.Utilizador;

import java.util.Scanner;

public class GeralEmprestimos {

    /*
     * Módulo de operações de empréstimos acessível ao cliente geral via RMI.
     * Contém ações para realizar empréstimos, devoluções, listar e verificar estados.
     */

    // Menu com operações relacionadas a empréstimos
    public static void menu(BibliotecaService service, Scanner scanner) {
        while (true) {
            System.out.println("\n----- 📚 Empréstimos -----");
            System.out.println("1) 📚 Realizar empréstimo");
            System.out.println("2) 🔄 Realizar devolução");
            System.out.println("3) 📜 Listar empréstimos ativos por utilizador");
            System.out.println("4) 🔎 Consultar estado");
            System.out.println("0) 👋 Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro(scanner);
            switch (op) {
                case 1 -> realizarEmprestimo(service, scanner);
                case 2 -> realizarDevolucao(service, scanner);
                case 3 -> listarEmprestimosAtivosPorUtilizador(service, scanner);
                case 4 -> verificarEstadoPorEmprestimo(service, scanner);
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Realiza um empréstimo: valida utilizador, pede id do livro e invoca o serviço
    private static void realizarEmprestimo(BibliotecaService service, Scanner scanner) {
        System.out.print("👤 ID do utilizador: ");
        int uID = ClienteAux.lerInteiro(scanner);

        try {
            Utilizador util = ClienteAux.verificarSuspensao(service, scanner, uID);
            if (util == null) return;

            System.out.print("📖 ID do livro: ");
            int lID = ClienteAux.lerInteiro(scanner);

            boolean ok = service.realizarEmprestimo(uID, lID);
            System.out.println(ok ? "✅ Empréstimo realizado!" : "❌ Falha no empréstimo");
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Inicia o fluxo de devolução: verifica se existe um empréstimo ativo e o estado do utilizador
    private static void realizarDevolucao(BibliotecaService service, Scanner scanner) {
        System.out.print("📖 ID do livro a devolver: ");
        int id = ClienteAux.lerInteiro(scanner);
        try {
            Emprestimo emp = service.consultarEmprestimoAtivoPorLivro(id);
            if (emp == null) {
                System.out.println("❌ Nenhum empréstimo ativo encontrado para o livro id=" + id);
                return;
            }

            Utilizador util = ClienteAux.verificarSuspensao(service, scanner, emp.getUtilizadorId());
            if (util == null) return;

            boolean ok = service.realizarDevolucao(id);
            System.out.println(ok ? "✅ Devolução registada!" : "❌ Falha na devolução");
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta o estado de um empréstimo específico (por id)
    private static void verificarEstadoPorEmprestimo(BibliotecaService service, Scanner scanner) {
        System.out.print("📚 ID do empréstimo: ");
        int id = ClienteAux.lerInteiro(scanner);
        try {
            String res = service.consultarEstadoEmprestimo(id);
            System.out.println("📋Resultado: " + res);
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lista todos os empréstimos ativos de um determinado utilizador
    private static void listarEmprestimosAtivosPorUtilizador(BibliotecaService service, Scanner scanner) {
        System.out.print("👤 ID do utilizador: ");
        int uID = ClienteAux.lerInteiro(scanner);
        try {
            Utilizador util = ClienteAux.verificarSuspensao(service, scanner, uID);
            if (util == null) return;

            java.util.List<Emprestimo> lista = service.listarEmprestimosAtivosPorUtilizador(uID);
            if (lista == null || lista.isEmpty()) {
                System.out.println("❌ Nenhum empréstimo ativo encontrado para o utilizador id=" + uID);
                return;
            }
            System.out.println("\n--- 📚 Empréstimos ativos do utilizador id=" + uID + " ---");
            lista.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }
}
