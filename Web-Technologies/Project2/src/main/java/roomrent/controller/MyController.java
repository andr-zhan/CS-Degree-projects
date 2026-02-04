package roomrent.controller;

// Entidades e repositórios usados na área pessoal
import roomrent.model.Ad; // Entidade do anúncio
import roomrent.model.PaymentRef; // Referência de pagamento (Multibanco)
import roomrent.model.User; // Entidade do utilizador
import roomrent.repository.AdRepository; // Acesso a anúncios do utilizador
import roomrent.repository.PaymentRefRepository; // Acesso a referências de pagamento
import roomrent.repository.UserRepository; // Acesso a utilizadores

// Services
import roomrent.service.AdService; // Lógica de anúncios
import roomrent.service.MessageService; // Lógica de mensagens

// Segurança e MVC
import org.springframework.security.access.AccessDeniedException; // Lança quando ação não permitida
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Injeta o user autenticado
import org.springframework.security.core.userdetails.UserDetails; // Representa o utilizador autenticado
import org.springframework.stereotype.Controller; // Marca a classe como Controller
import org.springframework.ui.Model; // Model para enviar atributos à view
import org.springframework.web.bind.annotation.GetMapping; // Mapear GET
import org.springframework.web.bind.annotation.PathVariable; // Mapear variáveis de path
import org.springframework.web.bind.annotation.RequestMapping; // Mapear prefixo de rota

import java.util.List; // Lista dos anúncios do utilizador

@Controller
@RequestMapping("/my") // Mapeia todos os endpoints para /my
public class MyController {
    // Serviço para gerir mensagens (mensagens por anúncio)
    private final MessageService messageService;

    // Serviço para lógica relacionada com anúncios
    private final AdService adService;

    // Repositórios para obter dados do utilizador, anúncios e pagamentos
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final PaymentRefRepository paymentRefRepository;

    // Construtor: injeta serviços e repositórios necessários
    public MyController(MessageService messageService, AdService adService, UserRepository userRepository, 
                        AdRepository adRepository, PaymentRefRepository paymentRefRepository) {
        this.messageService = messageService;
        this.adService = adService;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.paymentRefRepository = paymentRefRepository;
    }

    // Dashboard pessoal: mostra todos os anúncios do utilizador
    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        
        // Bloquear acesso de admins (devem usar área /admin)
        if (user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()))) {
            throw new AccessDeniedException("Admins devem usar /admin");
        }
        
        // Obtém anúncios do utilizador ordenados por data de criação (mais recentes primeiro)
        List<Ad> myAds = adRepository.findByOwnerOrderByCreatedAtDesc(user);
        model.addAttribute("user", user);
        model.addAttribute("myAds", myAds);
        model.addAttribute("contentTemplate", "my/dashboard");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Mostra as mensagens de um anúncio para o seu owner
    @GetMapping("/ads/{id}/messages")
    public String adMessages(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails principal,
                             Model model) {
        User owner = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        
        // Bloquear acesso de admins
        if (owner.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()))) {
            throw new AccessDeniedException("Admins devem usar /admin");
        }
        
        Ad ad = adService.get(id);
        
        // Verificar se o user é o dono do anúncio
        if (!ad.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Não autorizado");
        }
        
        model.addAttribute("ad", ad);
        model.addAttribute("messages", messageService.messagesForAdOwner(ad, owner));
        model.addAttribute("contentTemplate", "messages/ad-messages");
        model.addAttribute("showFooter", false);
        return "layout";
    }

    // Mostra detalhes de pagamento para o owner do anúncio
    @GetMapping("/ads/{id}/payment")
    public String adPayment(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails principal,
                            Model model) {
        User owner = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        
        // Bloquear acesso de admins
        if (owner.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()))) {
            throw new AccessDeniedException("Admins devem usar /admin");
        }
        
        Ad ad = adService.get(id);
        
        // Verificar se o user é o dono do anúncio
        if (!ad.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Não autorizado");
        }
        
        // Procurar a referência de pagamento do anúncio
        PaymentRef payment = paymentRefRepository.findByAd(ad);
        
        model.addAttribute("ad", ad);
        model.addAttribute("payment", payment);
        model.addAttribute("contentTemplate", "my/payment");
        model.addAttribute("showFooter", false);
        return "layout";
    }
}
