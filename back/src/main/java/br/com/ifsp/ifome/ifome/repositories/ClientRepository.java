package br.com.ifsp.ifome.ifome.repositories;

import br.com.ifsp.ifome.ifome.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
