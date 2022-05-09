package com.thesis.computer_workshop.repositories.usersRepositories;

import com.thesis.computer_workshop.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
