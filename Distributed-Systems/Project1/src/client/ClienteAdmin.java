package client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class ClienteAdmin {

    /*
     * Cliente administrativo que comunica com o servidor via sockets.
     * - Lê configuração em config/serverAdmin.properties (HOST/PORT)
     * - Possui um método utilitário enviarComando que envia um comando String e um conteudo em Object
     *   e espera um Object como resposta do servidor.
     * - Contém o menu principal que chama os submenus de administração (utilizadores, livros, devoluções).
     */

    // Host e porto do servidor socket admin (lidos de properties)
    private static String HOST;
    private static int PORT;

    // Scanner global usado por todo o cliente admin
    public static final Scanner sc = new Scanner(System.in);

    // Bloco estático: carrega propriedades ao iniciar a classe
    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config/serverAdmin.properties"));
            HOST = props.getProperty("socket.host", "localhost");
            PORT = Integer.parseInt(props.getProperty("socket.port", "6000"));
        } catch (Exception e) {
            System.out.println("🚨[ERRO] ao carregar config/server.properties. A usar valores por defeito.");
            HOST = "localhost";
            PORT = 6000;
        }
    }

    // Ponto de entrada do cliente admin. Testa a ligação ao socket e mostra o menu.
    public static void main(String[] args) {
        try {
            testarLigacaoSocket();
            System.out.println("✅ Cliente ADMIN iniciado (via sockets)\n");
            menuPrincipal();
        } catch (Exception e) {
            System.out.println("🚨[ERRO] ao conectar ao servidor via sockets: " + e.getMessage());
            return;
        }
    }

    // Método utilitário que testa se o servidor socket está acessível
    private static void testarLigacaoSocket() throws Exception {
        Socket sock = new Socket(HOST, PORT);
        sock.close();
    }


    // ===================== SOCKET REQUEST =====================

    /*
     * Envia um comando em Object para o servidor admin via socket.
     * - comando: String que o servidor usa para decidir ação
     * - dados: pode ser null, um inteiro, array de Object, etc.
     * Retorna o Object lido do servidor (pode ser null se erro de comunicação).
     */
    public static Object enviarComando(String comando, Object dados) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(comando);
            out.writeObject(dados);
            out.flush();

            return in.readObject();

        } catch (Exception e) {
            System.out.println("🚨[ERRO] ao comunicar com servidor: " + e.getMessage());
            return null;
        }
    }

    // ===================== MENU =====================

    // Exibe o menu principal de administração e encaminha para submenus
    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n========= 🛠️  MENU ADMINISTRADOR =========");
            System.out.println("1) 👤 Gestão de utilizadores");
            System.out.println("2) 📖 Gestão de livros");
            System.out.println("3) ⚙️  Devoluções & Suspensões");
            System.out.println("0) 👋 Sair");
            System.out.print("👉 Escolha: ");

            int cat = ClienteAux.lerInteiro();
            switch (cat) {
                case 1 -> menuAdminUtilizadores();
                case 2 -> menuAdminLivros();
                case 3 -> menuAdminDevolucoes();
                case 0 -> { System.out.println("👋 Encerrando cliente..."); return; }
                default -> System.out.println("❌ Opção inválida.");
            }
        }
    }

    // --- Submenus do administrador (encaminham para classes específicas) ---
    private static void menuAdminUtilizadores() {
        client.AdminUtilizadores.menu();
    }

    private static void menuAdminLivros() {
        client.AdminLivros.menu();
    }

    private static void menuAdminDevolucoes() {
        client.AdminDevolucoes.menu();
    }
}
