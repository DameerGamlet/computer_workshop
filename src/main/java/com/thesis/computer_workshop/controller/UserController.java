package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.users.Role;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import com.thesis.computer_workshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private UserService userService;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginUser(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "users/login";
    }

    @GetMapping("/registration")
    public String registrationUser(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "users/registration";
    }

    @PostMapping("/registration")
    public String createUser(Usr usr, Model model, @RequestParam(name = "confPassword", required = false) String confPassword) {
        if (!Objects.equals(usr.getPassword(), confPassword)) {
            model.addAttribute("message", "Пароли не совпадают. Пожалуйста, проверьте правильность введённых данных.");
            return "users/registration";
        }
        if (!userService.createUser(usr)) {
            model.addAttribute("message", "Такой аккаунт уже существует!");
            return "users/registration";
        }
        model.addAttribute("activate", "Активируйте аккаунт! Для этого перейдите по адресу на пришедщее сообщение.");
        return "users/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActive = userService.activateUser(code);
        if (isActive) {
            model.addAttribute("message", "Вы успешно активировали аккаунт");
        } else {
            model.addAttribute("message", "Код не действителен");
        }
        return "users/login";
    }

    @GetMapping("/account")
    public String getAccount(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_update_data")
    public String updateAccount(Principal principal, Model model, @RequestParam(name = "username") String username,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "phoneNumber") String phoneNumber) {
        System.out.println(username + " " + name + " " + email + " " + phoneNumber);
        Usr user = getUserByPrincipal(principal);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_update_password")
    public String updatePasswordAccount(Principal principal, Model model,
                                        @RequestParam(name = "password") String password,
                                        @RequestParam(name = "new_password") String new_password,
                                        @RequestParam(name = "conf_new_password") String conf_new_password) {
        System.out.println(password + " " + new_password + " " + conf_new_password);
        Usr user = getUserByPrincipal(principal);
        if (Objects.equals(user.getPassword(), password)) {
            if (Objects.equals(new_password, conf_new_password)) {
                user.setPassword(new_password);
                userRepository.save(user);
                model.addAttribute("message_update_password", "Пароль успешно обновлён!");
            } else {
                model.addAttribute("message_update_password", "Введённый пароль неверен или новый пароль не соотвествует подтверждению!");
            }
        }
        model.addAttribute("show_result", true);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_delete")
    public String deleteAccount(Principal principal, Model model,
                                @RequestParam(name = "username_check") String username,
                                @RequestParam(name = "password_check") String password) {
        System.out.println(username + " " + password);
        Usr user = getUserByPrincipal(principal);
        if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)) {
            userRepository.delete(user);
        }
        model.addAttribute("check", user.getUsername() != null);
        return "main/main";
    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
