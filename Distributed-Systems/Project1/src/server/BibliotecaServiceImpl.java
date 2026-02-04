package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaServiceImpl extends UnicastRemoteObject implements BibliotecaService {

    /*
     * Implementação RMI do serviço da biblioteca.
     * Este serviço é usado pelo ClienteGeral através de RMI.
     */

    public BibliotecaServiceImpl() throws RemoteException {
        super();
    }

//==============================================================================================================================================
//============================================================= Métodos Utilizadores ===========================================================
//==============================================================================================================================================

    /*
     * Regista um novo utilizador.
     *  - Nome e email são obrigatórios (validação local)
     *  - Email tem de ser único (validação na BD)
     *  - Estados iniciais: estado_operacional='aguarda_aprovacao', estado_admin='nao_aprovado'
     */
    @Override
    public boolean registarUtilizador(String nome, String email, java.sql.Date dataNascimento) throws RemoteException {

        // Verificação dos parâmetros obrigatórios antes de inserir na BD
        if (nome == null || nome.isBlank() || email == null || email.isBlank()) {
            System.out.println("🚨[ERRO] Nome e email são obrigatórios.");
            return false;
        }

        try (Connection conn = Database.getConnection()) {

            // Verificar se o email já existe na base de dados
            String checkSql = "SELECT COUNT(*) FROM utilizadores WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Lança RemoteException com mensagem específica para o cliente
                throw new RemoteException("Email já registado");
            }

            String sql = "INSERT INTO utilizadores (nome, email, data_nascimento, estado_operacional, estado_admin) " +
                        "VALUES (?, ?, ?, 'aguarda_aprovacao', 'nao_aprovado')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            if (dataNascimento != null) stmt.setDate(3, dataNascimento);
            else stmt.setNull(3, java.sql.Types.DATE);

            System.out.println("ℹ️ [INFO] Novo utilizador registado: " + nome + " (" + email + ")");
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] registarUtilizador: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Apaga um utilizador caso não existam quaisquer empréstimos ativos associados
    @Override
    public boolean apagarUtilizadorSeSemEmprestimos(int utilizadorId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {

            // Verifica se existem empréstimos bloqueantes
            String checkSql =
                "SELECT COUNT(*) FROM emprestimos " +
                "WHERE utilizador_id = ? AND estado IN ('ativo', 'devolucao_pendente')";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, utilizadorId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Tem empréstimos ativos/pendentes, não pode apagar
                        return false;
                    }
                }
            }

            // Apagar empréstimos históricos associados primeiro (devido à chave estrangeira)
            String delEmpSql = "DELETE FROM emprestimos WHERE utilizador_id = ?";
            try (PreparedStatement delEmpStmt = conn.prepareStatement(delEmpSql)) {
                delEmpStmt.setInt(1, utilizadorId);
                delEmpStmt.executeUpdate(); // mesmo que 0, é OK
            }

            // Apagar o utilizador
            String delUserSql = "DELETE FROM utilizadores WHERE id = ?";
            try (PreparedStatement delUserStmt = conn.prepareStatement(delUserSql)) {
                delUserStmt.setInt(1, utilizadorId);
                int affected = delUserStmt.executeUpdate();
                return affected > 0;
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] apagarUtilizadorSeSemEmprestimos: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta o estado operacional de um utilizador
    @Override
    public String consultarEstadoUtilizador(int utilizadorId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT estado_operacional FROM utilizadores WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, utilizadorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("estado_operacional");
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarEstadoUtilizador: " + e.getMessage());
        }
        return "Utilizador não encontrado";
    }

//==============================================================================================================================================
//================================================================ MÉTODOS Livros ==============================================================
//==============================================================================================================================================

    /*
     * Regista um novo livro.
     *  - Título e autor obrigatórios.
     *  - Estados iniciais: estado_operacional='aguarda_aprovacao', estado_admin='nao_aprovado'.
     *  - Categoria é opcional.
     */
    @Override
    public boolean registarLivro(String titulo, String autor, String categoria) throws RemoteException {

        // Título e autor são obrigatórios
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank()) {
            System.out.println("🚨[ERRO] Título e autor são obrigatórios.");
            return false;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO livros (titulo, autor, categoria, estado_operacional, estado_admin) " +
                         "VALUES (?, ?, ?, 'aguarda_aprovacao', 'nao_aprovado')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, categoria);
            System.out.println("[INFO] Novo livro registado: " + titulo + " de autoria de " + autor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] registarLivro: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Lista livros disponíveis e aprovados.
     * Filtros opcionais de categoria e autor (ILIKE para busca case-insensitive contendo substring).
     */
    @Override
    public List<Livro> listarLivrosDisponiveis(String categoriaFiltro, String autorFiltro) throws RemoteException {
        List<Livro> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM livros WHERE estado_operacional='disponivel' AND estado_admin='aprovado'";
            if (categoriaFiltro != null && !categoriaFiltro.isBlank()) sql += " AND categoria ILIKE '%" + categoriaFiltro + "%'";
            if (autorFiltro != null && !autorFiltro.isBlank()) sql += " AND autor ILIKE '%" + autorFiltro + "%'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

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
            System.out.println("🚨[ERRO] listarLivrosDisponiveis: " + e.getMessage());
        }
        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Consulta o estado operacional de um livro
    @Override
    public String consultarEstadoLivro(int livroId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT estado_operacional FROM livros WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("estado_operacional");
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarEstadoLivro: " + e.getMessage());
        }
        return "Livro não encontrado";
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Constrói uma lista textual com o histórico de empréstimos de um livro.
     * Cada entrada inclui id do empréstimo, nome do utilizador, estado, data empréstimo e data devolução.
     */
    @Override
    public List<String> consultarHistoricoLivro(int livroId) throws RemoteException {

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
//============================================================== MÉTODOS Empréstimos ===========================================================
//==============================================================================================================================================

    // Realiza um novo empréstimo para o par (utilizador, livro)
    @Override
    public boolean realizarEmprestimo(int utilizadorId, int livroId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {

            // 1) Verificar se o utilizador está aprovado e ativo
            String checkUser = "SELECT COUNT(*) FROM utilizadores " +
                            "WHERE id=? AND estado_admin='aprovado' AND estado_operacional='ativo'";
            PreparedStatement checkU = conn.prepareStatement(checkUser);
            checkU.setInt(1, utilizadorId);
            ResultSet rsU = checkU.executeQuery();
            rsU.next();
            if (rsU.getInt(1) == 0) {
                System.out.println("❌ Utilizador não aprovado ou inativo!");
                return false;
            }

            // 2) Verificar se o livro está aprovado e disponível
            String checkBook = "SELECT COUNT(*) FROM livros " +
                            "WHERE id=? AND estado_admin='aprovado' AND estado_operacional='disponivel'";
            PreparedStatement checkL = conn.prepareStatement(checkBook);
            checkL.setInt(1, livroId);
            ResultSet rsL = checkL.executeQuery();
            rsL.next();
            if (rsL.getInt(1) == 0) {
                System.out.println("❌ Livro não disponível ou não aprovado!");
                return false;
            }

            // 3) Realiza o empréstimo (inserção na tabela emprestimos)
            String sql = "INSERT INTO emprestimos (utilizador_id, livro_id, data_emprestimo, estado) " +
                        "VALUES (?, ?, CURRENT_DATE, 'ativo')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, utilizadorId);
            stmt.setInt(2, livroId);
            stmt.executeUpdate();

            // 4) Atualiza o estado do livro para 'emprestado'
            String sql2 = "UPDATE livros SET estado_operacional='emprestado' WHERE id=?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, livroId);
            stmt2.executeUpdate();

            System.out.println("ℹ️ [INFO] Empréstimo realizado: UtilizadorID=" + utilizadorId + ", LivroID=" + livroId);
            return true;

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] realizarEmprestimo: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Lista todos os empréstimos ativos (estado operacional 'ativo' ou 'devolucao_pendente') para um utilizador
    @Override
    public List<Emprestimo> listarEmprestimosAtivosPorUtilizador(int utilizadorId) throws RemoteException {

        List<Emprestimo> lista = new ArrayList<>();

        String sql =
            "SELECT id, utilizador_id, livro_id, data_emprestimo, data_devolucao, estado " +
            "FROM emprestimos " +
            "WHERE utilizador_id = ? AND (estado = 'ativo' OR estado = 'devolucao_pendente')";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilizadorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Emprestimo(
                            rs.getInt("id"),
                            rs.getInt("utilizador_id"),
                            rs.getInt("livro_id"),
                            rs.getDate("data_emprestimo"),
                            rs.getDate("data_devolucao"),
                            rs.getString("estado")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] listarEmprestimosAtivosPorUtilizador: " + e.getMessage());
        }

        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Retorna a representação textual de um empréstimo específico, serve para consulta de estado
    @Override
    public String consultarEstadoEmprestimo(int emprestimoId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, utilizador_id, livro_id, data_emprestimo, data_devolucao, estado, motivo_negacao FROM emprestimos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, emprestimoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "EmprestimoID: " + rs.getInt("id") +
                        " | LivroID: " + rs.getInt("livro_id") +
                        " | UtilizadorID: " + rs.getInt("utilizador_id") +
                        " | Estado: " + rs.getString("estado") +
                        " | Data emprestimo: " + rs.getString("data_emprestimo") +
                        " | Data devolucao: " + rs.getString("data_devolucao") +
                        " | Motivo negacao: " + rs.getString("motivo_negacao");
            } else {
                return "❌ Empréstimo id=" + emprestimoId + " não encontrado.";
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarEstadoDevolucao: " + e.getMessage());
            return "Erro ao consultar estado";
        }
    }

//==============================================================================================================================================
//============================================================== MÉTODOS Adicionais ============================================================
//==============================================================================================================================================

    /*
     * Sugere livros com base em um título fornecido:
     *  1. Localiza um livro para obter autor e categoria.
     *  2. Pesquisa outros livros aprovados e disponíveis que partilhem autor OU categoria.
     */
    @Override
    public List<Livro> sugerirLivrosPorTitulo(String titulo) throws RemoteException {

        List<Livro> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            // Primeiro localiza o livro pelo título (utilizamos ILIKE para corresponder sem case)
            String findSql = "SELECT id, titulo, autor, categoria FROM livros WHERE titulo ILIKE ? LIMIT 1";
            PreparedStatement findStmt = conn.prepareStatement(findSql);
            findStmt.setString(1, titulo);
            ResultSet rs = findStmt.executeQuery();
            if (!rs.next()) {
                return lista; // devolve vazio se livro não encontrado
            }

            int livroId = rs.getInt("id");
            String autor = rs.getString("autor");
            String categoria = rs.getString("categoria");

            // Agora seleciona outros livros aprovados e disponíveis com mesmo autor ou categoria
            String sugSql = "SELECT * FROM livros WHERE id<>? AND estado_admin='aprovado' AND estado_operacional='disponivel' " +
                            "AND (autor ILIKE ? OR categoria ILIKE ?)";
            PreparedStatement sugStmt = conn.prepareStatement(sugSql);
            sugStmt.setInt(1, livroId);
            sugStmt.setString(2, autor == null ? "" : "%" + autor + "%");
            sugStmt.setString(3, categoria == null ? "" : "%" + categoria + "%");

            ResultSet srs = sugStmt.executeQuery();
            while (srs.next()) {
                lista.add(new Livro(
                        srs.getInt("id"),
                        srs.getString("titulo"),
                        srs.getString("autor"),
                        srs.getString("categoria"),
                        srs.getString("estado_operacional"),
                        srs.getString("estado_admin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("🚨[ERRO] sugerirLivrosPorTitulo: " + e.getMessage());
        }
        return lista;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Regista pedido de devolução de um livro (altera estado do empréstimo para 'devolucao_pendente')
    @Override
    public boolean realizarDevolucao(int livroId) throws RemoteException {

        try (Connection conn = Database.getConnection()) {
            // Localiza o empréstimo ativo para o livro
            String findSql = "SELECT id FROM emprestimos WHERE livro_id=? AND estado='ativo' ORDER BY data_emprestimo DESC LIMIT 1";
            PreparedStatement findStmt = conn.prepareStatement(findSql);
            findStmt.setInt(1, livroId);
            ResultSet rs = findStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Nenhum empréstimo activo encontrado para o livro id=" + livroId);
                return false;
            }

            int emprestimoId = rs.getInt("id");

            // Marca o empréstimo como pedido de devolução pendente (aguarda aprovação do admin)
            String updEmp = "UPDATE emprestimos SET estado = 'devolucao_pendente' WHERE id = ?";
            PreparedStatement updEmpStmt = conn.prepareStatement(updEmp);
            updEmpStmt.setInt(1, emprestimoId);
            updEmpStmt.executeUpdate();

            // Marca o livro como manutencao para evitar novo empréstimo enquanto devolução pendente
            String updLivro = "UPDATE livros SET estado_operacional = 'manutencao' WHERE id = ?";
            PreparedStatement updLivroStmt = conn.prepareStatement(updLivro);
            updLivroStmt.setInt(1, livroId);
            updLivroStmt.executeUpdate();

            System.out.println("ℹ️ [INFO] Pedido de devolução registado para livro id=" + livroId + " (emprestimo id=" + emprestimoId + ")");
            return true;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] realizarDevolucao: " + e.getMessage());
            return false;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------

    // Regista pedido de remoção de suspensão por parte do utilizador
    @Override
    public boolean pedirRemoverSuspensao(int utilizadorId, String justificacao) throws RemoteException {

        try (Connection conn = Database.getConnection()) {
            // Só permite pedido se utilizador existir e estiver suspenso
            String checkSql = "SELECT estado_operacional FROM utilizadores WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, utilizadorId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) return false;
            String estado = rs.getString("estado_operacional");
            if (!"suspenso".equalsIgnoreCase(estado)) {
                System.out.println("⚠️ Pedido apenas permitido para utilizadores suspensos.");
                return false;
            }

            // Atualiza estado_operacional para 'aguarda_suspensao' e grava justificacao_remocao
            String upd = "UPDATE utilizadores SET estado_operacional = 'aguarda_suspensao', justificacao_remocao = ? WHERE id = ?";
            PreparedStatement updStmt = conn.prepareStatement(upd);
            updStmt.setString(1, justificacao);
            updStmt.setInt(2, utilizadorId);
            System.out.println("ℹ️ [INFO] Pedido de remoção de suspensão registado para utilizador id=" + utilizadorId);
            return updStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] pedirRemoverSuspensao: " + e.getMessage());
            return false;
        }
    }

//==============================================================================================================================================
//============================================================== MÉTODOS AUXILIARES ============================================================
//==============================================================================================================================================

    /*
     * Método auxiliar para obter o id do utilizador do empréstimo ativo de um livro.
     * Serve para
     *  - Iniciar processo de devolução por livro (sem conhecer o id do empréstimo).
     *  - Mostrar na UI o utilizador atual que detém o livro.
     *  - Obter utilizadorId a partir de livroId para lógicas administrativas.
     */
    @Override
    public Emprestimo consultarEmprestimoAtivoPorLivro(int livroId) throws RemoteException {

        String sql = "SELECT id, utilizador_id, livro_id, data_emprestimo, data_devolucao, estado, motivo_negacao " +
                     "FROM emprestimos WHERE livro_id = ? AND estado = 'ativo' ORDER BY data_emprestimo DESC LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Emprestimo(
                            rs.getInt("id"),
                            rs.getInt("utilizador_id"),
                            rs.getInt("livro_id"),
                            rs.getDate("data_emprestimo"),
                            rs.getDate("data_devolucao"),
                            rs.getString("estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("🚨[ERRO] consultarEmprestimoAtivoPorLivro: " + e.getMessage());
        }
        return null;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------
   
    // Consulta e devolve um objeto Utilizador por ID
    // Serve para fornecer ao cliente geral o estado atualizado do utilizador logo após uma operação que o possa afetar
    @Override
    public Utilizador consultarUtilizadorPorId(int utilizadorId) throws RemoteException {

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

}


