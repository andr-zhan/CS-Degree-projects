package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaAdmin {
    /*
     * Implementação server-side usada pelo servidor socket (cliente admin).
     * Contém métodos que realizam operações diretas na base de dados via JDBC
     */

    // Construtor padrão - nenhuma inicialização especial necessária
    public BibliotecaAdmin() {
    }

//==============================================================================================================================================
//============================================================= MÉTODOS UTILIZADORES ===========================================================
//==============================================================================================================================================

    // Retorna uma lista de Utilizador filtrada pelo campo estado administrativo
    public List<Utilizador> listarUtilizadoresPorEstado(String estado) {
        List<Utilizador> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, nome, email, data_nascimento, estado_operacional, estado_admin, motivo_suspensao, justificacao_remocao, COALESCE(suspensoes_count,0) AS suspensoes_count FROM utilizadores WHERE estado_admin=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Utilizador(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getDate("data_nascimento"),
                    rs.getString("estado_operacional"),
                    rs.getString("estado_admin"),
                    rs.getString("motivo_suspensao"),
                    rs.getString("justificacao_remocao"),
                    rs.getInt("suspensoes_count")
                ));
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] listarUtilizadoresPorEstado: " + e.getMessage());
        }
        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Marca um utilizador como aprovado a nível administrativo e define estado_operacional = 'ativo'
    public boolean aprovarUtilizador(int utilizadorId) {
        String sql = "UPDATE utilizadores " +
             "SET estado_admin='aprovado', estado_operacional='ativo', " +
             "suspensoes_count=0, motivo_suspensao=NULL, justificacao_remocao=NULL " +
             "WHERE id=?";
        return updateSimple(sql, utilizadorId);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------
  
  
    // Altera o estado operacional do utilizador. Se 'suspenso' incrementa o contador de suspensões
    // pode automaticamente bloquear o utilizador se atingir o limite (3).
    public boolean alterarEstadoUtilizador(int utilizadorId, String novoEstado, String motivo) {
        try (Connection conn = Database.getConnection()) {

            String check = "SELECT estado_admin FROM utilizadores WHERE id=?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, utilizadorId);
            ResultSet rsCheck = checkStmt.executeQuery();

            // Verifica se o utilizador está aprovado antes de permitir alteração de estado
            if (rsCheck.next()) {
                String estadoAtual = rsCheck.getString("estado_admin");
                if ("nao_aprovado".equalsIgnoreCase(estadoAtual)) {
                    System.out.println("❌ Não é possível alterar estado: utilizador não está aprovado.");
                    return false;
                }
            } else {
                System.out.println("❌ Utilizador não encontrado.");
                return false;
            }

            if ("suspenso".equalsIgnoreCase(novoEstado)) {
                String upd = "UPDATE utilizadores SET estado_operacional = ?, suspensoes_count = COALESCE(suspensoes_count,0) + 1, motivo_suspensao = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(upd);
                stmt.setString(1, novoEstado);
                stmt.setString(2, motivo);
                stmt.setInt(3, utilizadorId);
                stmt.executeUpdate();
                System.out.println("⚠️ Utilizador id=" + utilizadorId + " SUSPENSO. Motivo: " + motivo);

                String sel = "SELECT COALESCE(suspensoes_count,0) AS cnt FROM utilizadores WHERE id = ?";
                PreparedStatement selStmt = conn.prepareStatement(sel);
                selStmt.setInt(1, utilizadorId);
                ResultSet rs = selStmt.executeQuery();
                if (rs.next()) {
                    int cnt = rs.getInt("cnt");
                    if (cnt >= 3) {
                        String blk = "UPDATE utilizadores SET estado_operacional = 'bloqueado', estado_admin = 'nao_aprovado' WHERE id = ?";
                        PreparedStatement blkStmt = conn.prepareStatement(blk);
                        blkStmt.setInt(1, utilizadorId);
                        blkStmt.executeUpdate();
                        System.out.println("⚠️ Utilizador id=" + utilizadorId + " automaticamente BLOQUEADO após " + cnt + " suspensões");
                    }
                }

                return true;
            }

            if ("bloqueado".equalsIgnoreCase(novoEstado)) {
                String upd = "UPDATE utilizadores SET estado_operacional = 'bloqueado', estado_admin = 'nao_aprovado', motivo_suspensao = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(upd);
                stmt.setString(1, motivo);
                stmt.setInt(2, utilizadorId);
                stmt.executeUpdate();
                System.out.println("ℹ️ [INFO] Utilizador id=" + utilizadorId + " BLOQUEADO manualmente pelo admin. Motivo: " + motivo);
                return true;
            }

            System.out.println("ℹ️ [INFO] Alteração do estado do utilizador id=" + utilizadorId + " para '" + novoEstado + "'");
            return updateSimple("UPDATE utilizadores SET estado_operacional = ? WHERE id = ?", utilizadorId, novoEstado);

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] alterarEstadoUtilizador: " + e.getMessage());
            return false;
        }
    }

//-------------------------------------------------------------------------------------------------------------

    // Atualiza dados básicos do utilizador (usa COALESCE para manter valores existentes quando null)
    public boolean alterarDadosUtilizador(int utilizadorId, String nome, String email, java.sql.Date dataNascimento) {
        try (Connection conn = Database.getConnection()) {
            String sql =
                "UPDATE utilizadores SET " +
                "nome = COALESCE(?, nome), " +
                "email = COALESCE(?, email), " +
                "data_nascimento = COALESCE(?, data_nascimento) " +
                "WHERE id=?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);

            if (dataNascimento != null)
                stmt.setDate(3, dataNascimento);
            else
                stmt.setNull(3, java.sql.Types.DATE);

            stmt.setInt(4, utilizadorId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("ℹ️ [INFO] Dados do utilizador id=" + utilizadorId + " atualizados com sucesso.");
                return true;
            } else {
                System.out.println("⚠️ [AVISO] Tentativa de atualizar utilizador inexistente: id=" + utilizadorId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] alterarDadosUtilizador: " + e.getMessage());
            return false;
        }
    }

//==============================================================================================================================================
//================================================================ MÉTODOS LIVROS ==============================================================
//==============================================================================================================================================

    // Retorna uma lista de Livro filtrada pelo estado administrativo
    public List<Livro> listarLivrosPorEstado(String estado) {
        List<Livro> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM livros WHERE estado_admin=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("categoria"),
                        rs.getString("estado_operacional"),
                        rs.getString("estado_admin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] listarLivrosPorEstado: " + e.getMessage());
        }
        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Aprova um livro pendente e define estado_operacional = 'disponivel'
    public boolean aprovarLivro(int livroId) {
        String sql = "UPDATE livros SET estado_admin='aprovado', estado_operacional='disponivel' WHERE id=?";
        return updateSimple(sql, livroId);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Altera o estado operacional do livro
    public boolean alterarEstadoLivro(int livroId, String novoEstado) {
        try (Connection conn = Database.getConnection()) {

            String check = "SELECT estado_admin FROM livros WHERE id=?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, livroId);
            ResultSet rsCheck = checkStmt.executeQuery();

            // Verifica se o livro está aprovado antes de permitir alteração de estado
            if (rsCheck.next()) {
                String estadoAtual = rsCheck.getString("estado_admin");
                if ("nao_aprovado".equalsIgnoreCase(estadoAtual)) {
                    System.out.println("❌ Não é possível alterar estado: livro não está aprovado.");
                    return false;
                }
            } else {
                System.out.println("❌ Livro não encontrado.");
                return false;
            }

            String sql = "UPDATE livros SET estado_operacional = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoEstado);
            stmt.setInt(2, livroId);
            System.out.println("ℹ️ [INFO] Estado do livro id=" + livroId + " alterado para '" + novoEstado + "'");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] alterarEstadoLivro: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Atualiza dados básicos do livro (usa COALESCE para manter valores existentes quando null)
    public boolean alterarDadosLivro(int livroId, String titulo, String autor, String categoria) {
        try (Connection conn = Database.getConnection()) {

            String sql =
                "UPDATE livros SET " +
                "titulo = COALESCE(?, titulo), " +
                "autor = COALESCE(?, autor), " +
                "categoria = COALESCE(?, categoria) " +
                "WHERE id=?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, categoria);
            stmt.setInt(4, livroId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("ℹ️  [INFO] Dados do livro id=" + livroId + " atualizados com sucesso.");
                return true;
            } else {
                System.out.println("⚠️ [AVISO] Tentativa de atualizar livro inexistente: id=" + livroId);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] alterarDadosLivro: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta o histórico de empréstimos de um livro
    public List<String> consultarHistoricoLivro(int livroId) {
        List<String> historico = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT e.id, u.nome, e.data_emprestimo, e.data_devolucao, e.estado " +
                         "FROM emprestimos e JOIN utilizadores u ON e.utilizador_id = u.id " +
                         "WHERE livro_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                historico.add(
                        "ID do Empréstimo: " + rs.getInt("id") +
                        " | Utilizador: " + rs.getString("nome") +
                        " | Estado: " + rs.getString("estado") +
                        " | Data: " + rs.getString("data_emprestimo") +
                        " | Devolvido: " + rs.getString("data_devolucao")
                );
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarHistoricoLivro: " + e.getMessage());
        }
        return historico;
    }

//==============================================================================================================================================
//============================================================== MÉTODOS ADICIONAIS ============================================================
//==============================================================================================================================================

    // Lista pedidos de devolução pendentes, devolvendo strings descritivas para o admin
    public List<String> listarDevolucoesPendentes() {
        List<String> pendentes = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT e.id AS emp_id, e.livro_id, l.titulo, u.nome, e.data_emprestimo " +
                         "FROM emprestimos e " +
                         "JOIN livros l ON e.livro_id = l.id " +
                         "JOIN utilizadores u ON e.utilizador_id = u.id " +
                         "WHERE e.estado = 'devolucao_pendente'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendentes.add(
                        "EmprestimoID: " + rs.getInt("emp_id") +
                        " | LivroID: " + rs.getInt("livro_id") +
                        " | Título: " + rs.getString("titulo") +
                        " | Utilizador: " + rs.getString("nome") +
                        " | Data empréstimo: " + rs.getString("data_emprestimo")
                );
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] listarDevolucoesPendentes: " + e.getMessage());
        }
        return pendentes;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Aprova uma devolução pendente: marca empréstimo como concluído e atualiza estado operacional do livro para disponível
    public boolean aprovarDevolucao(int emprestimoId) {
        try (Connection conn = Database.getConnection()) {
            String findSql = "SELECT id, livro_id FROM emprestimos WHERE id=? AND estado='devolucao_pendente'";
            PreparedStatement findStmt = conn.prepareStatement(findSql);
            findStmt.setInt(1, emprestimoId);
            ResultSet rs = findStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Nenhuma devolução pendente encontrada para o emprestimo id=" + emprestimoId);
                return false;
            }

            int livroId = rs.getInt("livro_id");

            String updEmp = "UPDATE emprestimos SET data_devolucao = CURRENT_DATE, estado = 'concluido' WHERE id = ?";
            PreparedStatement updEmpStmt = conn.prepareStatement(updEmp);
            updEmpStmt.setInt(1, emprestimoId);
            updEmpStmt.executeUpdate();

            String updLivro = "UPDATE livros SET estado_operacional = 'disponivel' WHERE id = ?";
            PreparedStatement updLivroStmt = conn.prepareStatement(updLivro);
            updLivroStmt.setInt(1, livroId);
            updLivroStmt.executeUpdate();

            System.out.println("✅ Devolução aprovada para livro id=" + livroId + " (emprestimo id=" + emprestimoId + ")");
            return true;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] aprovarDevolucao: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Nega uma devolução pendente: suspende o empréstimo, suspende o utilizador com motivo,
    // incrementa contador de suspensões e pode bloquear automaticamente.
    public boolean negarDevolucao(int emprestimoId, String motivo) {
        try (Connection conn = Database.getConnection()) {
            String findSql = "SELECT id, livro_id, utilizador_id FROM emprestimos WHERE id=? AND estado='devolucao_pendente'";
            PreparedStatement findStmt = conn.prepareStatement(findSql);
            findStmt.setInt(1, emprestimoId);
            ResultSet rs = findStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Nenhuma devolução pendente encontrada para o emprestimo id=" + emprestimoId);
                return false;
            }

            int livroId = rs.getInt("livro_id");
            int utilizadorId = rs.getInt("utilizador_id");

            String updEmp = "UPDATE emprestimos SET estado = 'suspenso', motivo_negacao = ? WHERE id = ?";
            PreparedStatement updEmpStmt = conn.prepareStatement(updEmp);
            updEmpStmt.setString(1, motivo);
            updEmpStmt.setInt(2, emprestimoId);
            updEmpStmt.executeUpdate();

            String updUser = "UPDATE utilizadores SET estado_operacional = 'suspenso', motivo_suspensao = ? WHERE id = ?";
            PreparedStatement updUserStmt = conn.prepareStatement(updUser);
            updUserStmt.setString(1, motivo);
            updUserStmt.setInt(2, utilizadorId);
            updUserStmt.executeUpdate();

            String inc = "UPDATE utilizadores SET suspensoes_count = COALESCE(suspensoes_count,0) + 1 WHERE id = ?";
            PreparedStatement incStmt = conn.prepareStatement(inc);
            incStmt.setInt(1, utilizadorId);
            incStmt.executeUpdate();

            String selCnt = "SELECT COALESCE(suspensoes_count,0) AS cnt FROM utilizadores WHERE id = ?";
            PreparedStatement selCntStmt = conn.prepareStatement(selCnt);
            selCntStmt.setInt(1, utilizadorId);
            ResultSet rsCnt = selCntStmt.executeQuery();
            if (rsCnt.next()) {
                int cnt = rsCnt.getInt("cnt");
                if (cnt >= 3) {
                    String blk = "UPDATE utilizadores SET estado_operacional = 'bloqueado', estado_admin = 'nao_aprovado' WHERE id = ?";
                    PreparedStatement blkStmt = conn.prepareStatement(blk);
                    blkStmt.setInt(1, utilizadorId);
                    blkStmt.executeUpdate();
                    System.out.println("⚠️ Utilizador id=" + utilizadorId + " automaticamente BLOQUEADO após " + cnt + " suspensões");
                }
            }

            String updLivro = "UPDATE livros SET estado_operacional = 'manutencao' WHERE id = ?";
            PreparedStatement updLivroStmt = conn.prepareStatement(updLivro);
            updLivroStmt.setInt(1, livroId);
            updLivroStmt.executeUpdate();

            System.out.println("ℹ️ [INFO] Devolução recusada para livro id=" + livroId + " (emprestimo id=" + emprestimoId + ") | Utilizador id=" + utilizadorId + " motivo: " + motivo);
            return true;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] negarDevolucao: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lista pedidos de remoção de suspensão dos utilizadores (estado operacional do utilizador é 'aguarda_suspensao')
    public List<String> listarPedidosRemocaoSuspensao() {
        List<String> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, nome, motivo_suspensao, justificacao_remocao FROM utilizadores WHERE estado_operacional = 'aguarda_suspensao'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(
                        "UtilizadorID: " + rs.getInt("id") +
                        " | Nome: " + rs.getString("nome") +
                        " | MotivoSuspensao: " + rs.getString("motivo_suspensao") +
                        " | Justificação: " + rs.getString("justificacao_remocao")
                );
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] listarPedidosRemocaoSuspensao: " + e.getMessage());
        }
        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Aprova um pedido de remoção de suspensão: restaura estado do utilizador e atualiza livros associados
    public boolean aprovarPedidoRemocao(int utilizadorId) {
        try (Connection conn = Database.getConnection()) {
            String check = "SELECT estado_operacional FROM utilizadores WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, utilizadorId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || !"aguarda_suspensao".equalsIgnoreCase(rs.getString("estado_operacional"))) {
                System.out.println("❌ Pedido não encontrado ou utilizador não suspenso id=" + utilizadorId);
                return false;
            }

            String updUser = "UPDATE utilizadores SET estado_operacional = 'ativo', estado_admin = 'aprovado', motivo_suspensao = NULL, justificacao_remocao = NULL WHERE id = ?";
            PreparedStatement updUserStmt = conn.prepareStatement(updUser);
            updUserStmt.setInt(1, utilizadorId);
            updUserStmt.executeUpdate();

            try {
                String updLivros = "UPDATE livros SET estado_operacional = 'disponivel', estado_admin = 'aprovado' " +
                                    "WHERE id IN (SELECT livro_id FROM emprestimos WHERE utilizador_id = ? AND estado = 'suspenso')";
                PreparedStatement updLivrosStmt = conn.prepareStatement(updLivros);
                updLivrosStmt.setInt(1, utilizadorId);
                int affected = updLivrosStmt.executeUpdate();
                if (affected > 0) {
                    System.out.println("✅ " + affected + " livro(s) atualizados para 'disponivel' após aprovação do pedido do utilizador id=" + utilizadorId);
                }
            } catch (SQLException ex) {
                System.out.println("🚨[ERRO] ao atualizar livros após aprovar pedido de remoção: " + ex.getMessage());
            }

            System.out.println("✅ Pedido aprovado para utilizador id=" + utilizadorId);
            return true;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] aprovarPedidoRemocao: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Nega um pedido de remoção de suspensão: bloqueia o utilizador e grava o motivo
    public boolean negarPedidoRemocao(int pedidoId, String motivo) {
        int utilizadorId = pedidoId;
        try (Connection conn = Database.getConnection()) {
            String check = "SELECT estado_operacional FROM utilizadores WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, utilizadorId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || !"aguarda_suspensao".equalsIgnoreCase(rs.getString("estado_operacional"))) {
                System.out.println("❌ Pedido não encontrado ou utilizador não em aguarda_suspensao id=" + utilizadorId);
                return false;
            }

            String updUser = "UPDATE utilizadores SET estado_operacional = 'bloqueado', estado_admin = 'nao_aprovado', motivo_suspensao = ?, justificacao_remocao = NULL WHERE id = ?";
            PreparedStatement updUserStmt = conn.prepareStatement(updUser);
            updUserStmt.setString(1, motivo);
            updUserStmt.setInt(2, utilizadorId);
            updUserStmt.executeUpdate();

            System.out.println("ℹ️ [INFO] Pedido rejeitado! utilizador id=" + utilizadorId + " Bloqueado Motivo: " + motivo);
            return true;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] negarPedidoRemocao: " + e.getMessage());
            return false;
        }
    }


//==============================================================================================================================================
//============================================================== MÉTODOS AUXILIARES ============================================================
//==============================================================================================================================================

    // Consulta o estado e metadados de um empréstimo por id
    public String consultarEstadoEmprestimo(int emprestimoId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, utilizador_id, livro_id, data_emprestimo, data_devolucao, estado FROM emprestimos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, emprestimoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "EmprestimoID: " + rs.getInt("id") +
                        " | LivroID: " + rs.getInt("livro_id") +
                        " | UtilizadorID: " + rs.getInt("utilizador_id") +
                        " | Estado: " + rs.getString("estado") +
                        " | Data emprestimo: " + rs.getString("data_emprestimo") +
                        " | Data devolucao: " + rs.getString("data_devolucao");
            } else {
                return "Empréstimo não encontrado";
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarEstadoDevolucao: " + e.getMessage());
            return "Erro ao consultar estado";
        }
    }

    // Consulta e devolve um objeto Utilizador por id
    // Serve para fornecer ao cliente admin o estado atualizado do utilizador logo após uma operação que o possa afetar
    public Utilizador consultarUtilizadorPorId(int utilizadorId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, nome, email, data_nascimento, estado_operacional, estado_admin, motivo_suspensao, justificacao_remocao, COALESCE(suspensoes_count,0) AS suspensoes_count FROM utilizadores WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, utilizadorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilizador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getDate("data_nascimento"),
                        rs.getString("estado_operacional"),
                        rs.getString("estado_admin"),
                        rs.getString("motivo_suspensao"),
                        rs.getString("justificacao_remocao"),
                        rs.getInt("suspensoes_count")
                );
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarUtilizadorPorId: " + e.getMessage());
        }
        return null;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Utilitário privado para executar updates simples com um parâmetro inteiro
    private boolean updateSimple(String sql, int id) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] updateSimple: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Utilitário privado para executar updates simples com um parâmetro string e um inteiro
    private boolean updateSimple(String sql, int id, String valor) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, valor);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] updateSimple: " + e.getMessage());
            return false;
        }
    }

}
