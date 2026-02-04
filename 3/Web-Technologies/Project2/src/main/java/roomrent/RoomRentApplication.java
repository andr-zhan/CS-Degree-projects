package roomrent;

import org.springframework.boot.SpringApplication; // Classe para iniciar a aplicação Spring
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotação que marca a classe como app Spring Boot
import org.springframework.boot.CommandLineRunner; // Interface para executar lógica no arranque da aplicação
import org.springframework.context.annotation.Bean; // Anotação para declarar um bean
import roomrent.model.Role; // Entidade Role
import roomrent.model.User; // Entidade User
import roomrent.repository.RoleRepository; // Acesso a roles no BD
import roomrent.repository.UserRepository; // Acesso a users no BD
import org.springframework.security.crypto.password.PasswordEncoder; // Codificador de passwords

@SpringBootApplication
public class RoomRentApplication {
    // Método de entrada: inicia a aplicação Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(RoomRentApplication.class, args);
    }

    // Bean CommandLineRunner: inicializa dados padrão (roles e user admin) no arranque
    @Bean
    CommandLineRunner seedDefaults(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            // Cria ou obtém a role ROLE_ADMIN
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role(); r.setName("ROLE_ADMIN"); return roleRepo.save(r);
            });
            // Cria ou obtém a role ROLE_USER
            Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> {
                Role r = new Role(); r.setName("ROLE_USER"); return roleRepo.save(r);
            });
            // Cria ou obtém o utilizador admin com password encriptada
            userRepo.findByUsername("admin").orElseGet(() -> {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin123"));
                u.setEmail("admin@example.com");
                u.setEnabled(true);
                u.getRoles().add(adminRole);
                return userRepo.save(u);
            });
        };
    }
}
