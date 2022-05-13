//package com.thesis.computer_workshop.services;
//
//import com.thesis.computer_workshop.models.logs.LogUser;
//import com.thesis.computer_workshop.models.users.User;
//import com.thesis.computer_workshop.models.users.Role;
//import com.thesis.computer_workshop.repositories.logsRepositories.LogUserRepository;
//import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class UserService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final LogUserRepository logUserRepository;
//
//    public boolean createUser(User user){
//        String email = user.getEmail();
//        if(userRepository.findByUsername(email) != null){
//            return false;
//        }
//        user.setActive(true);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.getRoles().add(Role.ROLE_USER);
//        log.info("save user {}", email);
//        userRepository.save(user);
//        saveLogs(user);
//        return true;
//    }
//
//
//}
