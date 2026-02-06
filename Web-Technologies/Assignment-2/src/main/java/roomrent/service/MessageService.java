package roomrent.service;

// Domain models
import roomrent.model.Ad; // Entidade Ad
import roomrent.model.Message; // Entidade Message
import roomrent.model.User; // Entidade User (remetente/destinatário)

// Repositório
import roomrent.repository.MessageRepository; // Acesso a mensagens no BD

// Transações e stereotype
import jakarta.transaction.Transactional; // Anotação para métodos transacionais
import org.springframework.stereotype.Service; // Marca a classe como Service Spring

import java.util.List; // Lista de mensagens

// Serviço responsável por enviar e listar mensagens relacionadas a anúncios.
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    // Construtor: injeta dependência do repositório de mensagens
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Cria e persiste uma nova mensagem entre utilizadores para um anúncio
    @Transactional
    public Message sendMessage(Ad ad, User from, User to, String content) {
        Message m = new Message();
        m.setAd(ad);
        m.setFromUser(from);
        m.setToUser(to);
        m.setContent(content);
        return messageRepository.save(m);
    }

    // Mensagens destinadas ao owner de um anúncio (conversa do owner para esse anúncio)
    public List<Message> messagesForAdOwner(Ad ad, User owner) {
        return messageRepository.findByAdAndToUser(ad, owner);
    }
}
