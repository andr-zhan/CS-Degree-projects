package server;

import java.io.FileInputStream;

// Sockets para cliente admin
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// RMI para cliente geral
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Properties;

public class Servidor {

    /*
     * Classe responsável por inicializar o serviço RMI e uma ligação por sockets para o cliente admin.
     * - Regista BibliotecaService no registry RMI (porta 1099)
     * - Inicia uma ligação por sockets que aceita comandos do ClienteAdmin e os mapeia para BibliotecaAdmin
     */

    private static int PORT; // Porto do servidor socket admin (padrão definido em properties)
    public static void main(String[] args) {

        try {

            // Iniciar serviço RMI
            BibliotecaServiceImpl service = new BibliotecaServiceImpl();
            Registry registry;

            try {
                registry = LocateRegistry.getRegistry(1099);
                registry.list(); // testa ligação ao registry
                System.out.println("🚨[ERRO] Registry já estava ativo na porta 1099.");
            } catch (Exception e) {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("✅ Registry criado na porta 1099.");
            }

            // Regista a implementação do serviço para clientes RMI
            registry.rebind("BibliotecaService", service);
            System.out.println("✅ Servidor RMI iniciado e serviço registado!");

            // Iniciar servidor socket para cliente admin (usa BibliotecaAdmin local, não RMI)
            iniciarServidorAdmin(new BibliotecaAdmin());

        } catch (RemoteException e) {
            System.out.println("🚨[ERRO] ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lê configuração e inicia uma thread que aceita ligações socket do ClienteAdmin
    private static void iniciarServidorAdmin(BibliotecaAdmin service) {

        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config/serverAdmin.properties"));
            PORT = Integer.parseInt(props.getProperty("socket.port", "6000"));
        } catch (Exception e) {
            System.out.println("🚨[ERRO] ao carregar config/server.properties. A usar valores padrão.");
            PORT = 6000;
        }

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("✅ Servidor admin (SOCKET) ativo em: " + PORT);

                while (true) {
                    Socket cliente = serverSocket.accept();
                    new Thread(() -> tratarAdmin(cliente, service)).start(); // tratar cada cliente em thread separada
                }
            } catch (Exception e) {
                System.out.println("🚨[ERRO] no servidor admin: " + e.getMessage());
            }
        }).start();
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Trata uma ligação do ClienteAdmin: lê comando e dados serializados e invoca o método correspondente
    private static void tratarAdmin(Socket cliente, BibliotecaAdmin service) {
        try (ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(cliente.getInputStream())) {

            String comando = (String) in.readObject();
            Object dados = in.readObject();

            Object resposta = switch (comando) {

                // Comandos mapeados para métodos de BibliotecaAdmin
                case "LISTAR_UTILIZADORES_POR_ESTADO" ->
                        service.listarUtilizadoresPorEstado((String) dados);

                case "LISTAR_LIVROS_POR_ESTADO" ->
                        service.listarLivrosPorEstado((String) dados);

                case "APROVAR_UTILIZADOR" ->
                        service.aprovarUtilizador((int) dados);

                case "APROVAR_LIVRO" ->
                        service.aprovarLivro((int) dados);

                case "ALTERAR_ESTADO_UTILIZADOR" -> {
                    Object[] v = (Object[]) dados;
                    // v[0]=id, v[1]=novoEstado, v[2]=motivo (pode ser null)
                    int uid = (int) v[0];
                    String nEstado = (String) v[1];
                    String motivo = v.length > 2 ? (String) v[2] : null;
                    yield service.alterarEstadoUtilizador(uid, nEstado, motivo);
                }

                case "ALTERAR_ESTADO_LIVRO" -> {
                    Object[] v = (Object[]) dados;
                    yield service.alterarEstadoLivro((int) v[0], (String) v[1]);
                }

                case "ALTERAR_DADOS_UTILIZADOR" -> {
                    Object[] v = (Object[]) dados;
                    yield service.alterarDadosUtilizador((int) v[0], (String) v[1], (String) v[2], (java.sql.Date) v[3]);
                }

                case "ALTERAR_DADOS_LIVRO" -> {
                    Object[] v = (Object[]) dados;
                    yield service.alterarDadosLivro((int) v[0], (String) v[1], (String) v[2], (String) v[3]);
                }

                case "HISTORICO_LIVRO" ->
                        service.consultarHistoricoLivro((int) dados);

                case "LISTAR_DEVOLUCOES_PENDENTES" ->
                        service.listarDevolucoesPendentes();

                case "APROVAR_DEVOLUCAO" ->
                        service.aprovarDevolucao((int) dados);

                case "NEGAR_DEVOLUCAO" -> {
                    // chamar método de negação e devolver também informação do utilizador
                    Object[] v = (Object[]) dados;
                    int emprestimoId = (int) v[0];
                    String motivo = (String) v[1];
                    boolean resultado = service.negarDevolucao(emprestimoId, motivo);
                    if (resultado) {
                        // tentar obter o utilizador associado ao empréstimo para informar o cliente admin
                        String estadoInfo = service.consultarEstadoEmprestimo(emprestimoId);
                        // estadoInfo contém 'UtilizadorID: <id>' no seu texto conforme implementação
                        int utilId = -1;
                        try {
                            String marker = "UtilizadorID:";
                            int idx = estadoInfo.indexOf(marker);
                            if (idx != -1) {
                                String rest = estadoInfo.substring(idx + marker.length()).trim();
                                String[] parts = rest.split("\\s+|\\|", 2);
                                utilId = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
                            }
                        } catch (Exception ex) {
                            utilId = -1;
                        }

                        // obter objeto Utilizador atualizado
                        server.Utilizador util = null;
                        if (utilId != -1) {
                            util = service.consultarUtilizadorPorId(utilId);
                        }

                        // devolve resultado e utilizador (pode ser null)
                        yield new Object[]{resultado, util};
                    } else {
                        // devolve apenas falha
                        yield new Object[]{false, null};
                    }
                }

                case "LISTAR_PEDIDOS_REMOVER_SUSPENSAO" ->
                        service.listarPedidosRemocaoSuspensao();

                case "APROVAR_REMOVER_SUSPENSAO" ->
                        service.aprovarPedidoRemocao((int) dados);

                case "NEGAR_REMOVER_SUSPENSAO" -> {
                    Object[] v = (Object[]) dados;
                    yield service.negarPedidoRemocao((int) v[0], (String) v[1]);
                }

                default -> "❌ Comando desconhecido";
            };

            // Envia resposta ao cliente admin
            out.writeObject(resposta);
            out.flush();

        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println("🚨[ERRO] ao tratar cliente admin: " + e.getMessage());
            }
        }
    } 
}
