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

import java.security.Principal;
import java.util.Collections;

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
    public String createUser(Usr usr, Model model) {
        System.out.println(123);
        if(!userService.createUser(usr)){
            return "users/registration";
        }
        return "users/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActive = userService.activateUser(code);
        if(isActive){
            model.addAttribute("message", true);
        }
        else {
            model.addAttribute("message", false);
        }
        return "users/login";
    }

    @GetMapping("/account")
    public String getAccount(Principal principal,  Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
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
