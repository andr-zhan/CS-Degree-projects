package roomrent.model;

// JPA: anotações para mapeamento ORM (Entity, Id, Column, etc.)
import jakarta.persistence.*;

// Entidade JPA que representa um papel/autoridade de utilizador
@Entity
public class Role {
    // Chave primária (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da autoridade (único)
    @Column(nullable = false, unique = true)
    private String name; // e.g., ROLE_USER, ROLE_ADMIN

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
