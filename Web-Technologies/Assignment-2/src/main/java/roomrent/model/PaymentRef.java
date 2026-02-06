package roomrent.model;

// JPA: anotações ORM (Entity, Id, JoinColumn, OneToOne, etc.)
import jakarta.persistence.*;

import java.math.BigDecimal; // Valores monetários com precisão
import java.time.Instant; // Representa um instante no tempo

// Entidade JPA que representa uma referência de pagamento Multibanco associada a um anúncio
@Entity
@Table(name = "payment_refs")
public class PaymentRef {
    // Chave primária (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associação OneToOne com Ad: cada anúncio tem no máximo uma referência
    @OneToOne(optional = false)
    @JoinColumn(name = "ad_id", unique = true, nullable = false)
    private Ad ad;

    // Código da entidade Multibanco (entity)
    @Column(nullable = false)
    private String entity;

    // Código de referência Multibanco
    @Column(nullable = false)
    private String reference;

    // Montante associado à referência
    @Column(nullable = false)
    private BigDecimal amount;

    // Data de criação da referência (definida ao criar)
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
