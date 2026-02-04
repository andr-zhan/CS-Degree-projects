package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

public interface BibliotecaService extends Remote {
    /*
     * Interface remota (RMI) que descreve as operações expostas pelo serviço de biblioteca
     * usadas pelo ClienteGeral. Cada método lança RemoteException.
     */

    // Utilizador
    boolean registarUtilizador(String nome, String email, Date dataNascimento) throws RemoteException;
    boolean apagarUtilizadorSeSemEmprestimos(int utilizadorId) throws RemoteException;
    String consultarEstadoUtilizador(int utilizadorId) throws RemoteException;
  
    // Livros
    boolean registarLivro(String titulo, String autor, String categoria) throws RemoteException;
    List<Livro> listarLivrosDisponiveis(String categoriaFiltro, String autorFiltro) throws RemoteException;
    String consultarEstadoLivro(int livroId) throws RemoteException;
    List<String> consultarHistoricoLivro(int livroId) throws RemoteException;

    // Empréstimos
    boolean realizarEmprestimo(int utilizadorId, int livroId) throws RemoteException;
    List<Emprestimo> listarEmprestimosAtivosPorUtilizador(int utilizadorId) throws RemoteException;
    String consultarEstadoEmprestimo(int emprestimoId) throws RemoteException;
    
    // Adicionais
    List<Livro> sugerirLivrosPorTitulo(String titulo) throws RemoteException;
    boolean realizarDevolucao(int livroId) throws RemoteException;
    boolean pedirRemoverSuspensao(int utilizadorId, String justificacao) throws RemoteException;

    // Auxiliares
    Emprestimo consultarEmprestimoAtivoPorLivro(int livroId) throws RemoteException;
    Utilizador consultarUtilizadorPorId(int utilizadorId) throws RemoteException;
}
