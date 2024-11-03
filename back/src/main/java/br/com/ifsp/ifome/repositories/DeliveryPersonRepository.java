package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.DeliveryPerson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryPersonRepository extends CrudRepository<DeliveryPerson, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);

    Optional<DeliveryPerson> findByEmail(String email);
    boolean existsByCnhNumber(String cnhNumber);

//    @Query("SELECT d FROM DeliveryPerson d JOIN CustomerOrder co ON co.currentOrderClientStatus != 'CONCLUIDO'")
    @Query("""
    SELECT d
    FROM DeliveryPerson d
    WHERE NOT EXISTS (
        SELECT co
        FROM CustomerOrder co
        WHERE co.deliveryPerson = d
        AND co.currentOrderClientStatus != 'CONCLUIDO'
    )
    AND NOT EXISTS (
        SELECT r
        FROM RefuseCustomerOrder r
        WHERE r.deliveryId = d.id
    )
""")
    List<DeliveryPerson> findDeliveryPersonAvailable();
}
