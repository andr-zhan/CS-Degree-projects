package roomrent.model;

// JPA: anotações ORM (Entity, Table, Id, ManyToMany, JoinTable, etc.)
import jakarta.persistence.*;
// Validação de bean: restrições de campo (Email, NotBlank, Size)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Representa um instante no tempo
import java.time.Instant;
// Coleções usadas para associar roles ao utilizador
import java.util.HashSet;
import java.util.Set;

// Entidade JPA que representa um utilizador do sistema
@Entity
@Table(name = "users")
public class User {
    // Chave primária (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome de utilizador único (usado para login)
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;

    // Password encriptada
    @NotBlank
    @Size(min = 8, max = 255)
    @Column(nullable = false)
    private String password;

    // Email de contacto (único)
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    // Conta activada (aprovada pelo admin)
    @Column(nullable = false)
    private boolean enabled = false;

    // Data de registo (definida automaticamente no momento da criação)
    @Column(nullable = false, updatable = false)
    private Instant registeredAt = Instant.now();

    /*
     * Roles atribuídas ao utilizador (autoridades)
     * Carregamento imediato das roles ao carregar o user com FetchType.EAGER (Spring Security precisa das roles para autenticação)
     * Associação ManyToMany com tabela intermédia user_roles
    */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>(); // Colecção de roles atribuídas ao utilizador

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
