package roomrent.repository;

import roomrent.model.Message; // Entidade Message
import roomrent.model.User; // Entidade User (remetente/destinatário)
import roomrent.model.Ad; // Entidade Ad relacionada à mensagem
import org.springframework.data.jpa.repository.JpaRepository; // Interface base Spring Data JPA
import org.springframework.data.jpa.repository.Query; // Anotação para consultas JPQL

import java.util.List; // java.util.List

// Repositório para gerir mensagens
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Mensagens de um anúncio filtradas pelo destinatário (conversa para o owner)
    List<Message> findByAdAndToUser(Ad ad, User toUser);

    // Todas as mensagens associadas a um anúncio (usado ao apagar um anúncio)
    List<Message> findByAd(Ad ad);
}
