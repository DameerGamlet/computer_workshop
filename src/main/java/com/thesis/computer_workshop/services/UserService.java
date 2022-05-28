package com.thesis.computer_workshop.services;

import com.thesis.computer_workshop.models.mail.MaiLSender;
import com.thesis.computer_workshop.models.users.Role;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private MaiLSender maiLSender;

    public boolean createUser(Usr user) {
        Usr usrBD = userRepository.findByUsername(user.getUsername());
        if (usrBD != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(user.getPassword());
        user.setActivationCode(UUID.randomUUID().toString());
        //        usr.setPassword(passwordEncoder.encode(usr.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String link = "http://localhost:8080/activate/" + user.getActivationCode();
            String message = "Добрый день " + user.getName()
                    + "!\nСпасибо за вашу регистрацию на нашем сервисе\n" + "-".repeat(50)
                    + "\nДля авторизации необходимо подтвердить пользователя перейдя по следующей ссылке: " + link + ".";
            maiLSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        Usr user = userRepository.findByActivationCode(code);

        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }
}
