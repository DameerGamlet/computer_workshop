package com.thesis.computer_workshop.repositories.productsRepositories;

import com.thesis.computer_workshop.models.products.Notebook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotebookRepository  extends CrudRepository<Notebook, Long> {
    long count();
    List<Notebook> findAll();
    Iterable<Notebook> findByBrand(String brand);
    @Query(value = "select n.* from notebook n where n.price between :a and :b", nativeQuery = true)
    List<Notebook> findAllByFromAndToPrice(@Param("a") double a, @Param("b") double b);
    @Query(value = "select n.* from notebook n where n.price between :a and :b", nativeQuery = true)
    List<Notebook> findAllByFromQuery(@Param("a") double a, @Param("b") double b);
    @Query(value = "select n.* from notebook n order by n.name", nativeQuery = true)
    List<Notebook> findAllOrderByName();
    @Query(value = "select n.* from notebook n where n.name LIKE %:a% " +
            " or n.brand like %:a%" +
            " or n.processor_name like %:a% " +
            " or n.video_card_name like %:a%", nativeQuery = true)
    List<Notebook> findAllByKeyword(@Param("a") String keyword);
}
