package com.thesis.computer_workshop.repositories;

import com.thesis.computer_workshop.models.Notebook;
import org.springframework.data.repository.CrudRepository;

public interface NotebookRepository  extends CrudRepository<Notebook, Long> {
}
