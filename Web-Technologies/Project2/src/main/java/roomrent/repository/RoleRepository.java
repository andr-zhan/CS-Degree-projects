package roomrent.repository;

import roomrent.model.Role; // Entidade Role (autoridade)
import org.springframework.data.jpa.repository.JpaRepository; // Interface base do Spring Data JPA

import java.util.Optional; // Para retorno opcional quando a role pode não existir

// Repositório para gerir roles/authorities do sistema
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Procura uma role pelo nome (ex.: ROLE_USER, ROLE_ADMIN)
    Optional<Role> findByName(String name);
}
