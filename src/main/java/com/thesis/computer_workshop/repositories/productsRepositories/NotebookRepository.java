package com.thesis.computer_workshop.repositories.productsRepositories;

import com.thesis.computer_workshop.models.products.Notebook;
import org.springframework.data.repository.CrudRepository;

public interface NotebookRepository  extends CrudRepository<Notebook, Long> {
}
