package client;

import java.rmi.Naming;
import java.util.Scanner;
import server.BibliotecaService;

public class ClienteGeral {

    /*
     * Cliente Geral que usa RMI para comunicar com a implementação do serviço
     * Este cliente apresenta um menu para operações de utilizador, empréstimos e livros.
     */

    // Referência remota ao serviço e scanner para input do utilizador
    private static BibliotecaService service;
    private static final Scanner scanner = new Scanner(System.in);

    // Inicializa o cliente RMI (procura o serviço no registry) e apresenta o menu
    public static void main(String[] args) {
        try {
            service = (BibliotecaService) Naming.lookup("rmi://localhost:1099/BibliotecaService");
            menuPrincipal();
        } catch (Exception e) {
            System.out.println("🚨[ERRO] no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Menu principal que encaminha para submenus específicos, passando a referência ao service
    private static void menuPrincipal() {
        int opcao;
        do {
            System.out.println("\n========= 💼 MENU GERAL =========");
            System.out.println("1) 👤 Utilizador");
            System.out.println("2) 📚 Empréstimos");
            System.out.println("3) 📖 Livro");
            System.out.println("0) 👋 Sair");
            System.out.print("👉 Escolha: ");

            opcao = ClienteAux.lerInteiro(scanner);
            switch (opcao) {
                case 1 -> GeralUtilizador.menu(service, scanner);
                case 2 -> GeralEmprestimos.menu(service, scanner);
                case 3 -> GeralLivro.menu(service, scanner);
                case 0 -> { System.out.println("👋 Encerrando cliente..."); return; }
                default -> System.out.println("❌ Opção inválida.");
            }

        } while (opcao != 0);
        scanner.close();
    }

}
