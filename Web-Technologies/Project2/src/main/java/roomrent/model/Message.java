package roomrent.model;

// JPA: anotações ORM (Entity, ManyToOne, Column, etc.)
import jakarta.persistence.*;
// Bean Validation: restrições de campo (NotBlank, Size)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Representa um instante no tempo
import java.time.Instant;
 
// Entidade JPA que representa uma mensagem entre utilizadores
@Entity
@Table(name = "messages")
public class Message {
    // Chave primária (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com o anúncio (muitas mensagens associadas a um mesmo anúncio)
    @ManyToOne(optional = false)
    private Ad ad;

    // Utilizador que enviou a mensagem
    @ManyToOne(optional = false)
    private User fromUser;

    // Utilizador destinatário da mensagem
    @ManyToOne(optional = false)
    private User toUser;

    // Texto da mensagem (até 2000 caracteres)
    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String content;

    // Data/hora de criação (definida automaticamente no momento da criação)
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
