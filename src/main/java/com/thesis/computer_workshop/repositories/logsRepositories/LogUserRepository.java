package com.thesis.computer_workshop.repositories.logsRepositories;

import com.thesis.computer_workshop.models.logs.LogProduct;
import com.thesis.computer_workshop.models.logs.LogUser;
import org.springframework.data.repository.CrudRepository;

public interface LogUserRepository extends CrudRepository<LogUser, Long> {

}
