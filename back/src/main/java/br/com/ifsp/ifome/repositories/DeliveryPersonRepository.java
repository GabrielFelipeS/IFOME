package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.DeliveryPerson;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeliveryPersonRepository extends CrudRepository<DeliveryPerson, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);
    Optional<DeliveryPerson> findByEmail(String email);
    boolean existsByCnhNumber(String cnhNumber);
}
