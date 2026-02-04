package server;

import java.io.Serializable;

public class Livro implements Serializable {

    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private String estadoOperacional;
    private String estadoAdmin;

    /*
     * Representa um livro no sistema. Campos incluem metadados e estados
     * (operacional: aguarda_aprovacao/disponivel/emprestado/manutencao, admin: aprovado/nao_aprovado).
     */
    public Livro(int id, String titulo, String autor, String categoria,
                 String estadoOperacional, String estadoAdmin) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.estadoOperacional = estadoOperacional;
        this.estadoAdmin = estadoAdmin;
    }

    // Getters para leitura dos campos do livro
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getCategoria() { return categoria; }
    public String getEstadoOperacional() { return estadoOperacional; }
    public String getEstadoAdmin() { return estadoAdmin; }

    // ToString: representação textual do livro
    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", categoria='" + categoria + '\'' +
                ", estadoOperacional='" + estadoOperacional + '\'' +
                ", estadoAdmin='" + estadoAdmin + '\'' +
                '}';
    }
}
