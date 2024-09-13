package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
    boolean existsByEmail(String email);
}
