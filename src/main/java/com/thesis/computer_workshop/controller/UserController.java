package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.users.Role;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registrationUser(Model model) {
        return "users/registration";
    }

    @PostMapping("/registration")
    public String createUser(Usr usr, Model model) {
        Usr byUserName = userRepository.findByUsername(usr.getUsername());
        if(byUserName != null){
            model.addAttribute("message", "Такой пользователь уже существует !!!");
            return "users/registration";
        }
        usr.setActive(true);
//        usr.setPassword(passwordEncoder.encode(usr.getPassword()));
        usr.setRoles(Collections.singleton(Role.USER));
        userRepository.save(usr);
        System.out.println(usr.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/account")
    public String getAccount(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "users/account";
    }

    public Usr getUserByPrincipal(Principal principal){
        if(principal == null){
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
