package roomrent.security;

import roomrent.service.UserDetailsServiceImpl; // Serviço que carrega UserDetails para autenticação
import org.springframework.context.annotation.Bean; // Anotação para declarar beans no contexto Spring
import org.springframework.context.annotation.Configuration; // Marca a configuração de segurança
import org.springframework.http.HttpMethod; // Constantes para métodos HTTP
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Provider que usa UserDetailsService + PasswordEncoder
import org.springframework.security.config.Customizer; // Utilitário para configurar defaults de forma concisa
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Builder para configurar regras HTTP/Security
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementação de PasswordEncoder (BCrypt)
import org.springframework.security.crypto.password.PasswordEncoder; // Interface para codificação de passwords
import org.springframework.security.web.SecurityFilterChain; // Configuração da cadeia de filtros do Spring Security

@Configuration
public class SecurityConfig {

    // Bean: codificador de passwords usando BCrypt (custo 12)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // Bean: Provider de autenticação que delega em UserDetailsService e usa o PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    // Configuração principal da segurança web (filtros, CSRF, autorização, login/logout)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Ignorar proteção CSRF em endpoints /api/** (útil para chamadas REST/JSX)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                    // Recursos estáticos e páginas públicas
                    .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/login", "/register", "/ads", "/ads/**").permitAll()
                    // Mensagens exigem autenticação
                    .requestMatchers("/messages/**").authenticated()
                    // Área administrativa apenas para utilizadores com ROLE_ADMIN
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // Qualquer outra requisição exige autenticação
                    .anyRequest().authenticated()
                )
                // Configuração do formulário de login
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/", true)
                )
                // Configuração do logout
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
                // Remember-me com configuração padrão
                .rememberMe(Customizer.withDefaults());
        return http.build();
    }
}
