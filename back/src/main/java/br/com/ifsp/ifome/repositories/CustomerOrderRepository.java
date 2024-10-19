package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>
{
    List<CustomerOrder> findAllByCartClientEmail(String email);

}
