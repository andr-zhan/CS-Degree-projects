package roomrent.controller;

// Entidade e serviço utilizados na página inicial
import roomrent.model.Ad; // Entidade do anúncio
import roomrent.service.AdService; // Serviço para obter anúncios (latestByType, search)
// MVC / Spring Web
import org.springframework.stereotype.Controller; // Marca a classe como Controller
import org.springframework.ui.Model; // Model para enviar atributos à view
import org.springframework.web.bind.annotation.GetMapping; // Mapear GET

@Controller
public class HomeController {
    // Serviço para obter anúncios a mostrar na página inicial
    private final AdService adService;

    // Construtor: injeta AdService
    public HomeController(AdService adService) { this.adService = adService; }

    @GetMapping("/")
    public String home(Model model) {
        // Carrega as últimas OFERTAS e PROCURAS (3 de cada) para mostrar na página inicial
        model.addAttribute("latestOffers", adService.latestByType(Ad.AdType.OFERTA, 3));
        model.addAttribute("latestSearches", adService.latestByType(Ad.AdType.PROCURA, 3));
        model.addAttribute("contentTemplate", "home");
        return "layout";
    }
}
