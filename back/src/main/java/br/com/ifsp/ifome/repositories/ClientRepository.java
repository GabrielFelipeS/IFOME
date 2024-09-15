package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    boolean existsByEmail(String email);
    Optional<Client> findByEmail(String email);
}
