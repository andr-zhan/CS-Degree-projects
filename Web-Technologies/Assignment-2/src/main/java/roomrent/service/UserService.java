package roomrent.service;

import roomrent.model.Role; // Entidade Role
import roomrent.model.User; // Entidade User
import roomrent.repository.RoleRepository; // Repositório de roles
import roomrent.repository.UserRepository; // Repositório de utilizadores
import jakarta.transaction.Transactional; // Anotação para métodos transacionais
import org.springframework.security.crypto.password.PasswordEncoder; // Codificador de passwords
import org.springframework.stereotype.Service; // Marca a classe como Service Spring

import java.util.List; // Lista de utilizadores

/* 
 * Serviço responsável por operações relacionadas a utilizadores,
 * registo, aprovação e listagem de utilizadores pendentes.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Construtor: injeta dependências dos repositórios e codificador de passwords
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Regista um novo utilizador com username, password e email
    @Transactional
    public User register(String username, String rawPassword, String email) {
        // Verificar se username já existe
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Erro: Username já em uso");
        }
        // Verificar se email já existe
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Erro: Email já registado");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword)); // encripta a password
        u.setEmail(email);
        // Atribui a role ROLE_USER por defeito
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roleRepository.save(r);
        });
        u.getRoles().add(userRole);
        return userRepository.save(u);
    }

    // Aprova um utilizador pendente (usado pelo admin)
    @Transactional
    public void approveUser(Long userId) {
        User u = userRepository.findById(userId).orElseThrow();
        u.setEnabled(true);
        userRepository.save(u);
    }

    // Obtém a lista de utilizadores pendentes (não aprovados)
    public List<User> pendingUsers() {
        return userRepository.findAll().stream().filter(u -> !u.isEnabled()).toList();
    }
}
