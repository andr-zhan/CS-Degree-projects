package server;

import java.io.Serializable;
import java.sql.Date;

public class Utilizador implements Serializable {

    private int id;
    private String nome;
    private String email;
    private Date dataNascimento;
    private String estadoOperacional;
    private String estadoAdmin;
    private String motivoSuspensao;
    private String justificacaoRemocao;
    private int suspensoesCount;

    /*
     * Representa um utilizador com todos os campos da base de dados. É serializável para
     * ser usado via RMI ou enviado entre processos.
     */
    public Utilizador(int id, String nome, String email, Date dataNascimento,
                      String estadoOperacional, String estadoAdmin, String motivoSuspensao,
                      String justificacaoRemocao, int suspensoesCount) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.estadoOperacional = estadoOperacional;
        this.estadoAdmin = estadoAdmin;
        this.motivoSuspensao = motivoSuspensao;
        this.justificacaoRemocao = justificacaoRemocao;
        this.suspensoesCount = suspensoesCount;
    }

    // Getters para acesso imutável aos campos do utilizador
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Date getDataNascimento() { return dataNascimento; }
    public String getEstadoOperacional() { return estadoOperacional; }
    public String getEstadoAdmin() { return estadoAdmin; }
    public String getMotivoSuspensao() { return motivoSuspensao; }
    public String getJustificacaoRemocao() { return justificacaoRemocao; }
    public int getSuspensoesCount() { return suspensoesCount; }

    // ToString: representação textual do utilizador
    @Override
    public String toString() {
        return "Utilizador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", estadoOperacional='" + estadoOperacional + '\'' +
                ", estadoAdmin='" + estadoAdmin + '\'' +
                ", motivoSuspensao='" + motivoSuspensao + '\'' +
                ", justificacaoRemocao='" + justificacaoRemocao + '\'' +
                ", suspensoesCount=" + suspensoesCount +
                '}';
    }
}
