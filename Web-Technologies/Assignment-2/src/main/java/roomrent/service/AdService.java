package roomrent.service;

// Domain models
import roomrent.model.Ad; // Entidade Ad
import roomrent.model.PaymentRef; // Entidade PaymentRef
import roomrent.model.User; // Entidade User

// Repositórios
import roomrent.repository.AdRepository; // Repositório de anúncios
import roomrent.repository.MessageRepository; // Repositório de mensagens
import roomrent.repository.PaymentRefRepository; // Repositório de referências de pagamento

// Transações e configuração
import jakarta.transaction.Transactional; // Anotação para métodos transacionais
import org.springframework.beans.factory.annotation.Value; // Injeta configurações do application.properties

// Segurança e exceções
import org.springframework.security.access.AccessDeniedException; // Exceção para negar ações não autorizadas
import org.springframework.data.domain.Page; // Resultado paginado
import org.springframework.data.domain.PageRequest; // Implementação de Pageable
import org.springframework.data.domain.Pageable; // Interface de paginação
import org.springframework.security.core.userdetails.UserDetails; // Representa o utilizador autenticado
import org.springframework.stereotype.Service; // Marca a classe como Service Spring
import org.springframework.http.HttpStatus; // Códigos HTTP
import org.springframework.web.server.ResponseStatusException; // Exceção com status HTTP

// Utilitários
import java.math.BigDecimal; // Para valores monetários
import java.security.SecureRandom; // Gerador de números seguros
import java.util.Random; // Gerador de números aleatórios

// Serviço com lógica de negócio relacionada com anúncios (criação, pesquisa, exclusão, pagamentos)
@Service
public class AdService {
    // Dependências (injeção via construtor)
    private final AdRepository adRepository; // Acesso a anúncios
    private final PaymentService paymentService; // Serviço externo para obter referências MB
    private final PaymentRefRepository paymentRefRepository; // Acesso a referências de pagamento
    private final MessageRepository messageRepository; // Acesso a mensagens relacionadas

    // Gerador de códigos seguro para o campo `code` dos anúncios
    private final Random random = new SecureRandom();

    // Taxa fixa para publicação de anúncios
    @Value("${roomrent.ad.publication.fee}")
    private BigDecimal publicationFee;

    // Construtor: injeta dependências necessárias
    public AdService(AdRepository adRepository, PaymentService paymentService, PaymentRefRepository paymentRefRepository, MessageRepository messageRepository) {
        this.adRepository = adRepository;
        this.paymentService = paymentService;
        this.paymentRefRepository = paymentRefRepository;
        this.messageRepository = messageRepository;
    }

    // Gera um código público único para o anúncio (8 caracteres, sem ambíguas)
    private String generateCode() {
        String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        String code;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
            code = sb.toString();
        } while (adRepository.existsByCode(code));
        return code;
    }

    // Cria um anúncio: valida que o owner não é admin e atribui code/owner
    @Transactional
    public Ad createAd(Ad ad, User owner) {
        if (owner.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()))) {
            throw new AccessDeniedException("Admins cannot create ads");
        }
        ad.setOwner(owner);
        // Garantir que o nome do anunciante corresponde ao username autenticado
        ad.setAdvertiserName(owner.getUsername());
        ad.setCode(generateCode());
        Ad saved = adRepository.save(ad);
        return saved;
    }

    // Cria e persiste uma referência de pagamento (Multibanco) para um anúncio
    @Transactional
    public PaymentRef createPaymentRef(Ad ad) {
        BigDecimal amount = publicationFee;
        PaymentService.MBPaymentInfo info = paymentService.requestMbRef(amount); // Obter referência MB do serviço externo
        PaymentRef ref = new PaymentRef();
        ref.setAd(ad);
        ref.setAmount(amount);
        ref.setEntity(info.entity());
        ref.setReference(info.reference());
        return paymentRefRepository.save(ref);
    }

    // Retorna os últimos anúncios ativos por tipo (usado na página inicial)
    public Page<Ad> latestByType(Ad.AdType type, int size) {
        Pageable p = PageRequest.of(0, size);
        return adRepository.latestByTypeAndActive(type, p);
    }

    // Pesquisa com filtros opcionais (tipo, zona, anunciante)
    public Page<Ad> search(Ad.AdType type, String zone, String advertiserName, Pageable pageable) {
        return adRepository.search(type, zone, advertiserName, pageable);
    }

    // Pesquisa usada pelo admin (inclui filtro de estado)
    public Page<Ad> adminSearch(Ad.AdType type, String zone, String advertiserName, Ad.State state, Pageable pageable) {
        return adRepository.adminSearch(type, zone, advertiserName, state, pageable);
    }

    // Altera o estado (ATIVO/INATIVO) de um anúncio (usado por admin)
    @Transactional
    public void setState(Long adId, Ad.State state) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        ad.setState(state);
        adRepository.save(ad);
    }

    // Obtém um anúncio por id (lança se não existir)
    public Ad get(Long id) { return adRepository.findById(id).orElseThrow(); }

    /*
     * Detalhes públicos: anúncios ativos são sempre visíveis; 
     * anúncios inativos só para admins.
     */
    public Ad getForDetails(Long id, UserDetails principal) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (ad.getState() == Ad.State.ATIVO) {
            return ad;
        }
        // Verifica se o utilizador autenticado é admin
        boolean isAdmin = principal != null && principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            return ad;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // Remove um anúncio e os dados relacionados
    @Transactional
    public void deleteAd(Long adId) {
        // Remover referência de pagamento e mensagens associadas antes de apagar o anúncio
        Ad ad = adRepository.findById(adId).orElseThrow();
        PaymentRef payment = paymentRefRepository.findByAd(ad);
        if (payment != null) {
            paymentRefRepository.delete(payment);
        }
        messageRepository.deleteAll(messageRepository.findByAd(ad));
        adRepository.deleteById(adId);
    }
}
