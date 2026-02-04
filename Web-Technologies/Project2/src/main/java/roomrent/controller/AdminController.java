package roomrent.controller;

// Entidades e services usados pelo controller
import roomrent.model.Ad; // Entidade do anúncio
import roomrent.service.AdService; // Lógica de negócio para anúncios
import roomrent.service.UserService; // Lógica/serviço para gestão de utilizadores

// Paginação e MVC
import org.springframework.data.domain.Page; // Representa página de resultados
import org.springframework.data.domain.Pageable; // Interface para pageable (pagination requests)
import org.springframework.stereotype.Controller; // Marca a classe como Controller Spring MVC
import org.springframework.ui.Model; // Model para enviar atributos para as views
import org.springframework.web.bind.annotation.*; // Mapear endpoints HTTP (@GetMapping, @PostMapping, etc.)

@Controller
@RequestMapping("/admin") // Mapeia todos os endpoints para /admin
public class AdminController {
    // Serviço para gerir utilizadores (aprovações, estados)
    private final UserService userService;
    
    // Serviço para gerir anúncios (state, delete, search)
    private final AdService adService;

    // Construtor: injeta serviços necessários ao controller
    public AdminController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    // Dashboard administrativo: lista anúncios com filtros e mostra utilizadores pendentes de aprovação
    @GetMapping
    public String dashboard(@RequestParam(name = "type", required = false) Ad.AdType type,
                            @RequestParam(name = "zone", required = false) String zone,
                            @RequestParam(name = "name", required = false) String name,
                            @RequestParam(name = "state", required = false) Ad.State state,
                            Model model) {
        model.addAttribute("pendingUsers", userService.pendingUsers());
        Page<Ad> ads = adService.adminSearch(type, zone, name, state, Pageable.unpaged());
        model.addAttribute("ads", ads);
        model.addAttribute("type", type);
        model.addAttribute("zone", zone);
        model.addAttribute("name", name);
        model.addAttribute("state", state);
        model.addAttribute("contentTemplate", "admin/dashboard");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Aprova um utilizador pendente (muda o estado de 'pending' para 'active')
    @PostMapping("/users/{id}/approve")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/admin"; // Redireciona de volta ao dashboard administrativo
    }

    // Ativa um anúncio (torna-o visível)
    @PostMapping("/ads/{id}/activate")
    public String activateAd(@PathVariable Long id) {
        adService.setState(id, Ad.State.ATIVO);
        return "redirect:/"; // Redireciona para a página inicial após ativar o anúncio
    }

    // Desativa um anúncio (oculta-o do público)
    @PostMapping("/ads/{id}/deactivate")
    public String deactivateAd(@PathVariable Long id) {
        adService.setState(id, Ad.State.INATIVO);
        return "redirect:/";
    }

    // Apaga um anúncio permanentemente (remove também dados relacionados no service)
    @PostMapping("/ads/{id}/delete")
    public String deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return "redirect:/admin";
    }
}
