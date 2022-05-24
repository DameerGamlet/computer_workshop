package com.thesis.computer_workshop.repositories.productsRepositories;

import com.thesis.computer_workshop.models.products.Notebook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotebookRepository  extends CrudRepository<Notebook, Long> {
    long count();
    Iterable<Notebook> findByBrand(@Param("brand") String brand);
    @Query(value = "select n.* from notebook n where n.price between :a and :b", nativeQuery = true)
    List<Notebook> findAllByFromAndToPrice(@Param("a") double a, @Param("b") double b);
}
