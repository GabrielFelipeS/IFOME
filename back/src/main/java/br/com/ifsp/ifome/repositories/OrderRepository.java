package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long>
{
}
