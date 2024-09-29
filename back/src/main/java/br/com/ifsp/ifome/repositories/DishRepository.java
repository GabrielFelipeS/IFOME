package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Long> {

}
