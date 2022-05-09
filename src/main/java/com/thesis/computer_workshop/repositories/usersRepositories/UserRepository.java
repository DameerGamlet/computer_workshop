package com.thesis.computer_workshop.repositories.usersRepositories;

import com.thesis.computer_workshop.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
