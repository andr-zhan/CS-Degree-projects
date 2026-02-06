package roomrent.service;

import roomrent.model.User; // Entidade User
import roomrent.repository.UserRepository; // Repositório de utilizadores
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Representa uma Authority
import org.springframework.security.core.userdetails.UserDetails; // Interface do utilizador autenticado
import org.springframework.security.core.userdetails.UserDetailsService; // Serviço para carregar detalhes do utilizador
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção quando utilizador não é encontrado
import org.springframework.stereotype.Service; // Marca a classe como Service Spring

import java.util.List; // Lista para armazenar authorities

// Serviço que carrega detalhes do utilizador para autenticação e autorização
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Construtor: injeta dependência do repositório de utilizadores
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Mapeia roles do utilizador para SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = u.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getName()))
            .toList();
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), u.isEnabled(), true, true, true, authorities
        );
    }
}
