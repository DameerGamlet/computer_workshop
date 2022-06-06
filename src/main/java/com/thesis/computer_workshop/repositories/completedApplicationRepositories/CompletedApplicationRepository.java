package com.thesis.computer_workshop.repositories.completedApplicationRepositories;

import com.thesis.computer_workshop.models.buy.CompletedApplication;
import org.springframework.data.repository.CrudRepository;

public interface CompletedApplicationRepository extends CrudRepository<CompletedApplication, Long> {
}
