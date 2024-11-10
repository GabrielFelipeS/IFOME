package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
