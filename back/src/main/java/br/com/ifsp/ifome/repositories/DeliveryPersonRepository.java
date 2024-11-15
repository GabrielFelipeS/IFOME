package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.DeliveryPerson;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryPersonRepository extends CrudRepository<DeliveryPerson, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);

    Optional<DeliveryPerson> findByEmail(String email);
    boolean existsByCnhNumber(String cnhNumber);



    @Query("""
    SELECT d
    FROM DeliveryPerson d
    WHERE d.available = 'Disponível' AND d.latitude IS NOT NULL  AND d.longitude IS NOT NULL
    AND NOT EXISTS (
        SELECT co
        FROM CustomerOrder co
        WHERE co.deliveryPerson.id = d.id AND co.currentOrderClientStatus != 'CONCLUIDO'
        OR EXISTS (
            SELECT r
            FROM RefuseCustomerOrder r
            WHERE r.deliveryId = d.id AND r.customerOrderId = co.id AND r.customerOrderId = :id
        )
    )
""")
    List<DeliveryPerson> findDeliveryPersonAvailable(@Param("id") Long id);


    @Query("""
        UPDATE DeliveryPerson SET available = 'Indisponível'
        WHERE available = 'Disponível'
        AND latitude is not null
        AND longitude is not null
        AND lastUpdate < :fiveMinutesAgo
    """)
    @Modifying
    void disabledDeliveryPerson(@Param("fiveMinutesAgo") LocalDateTime fiveMinutesAgo);
}
