package roomrent.repository;

import roomrent.model.Ad; // Entidade Ad
import roomrent.model.Ad.AdType; // Enum: tipo do anúncio
import roomrent.model.Ad.State; // Enum: estado do anúncio
import roomrent.model.User; // Entidade User
import org.springframework.data.domain.Page; // Tipo de página de resultados
import org.springframework.data.domain.Pageable; // Parâmetros de paginação
import org.springframework.data.jpa.repository.JpaRepository; // Interface base do Spring Data JPA
import org.springframework.data.jpa.repository.Query; // Anotação para consultas JPQL
import org.springframework.data.repository.query.Param; // Binding de parâmetros em @Query

import java.util.List; // Lista de anúncios

// Repositório para gerir anúncios
public interface AdRepository extends JpaRepository<Ad, Long> {

    // Busca paginada por tipo e estado
    Page<Ad> findByTypeAndState(AdType type, State state, Pageable pageable);

    // Busca com filtros opcionais 
    // retorna apenas anúncios com estado ATIVO
    @Query("SELECT a FROM Ad a WHERE" +
           " (:type IS NULL OR a.type = :type)" +
           " AND (:zone IS NULL OR LOWER(a.zone) LIKE LOWER(CONCAT('%', CAST(:zone AS string), '%')))" +
           " AND (:name IS NULL OR LOWER(a.advertiserName) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))" +
           " AND a.state = 'ATIVO'")
    Page<Ad> search(@Param("type") AdType type,
                    @Param("zone") String zone,
                    @Param("name") String advertiserName,
                    Pageable pageable);

    // Pesquisa utilizada pela área admin (inclui filtro de estado)
    @Query("SELECT a FROM Ad a WHERE" +
           " (:type IS NULL OR a.type = :type)" +
           " AND (:zone IS NULL OR LOWER(a.zone) LIKE LOWER(CONCAT('%', CAST(:zone AS string), '%')))" +
           " AND (:name IS NULL OR LOWER(a.advertiserName) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))" +
           " AND (:state IS NULL OR a.state = :state)")
    Page<Ad> adminSearch(@Param("type") AdType type,
                         @Param("zone") String zone,
                         @Param("name") String advertiserName,
                         @Param("state") State state,
                         Pageable pageable);

    // Últimos anúncios ativos por tipo (ordenados por createdAt desc)
    @Query("SELECT a FROM Ad a WHERE" +
           " a.type = :type AND a.state = 'ATIVO'" +
           " ORDER BY a.createdAt DESC")
    Page<Ad> latestByTypeAndActive(@Param("type") AdType type, Pageable pageable);

    // Verifica se existe um anúncio com determinado código único
    boolean existsByCode(String code);
    
    // Lista anúncios por estado (paginada)
    Page<Ad> findByState(State state, Pageable pageable);
    
    // Lista anúncios de um utilizador (owner) ordenados por data de criação descendente
    List<Ad> findByOwnerOrderByCreatedAtDesc(User owner);
}
