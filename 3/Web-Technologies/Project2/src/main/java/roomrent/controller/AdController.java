package roomrent.controller;

// Entidades (domain models) usadas pelo controlador
import roomrent.model.Ad; // Entidade do anúncio
import roomrent.model.PaymentRef; // Referência de pagamento (Multibanco)
import roomrent.model.User; // Entidade do utilizador

// Repositórios e services
import roomrent.repository.UserRepository; // Acesso a dados de utilizadores
import roomrent.service.AdService; // Lógica de negócio para anúncios

// Validação e binding
import jakarta.validation.Valid; // Anotação para validação de beans

// Configuração e paginação
import org.springframework.beans.factory.annotation.Value; // Injeta propriedades do application.properties
import org.springframework.data.domain.Page; // Representa página de resultados
import org.springframework.data.domain.PageRequest; // Implementação de Pageable para requests de página
import org.springframework.data.domain.Pageable; // Interface para paginação

// Segurança
import org.springframework.security.access.AccessDeniedException; // Exceção para negar acesso (authorization)
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Injeta o UserDetails autenticado
import org.springframework.security.core.userdetails.UserDetails; // Interface que representa o utilizador autenticado

// MVC / Web
import org.springframework.stereotype.Controller; // Marca a classe como Spring MVC Controller
import org.springframework.ui.Model; // Model para enviar atributos para a view (Thymeleaf)
import org.springframework.web.bind.annotation.*; // Mapear endpoints HTTP (@GetMapping, @PostMapping, etc.)

@Controller
@RequestMapping("/ads") // Mapeia todos os endpoints para /ads
public class AdController {
    // Service com a lógica de negócio para anúncios
    private final AdService adService;

    // Repositório para aceder a utilizadores (usado ao criar anúncios)
    private final UserRepository userRepository;

    // Tamanho da página para paginação (lido de application.properties)
    @Value("${roomrent.pagination.size:4}")
    private int pageSize;

    // Construtor: injeta dependências (AdService, UserRepository)
    public AdController(AdService adService, UserRepository userRepository) {
        this.adService = adService;
        this.userRepository = userRepository;
    }

    // Lista anúncios com filtros (type, zone, name) e paginação
    @GetMapping
    public String list(@RequestParam(name = "type", required = false) Ad.AdType type,
                       @RequestParam(name = "zone", required = false) String zone,
                       @RequestParam(name = "name", required = false) String advertiserName,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Ad> ads = adService.search(type, zone, advertiserName, pageable);
        model.addAttribute("ads", ads);
        model.addAttribute("type", type);
        model.addAttribute("zone", zone);
        model.addAttribute("name", advertiserName);
        model.addAttribute("contentTemplate", "ads/list");
        model.addAttribute("showFooter", false); // Não mostrar o rodapé nesta página
        return "layout";
    }

    // Mostra detalhes do anúncio; getForDetails aplica regras de visibilidade (estado/roles)
    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          @AuthenticationPrincipal UserDetails principal,
                          Model model) {
        Ad ad = adService.getForDetails(id, principal);
        model.addAttribute("ad", ad);
        model.addAttribute("contentTemplate", "ads/details");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Mostra formulário para criar um novo anúncio
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ad", new Ad());
        model.addAttribute("contentTemplate", "ads/form");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Cria o anúncio: valida, guarda, cria referência de pagamento; trata AccessDeniedException para admins
    @PostMapping
    public String create(@Valid @ModelAttribute("ad") Ad ad,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model) {
        User owner = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        try {
            Ad saved = adService.createAd(ad, owner);
            PaymentRef ref = adService.createPaymentRef(saved);
            model.addAttribute("ad", saved);
            model.addAttribute("payment", ref);
            model.addAttribute("contentTemplate", "ads/payment");
            model.addAttribute("showFooter", false);
            return "layout";
        } catch (AccessDeniedException ex) {
            model.addAttribute("error", "Admins não podem criar anúncios.");
            model.addAttribute("ad", ad);
            model.addAttribute("contentTemplate", "ads/form");
            model.addAttribute("showFooter", false);
            return "layout";
        }
    }
}
