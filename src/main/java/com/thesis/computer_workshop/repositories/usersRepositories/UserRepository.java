package com.thesis.computer_workshop.repositories.usersRepositories;

import com.thesis.computer_workshop.models.users.Usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usr, Long> {
    Usr findByUsername(String username);
}
