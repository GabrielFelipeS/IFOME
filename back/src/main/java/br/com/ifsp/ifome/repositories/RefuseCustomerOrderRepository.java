package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.RefuseCustomerOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefuseCustomerOrderRepository extends CrudRepository<RefuseCustomerOrder, Long> {
    List<RefuseCustomerOrder> findRefuseCustomerOrderByCustomerOrderId(Long id);
}
