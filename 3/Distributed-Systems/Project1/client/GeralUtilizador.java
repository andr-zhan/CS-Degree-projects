package client;

import server.BibliotecaService;
import server.Utilizador;

import java.sql.Date;
import java.util.Scanner;

public class GeralUtilizador {

    /*
     * - Módulo de utilizador para o cliente geral via RMI.
     * - Permite: registar utilizador, apagar utilizador (se não houver empréstimos ativos associados) 
     *   e consultar estado operacional.
     */

    // Menu de utilizador que encaminha para operações pretendidas
    public static void menu(BibliotecaService service, Scanner scanner) {
        while (true) {
            System.out.println("\n----- 👤 Utilizador -----");
            System.out.println("1) 👤 Registar");
            System.out.println("2) 🗑️  Apagar");
            System.out.println("3) 🔍 Consultar estado");
            System.out.println("0) ↩️  Voltar");
            System.out.print("👉 Escolha: ");

            int op = ClienteAux.lerInteiro(scanner);
            switch (op) {
                case 1 -> registarUtilizador(service, scanner);
                case 2 -> apagarUtilizadorSeSemEmprestimos(service, scanner);
                case 3 -> consultarEstadoUtilizador(service, scanner);
                case 0 -> { return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Regista um novo utilizador que fica pendente de aprovação administrativa
    private static void registarUtilizador(BibliotecaService service, Scanner scanner) {
        System.out.print("👤 Nome: ");
        String nome = scanner.nextLine();
        System.out.print("📩 Email: ");
        String email = scanner.nextLine();
        System.out.print("📅 Data de nascimento (YYYY-MM-DD) (ENTER para omitir): ");
        Date dataNascimento = ClienteAux.parseDateOrNull(scanner);

        try {
            boolean ok = service.registarUtilizador(nome, email, dataNascimento);
            System.out.println(ok ? "✅ Utilizador registado!" : "❌ Falha ao registar");
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Apaga um utilizador apenas se não existirem empréstimos ativos associados
    private static void apagarUtilizadorSeSemEmprestimos(BibliotecaService service, Scanner scanner) {
        System.out.print("👤 ID do utilizador a apagar: ");
        int uID = ClienteAux.lerInteiro(scanner);
        try {
            Utilizador util = ClienteAux.verificarSuspensao(service, scanner, uID);
            if (util == null) return;

            boolean ok = service.apagarUtilizadorSeSemEmprestimos(uID);
            if (ok) System.out.println("✅ Utilizador apagado com sucesso.");
            else System.out.println("❌ Falha ao apagar utilizador: podem existem empréstimos ativos ou o utilizador não existe.");
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta e imprime o estado operacional do utilizador
    private static void consultarEstadoUtilizador(BibliotecaService service, Scanner scanner) {
        System.out.print("👤 ID do utilizador: ");
        int id = ClienteAux.lerInteiro(scanner);
        try {
            System.out.println("⚙️  Estado: " + service.consultarEstadoUtilizador(id));
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
        }
    }
}
