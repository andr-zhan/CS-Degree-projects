package roomrent.controller;

// Entidades e repositórios usados pelo controller
import roomrent.model.Ad; // Entidade do anúncio
import roomrent.model.User; // Entidade do utilizador
import roomrent.repository.UserRepository; // Acesso a dados de utilizadores

// Services
import roomrent.service.AdService; // Lógica de anúncios
import roomrent.service.MessageService; // Lógica de mensagens (envio, listagem)

// Segurança e MVC
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Injeta UserDetails do utilizador autenticado
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Representa uma Authority para checks rápidos
import org.springframework.security.core.userdetails.UserDetails; // Interface do utilizador autenticado
import org.springframework.stereotype.Controller; // Marca a classe como Controller
import org.springframework.ui.Model; // Model para enviar atributos à view
import org.springframework.web.bind.annotation.*; // Mapear endpoints HTTP (@PostMapping, @RequestParam, etc.)

@Controller
@RequestMapping("/messages") // Mapeia todos os endpoints para /messages
public class MessageController {
    // Serviço para envio/listagem de mensagens
    private final MessageService messageService;

    // Serviço para obter anúncios relacionados
    private final AdService adService;
    
    // Repositório para aceder a utilizadores (remetente/destinatário)
    private final UserRepository userRepository;

    // Construtor: injeta dependências
    public MessageController(MessageService messageService, AdService adService, UserRepository userRepository) {
        this.messageService = messageService;
        this.adService = adService;
        this.userRepository = userRepository;
    }

    // Envia uma mensagem relacionada com um anúncio (adId)
    @PostMapping("/send/{adId}")
    public String send(@PathVariable Long adId,
                       @RequestParam String content,
                       @AuthenticationPrincipal UserDetails principal,
                       Model model) {
        // Obtém anúncio e utilizadores (remetente e destinatário)
        Ad ad = adService.get(adId);
        User from = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        User to = ad.getOwner();
        
        // Bloqueia admins de enviar mensagens (regra de negócio)
        boolean isAdmin = principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")); // Verifica se o utilizador é admin
        if (isAdmin) {
            model.addAttribute("ad", ad);
            model.addAttribute("error", "Admins não podem enviar mensagens.");
            model.addAttribute("contentTemplate", "ads/details");
            model.addAttribute("showFooter", false);
            return "layout";
        }

        // Impede o owner de enviar mensagem para o próprio anúncio
        if (from.getId().equals(to.getId())) {
            model.addAttribute("ad", ad);
            model.addAttribute("error", "Não pode enviar mensagem para o próprio anúncio.");
            model.addAttribute("contentTemplate", "ads/details");
            model.addAttribute("showFooter", false);
            return "layout";
        }
        
        // Envia a mensagem e mostra confirmação na mesma página de detalhe
        messageService.sendMessage(ad, from, to, content);
        model.addAttribute("ad", ad);
        model.addAttribute("message", "Mensagem enviada ao anunciante.");
        model.addAttribute("contentTemplate", "ads/details");
        model.addAttribute("showFooter", false);
        return "layout";
    }
}
