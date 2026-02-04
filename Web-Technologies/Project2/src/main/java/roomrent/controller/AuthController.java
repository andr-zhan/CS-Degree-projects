package roomrent.controller;

// Serviço usado para registo e gestão de utilizadores
import roomrent.service.UserService;

// Anotações de validação para os campos do formulário (Jakarta Validation)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// MVC / Spring Web
import org.springframework.stereotype.Controller; // Marca a classe como controller MVC
import org.springframework.ui.Model; // Model para passar atributos às views
import org.springframework.validation.annotation.Validated; // Habilita validação em métodos com constraints
import org.springframework.web.bind.annotation.GetMapping; // Mapear GET
import org.springframework.web.bind.annotation.ModelAttribute; // Bind de form models
import org.springframework.web.bind.annotation.PostMapping; // Mapear POST

@Controller
@Validated
public class AuthController {

    // Formulário de registo com validações (username, password, email)
    private record RegisterForm(@NotBlank @Size(min=3,max=50) String username,
                                @NotBlank @Size(min=8,max=255) String password,
                                @Email @NotBlank String email) {}

    // Serviço para registar/utilizadores
    private final UserService userService;

    // Injeta UserService via construtor
    public AuthController(UserService userService) { this.userService = userService; }

    // Mostra a página de login
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("contentTemplate", "login");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Mostra formulário de registo com um RegisterForm vazio para binding
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm("", "", ""));
        model.addAttribute("contentTemplate", "register");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Processa submissão do formulário de registo
    // Tenta criar o utilizador; em caso de erro (IllegalArgumentException) retorna o form com a mensagem
    @PostMapping("/register")
    public String doRegister(@ModelAttribute RegisterForm form, Model model) {
        try {
            userService.register(form.username(), form.password(), form.email());
            model.addAttribute("message", "Registo efetuado. Aguarde aprovação do administrador.");
            model.addAttribute("contentTemplate", "login"); // Após registo com sucesso, mostra login
            model.addAttribute("showFooter", false);
            return "layout";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerForm", form);
            model.addAttribute("contentTemplate", "register");
            model.addAttribute("showFooter", false);
            return "layout";
        }
    }
}
