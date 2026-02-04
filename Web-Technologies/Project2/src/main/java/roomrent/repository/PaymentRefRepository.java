package roomrent.repository;

import roomrent.model.PaymentRef; // Entidade PaymentRef (referência de pagamento)
import roomrent.model.Ad; // Entidade Ad (anúncio associado)
import org.springframework.data.jpa.repository.JpaRepository; // Interface base do Spring Data JPA

// Repositório para gerir referências de pagamento
public interface PaymentRefRepository extends JpaRepository<PaymentRef, Long> {

    // Procura a referência de pagamento associada a um anúncio
    PaymentRef findByAd(Ad ad);
}
