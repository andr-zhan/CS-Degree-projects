package roomrent.service;

import org.springframework.beans.factory.annotation.Value; // Para injetar valores de configuração
import org.springframework.http.HttpEntity; // Para criar requests HTTP
import org.springframework.http.HttpHeaders; // Para definir headers HTTP
import org.springframework.http.MediaType; // Para definir tipos de mídia (Content-Type)
import org.springframework.http.ResponseEntity; // Para capturar respostas HTTP
import org.springframework.stereotype.Service; // Marca a classe como Service Spring
import org.springframework.util.LinkedMultiValueMap; // Implementação de MultiValueMap
import org.springframework.util.MultiValueMap; // Para criar corpo de requests com múltiplos valores
import org.springframework.web.client.RestClientException; // Exceção para erros em REST calls
import org.springframework.web.client.RestTemplate; // Cliente REST para fazer HTTP requests

import java.math.BigDecimal; // Para valores monetários

// Serviço responsável por interagir com o serviço externo de pagamento (obter referências MB)
@Service
public class PaymentService {

    // Classe record para armazenar a informação de pagamento Multibanco
    public record MBPaymentInfo(String entity, String reference) {}

    // Cliente REST para fazer requests HTTP
    private final RestTemplate restTemplate = new RestTemplate();

    // Endpoint do serviço de pagamento
    @Value("${roomrent.payment.mb.endpoint}")
    private String endpoint;

    public MBPaymentInfo requestMbRef(BigDecimal amount) {
        try {
            // Preparar headers com Content-Type application/x-www-form-urlencoded
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            // Criar body com o parâmetro amount
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("amount", amount.toString());
            
            // Criar HttpEntity com headers e body
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            
            // Fazer POST request
            ResponseEntity<String> resp = restTemplate.postForEntity(endpoint, request, String.class);
            String responseBody = resp.getBody() == null ? "" : resp.getBody();
            
            // Parse JSON response
            String entity = extract(responseBody, "mb_entity");
            String reference = extract(responseBody, "mb_reference");
            
            // Valores de fallback se não obtiver dados válidos
            if (entity.isEmpty()) entity = "12000";
            if (reference.isEmpty()) reference = "000000000";
            
            return new MBPaymentInfo(entity, reference);
        } catch (RestClientException ex) {
            // Tolerar falhas no serviço de pagamento, retornar valores padrão
            return new MBPaymentInfo("12000", "999999999");
        }
    }

    // Método simples para extrair valores de um JSON simples
    private String extract(String json, String key) {
        int i = json.indexOf('"' + key + '"'); 
        if (i < 0) return "";
        int colon = json.indexOf(':', i);
        int start = json.indexOf('"', colon + 1) + 1;
        int end = json.indexOf('"', start);
        return json.substring(start, end);
    }
}
