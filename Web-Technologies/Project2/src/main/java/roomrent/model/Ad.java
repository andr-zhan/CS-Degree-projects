package roomrent.model;

import jakarta.persistence.*; // Anotações para mapeamento ORM (Entity, Column, Id, many-to-one, etc.)
import jakarta.validation.constraints.*; // Restrições de validação de campos (NotNull, NotBlank, Size, etc.)

import java.math.BigDecimal; // Classe para valores monetários
import java.time.Instant; // Representa um instante no tempo

// Entidade JPA que representa um anúncio de alojamento
@Entity
@Table(name = "ads")
public class Ad {
    // Tipo do anúncio
    public enum AdType { OFERTA, PROCURA }
    // Tipo de alojamento
    public enum AccommodationType { QUARTO, T0, T1, T2, T3, T4, T5 }
    // Preferência de género do anunciante
    public enum GenderPreference { MASCULINO, FEMININO, INDIFERENTE }
    // Estado do anúncio
    public enum State { INATIVO, ATIVO }

    // Chave primária (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código público único do anúncio
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // Tipo do anúncio
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdType type;

    // Tipologia de alojamento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccommodationType accommodationType;

    // Preferência de género (padrão INDIFERENTE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderPreference genderPreference = GenderPreference.INDIFERENTE;

    // Preço pedido pelo anunciante
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    @Column(nullable = false)
    private BigDecimal price;

    // Descrição detalhada do anúncio
    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String details;

    // Nome do anunciante guardado no momento da criação
    @Column(nullable = false, length = 120)
    private String advertiserName;

    // Informação de contacto do anunciante
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String contact;

    // Zona
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String zone;

    // Data de criação (definida no momento da criação)
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Estado do anúncio (ATIVO / INATIVO) - gerido pelos administradores
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state = State.INATIVO; // padrão INATIVO

    // Dono do anúncio (um utilizador pode ter vários anúncios)
    @ManyToOne(optional = false)
    private User owner;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public GenderPreference getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(GenderPreference genderPreference) {
        this.genderPreference = genderPreference;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
