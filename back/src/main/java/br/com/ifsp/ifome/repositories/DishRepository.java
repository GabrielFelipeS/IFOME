package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%'")
    List<Dish> findAllByRestaurantId(Long id);

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%'")
    List<Dish> findAllAvailable(Sort sort);

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%'")
    Page<Dish> findAllAvailable(PageRequest name);
}
