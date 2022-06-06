package com.thesis.computer_workshop.repositories.applicationRepositories;

import com.thesis.computer_workshop.models.application.RepairApplication;
import org.springframework.data.repository.CrudRepository;

public interface RepairRepository extends CrudRepository<RepairApplication, Long> {
}
