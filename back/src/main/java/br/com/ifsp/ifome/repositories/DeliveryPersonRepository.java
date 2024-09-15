package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.DeliveryPerson;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeliveryPersonRepository extends CrudRepository<DeliveryPerson, Long> {
    boolean existsByEmail(String email);
    Optional<DeliveryPerson> findByEmail(String email);
}
