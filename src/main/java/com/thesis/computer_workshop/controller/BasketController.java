package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.basketRepositories.BasketRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class BasketController {
    @Autowired
    public BasketRepository basketRepository;
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public UserRepository userRepository;

    @GetMapping("/basket")
    public String returnAdminCatalog(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("counts", counts);
        return "/basket/user_basket";
    }
    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
