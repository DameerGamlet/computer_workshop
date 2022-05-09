package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.thesis.computer_workshop.models.users.User;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginUser(){
        return "users/login";
    }

    @GetMapping("/registration")
    public String registrationUser(){
        return "users/registration";
    }

//    @PostMapping("/registration")
//    public String createUser(User user, Model model){
//        if(!userService.createUser(user)){
//            model.addAttribute("errorMessage", "Пользователь с email \"" + user.getEmail() + "\" уже существует");
//            return "registration";
//        }
//        return "redirect:/login";
//    }
}
