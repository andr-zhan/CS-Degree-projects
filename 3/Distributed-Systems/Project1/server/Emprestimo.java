package server;

import java.io.Serializable;
import java.sql.Date;

public class Emprestimo implements Serializable {

    private int id;
    private int utilizadorId;
    private int livroId;
    private Date dataEmprestimo;
    private Date dataDevolucao;
    private String estado;
    private String motivoNegacao;

    /*
     * Representa um empréstimo de livro. É serializável para ser usado via RMI
     * ou enviado entre processos (por exemplo, para logs ou debugging).
     */
    public Emprestimo(int id, int utilizadorId, int livroId,
                      Date dataEmprestimo, Date dataDevolucao, String estado) {
        this.id = id;
        this.utilizadorId = utilizadorId;
        this.livroId = livroId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.estado = estado;
        this.motivoNegacao = null; 
    }

    // Getters: fornecem acesso imutável aos campos do empréstimo
    public int getId() { return id; }
    public int getUtilizadorId() { return utilizadorId; }
    public int getLivroId() { return livroId; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public Date getDataDevolucao() { return dataDevolucao; }
    public String getEstado() { return estado; }
    public String getMotivoNegacao() { return motivoNegacao; }

    // ToString: representação textual do empréstimo
    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", utilizadorId=" + utilizadorId +
                ", livroId=" + livroId +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + dataDevolucao +
                ", estado='" + estado + '\'' +
                (motivoNegacao != null ? ", motivoNegacao='" + motivoNegacao + '\'' : "") +
                '}';
    }
}
