package client;

import server.Utilizador;
import server.BibliotecaService;

import java.sql.Date;
import java.util.Scanner;

public class ClienteAux {

    /*
     * Pequenas utilidades compartilhadas pelos clientes:
     * - Leitura robusta de inteiros
     * - Leitura de strings que podem ser nulas (ENTER -> null)
     * - Verificações relacionadas com suspensão/bloqueio de utilizadores
     * - Parsing de datas (YYYY-MM-DD) com repetição em caso de erro
     */

    // Lê um inteiro do scanner fornecido, repetindo até receber um valor válido
    public static int lerInteiro(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Valor inválido. Digite novamente: ");
            }
        }
    }

    // Conveniência: usa o scanner global do ClienteAdmin
    public static int lerInteiro() {
        return lerInteiro(ClienteAdmin.sc);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lê uma linha e transforma em null se estiver vazia/whitespace
    public static String lerOuNull(Scanner scanner) {
        String txt = scanner.nextLine().trim();
        return txt.isBlank() ? null : txt;
    }

    public static String lerOuNull() {
        return lerOuNull(ClienteAdmin.sc);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Verifica o estado operacional de um utilizador (bloqueado / suspenso / ativo).
     * - Se bloqueado: informa e impede a operação.
     * - Se suspenso: pergunta se o utilizador quer pedir remoção da suspensão e
     *   envia o pedido via service.pedirRemoverSuspensao.
     * - Se tudo OK, retorna o objeto Utilizador.
     */
    public static Utilizador verificarSuspensao(BibliotecaService service, Scanner scanner, int uID) {
        try {
            Utilizador util = service.consultarUtilizadorPorId(uID);
            if (util == null) {
                System.out.println("❌ Utilizador não encontrado.");
                return null;
            }
            String estadoOp = util.getEstadoOperacional();
            if ("bloqueado".equalsIgnoreCase(estadoOp)) {
                String motivo = util.getMotivoSuspensao();
                int suspCount = util.getSuspensoesCount();
                if (suspCount >= 3) {
                    System.out.println("⚠️ Utilizador BLOQUEADO (automático por exceder o limite de suspensões). Motivo: " + motivo);
                } else {
                    System.out.println("⚠️ Utilizador BLOQUEADO (bloqueio manual pelo admin). Motivo: " + motivo);
                }
                System.out.println("❌ Operação não permitida enquanto o utilizador estiver bloqueado.");
                return null;
            }
            if ("suspenso".equalsIgnoreCase(estadoOp)) {
                System.out.println("⚠️ Utilizador suspenso. Motivo: " + util.getMotivoSuspensao());
                System.out.print("❓ Deseja pedir remoção da suspensão? (S/N): ");

                String resp;
                do {
                    resp = scanner.nextLine().trim();
                    if (!resp.equalsIgnoreCase("S") && !resp.equalsIgnoreCase("N")) {
                        System.out.print("❌ Resposta inválida. Introduza 'S' para sim ou 'N' para não: ");
                        resp = scanner.nextLine().trim();
                    }
                } while (!resp.equalsIgnoreCase("S") && !resp.equalsIgnoreCase("N"));

                if (resp.equalsIgnoreCase("S")) {
                    System.out.print("👉 Justificação para remoção: ");
                    String just = scanner.nextLine();
                    boolean pedido = service.pedirRemoverSuspensao(uID, just);
                    System.out.println(pedido ? "✅ Pedido enviado ao admin." : "❌ Falha ao enviar pedido.");
                }
                return null;
            }
            return util;
        } catch (Exception e) {
            System.out.println("🚨[ERRO]: " + e.getMessage());
            return null;
        }
    }

    public static Utilizador verificarSuspensao(BibliotecaService service, int uID) {
        return verificarSuspensao(service, ClienteAdmin.sc, uID);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lê e converte uma data no formato YYYY-MM-DD; repete até obter formato correto ou ENTER
    public static Date parseDateOrNull(Scanner scanner) {
        while (true) {
            String dn = scanner.nextLine().trim();
            if (dn.isBlank()) return null;
            try {
                return Date.valueOf(dn);
            } catch (IllegalArgumentException ex) {
                System.out.print("❌ Formato inválido. Use YYYY-MM-DD ou ENTER para omitir: ");
            }
        }
    }

    public static Date parseDateOrNull() {
        return parseDateOrNull(ClienteAdmin.sc);
    }
}
