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

    @Query("SELECT d FROM DeliveryPerson d JOIN CustomerOrder co ON co.currentOrderClientStatus != 'CONCLUIDO' AND co.deliveryPerson.id = d.id JOIN RefuseCustomerOrder  r ON r.deliveryId = d.id AND r.customerOrderId = co.id")
    List<DeliveryPerson> findDeliveryPersonAvailable();
}
