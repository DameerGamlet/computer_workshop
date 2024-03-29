package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.basket.Basket;
import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.basketRepositories.BasketRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import com.thesis.computer_workshop.services.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class NoteBookController {
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public BasketRepository basketRepository;
    private final NotebookService notebookService;

    @GetMapping("/notebooks/list")
    public String returnAllNoteBooksWithKeyword(@RequestParam(name = "keyword", required = false) String keyword, Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        System.out.println(keyword);
        Iterable<Notebook> notebooks = notebookService.listProducts(keyword);
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("counts", counts);
        return "/products/notebook/notebooks_list";
    }

//    @PostMapping("/add_basket")
//    public String saveNotebook(Model model, Principal principal, Notebook notebook) {
//        Usr user = getUserByPrincipal(principal);
//        Basket basket = new Basket();
//        basket.setUserClass(user);
//        basket.setNotebookClass(notebook);
//        basketRepository.save(basket);
//        model.addAttribute("message_basket", "");
//        return "/products/notebook/notebooks_list";
//    }

//    @PostMapping("/notebooks/list")
//    @RequestMapping(params = "price_from")
//    public String returnByPrinceFrom(@RequestParam(name = "price_from") double price_from, Model model, Principal principal) {
//        System.out.println(price_from);
//        System.out.println(123);
//        Usr user = getUserByPrincipal(principal);
//        model.addAttribute("check", user.getUsername() != null);
//        Iterable<Notebook> notebooks = notebookRepository.findAllByFromAndToPrice(0, price_from);
//        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
//        model.addAttribute("notebooks", notebooks);
//        model.addAttribute("counts", counts);
//        return "/products/notebook/notebooks_list";
//    }
//    @PostMapping("/notebooks/list")
//    @ResponseBody
//    public String returnByPBrand(@RequestParam(name = "brand") String brand, Model model, Principal principal) {
//        Usr user = getUserByPrincipal(principal);
//        model.addAttribute("check", user.getUsername() != null);
//        Iterable<Notebook> notebooks = notebookRepository.findByBrand("HP");
//        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
//        model.addAttribute("notebooks", notebooks);
//        model.addAttribute("counts", counts);
//        return "/products/notebook/notebooks_list";
//    }

    //      Вывод каждого отдельного ноутбука
    @GetMapping("/notebook/{id}")
    public String notebookInfo(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        Optional<Notebook> notebook = notebookRepository.findById(id);
        ArrayList<Notebook> result = new ArrayList<>();
        notebook.ifPresent(result::add);
        model.addAttribute("product", result);
        model.addAttribute("name", result.get(0).getName().split("/")[0]);
        List<String> description = Arrays.asList(result.get(0).getDescription().split("\n"));
        List<String> descriptionOut = new ArrayList<>();
        description.forEach(item -> {
            if (item.length() > 1) descriptionOut.add(item);
        });
        model.addAttribute("description", descriptionOut);
        return "/products/notebook/notebook-info";
    }

//    @PostMapping("/notebook/{id}")
//    public String saveNotebook(Model model, Principal principal, Notebook notebook) {
//        Usr user = getUserByPrincipal(principal);
//        Basket basket = new Basket();
//        basket.setUserClass(user);
//        basket.setNotebookClass(notebook);
//        basketRepository.save(basket);
//        model.addAttribute("message_basket", "");
//        return "/products/notebook/notebooks_list";
//    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
