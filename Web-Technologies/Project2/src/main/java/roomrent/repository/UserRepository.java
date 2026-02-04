package roomrent.repository;

import roomrent.model.User; // Entidade User
import org.springframework.data.jpa.repository.JpaRepository; // Interface base Spring Data JPA

import java.util.Optional; // Para retornos que podem ser vazios

// Repositório para operações de leitura/escrita sobre utilizadores
public interface UserRepository extends JpaRepository<User, Long> {

    // Procura utilizador por username (único)
    Optional<User> findByUsername(String username);

    // Procura utilizador por email (único)
    Optional<User> findByEmail(String email);
}
