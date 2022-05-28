package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.products.UsbFlashProduct;
import com.thesis.computer_workshop.models.reviews.Review;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.UsbFlashRepository;
import com.thesis.computer_workshop.repositories.reviewRepositories.ReviewRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    public UsbFlashRepository usbFlashRepository;
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ReviewRepository reviewRepository;

//   КАТАЛОГ

    @GetMapping("/shop")
    public String returnShop(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "/main/shop";
    }

    @GetMapping("/shop/peripherals")
    public String returnCatPeripherals(Model model) {
        return "/catalog/cat_peripherals";
    }

//    ФЛЕШКИ

    @GetMapping("/usb/list")
    public String returnAllUsbFlash(Model model) {
        Iterable<UsbFlashProduct> products = usbFlashRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("counts", products.spliterator().getExactSizeIfKnown());
        return "/products/usb/usb_flash_list";
    }

    @GetMapping("/usb/create")
    public String getCreateUsb(UsbFlashProduct usbFlashProduct, Model model) {
        if (usbFlashProduct.getName() != null) {
            System.out.println("_" + usbFlashProduct.getName() + "_");
            usbFlashRepository.save(usbFlashProduct);
        }
        model.addAttribute("products", usbFlashRepository.findAll());
        return "/products/usb/add_usb";
    }

    @PostMapping("/usb/create")
    public String createUsb(UsbFlashProduct usbFlashProduct) {
        usbFlashRepository.save(usbFlashProduct);
        return "redirect:/usb/create";
    }

    @PostMapping("/usb/delete/{id}")
    public String deleteUsb(Long id) {
        usbFlashRepository.deleteById(id);
        return "redirect:/list";
    }

    @GetMapping("/usb/{id}")
    public String usbInfo(@PathVariable(value = "id") Long id, Model model) {
        Optional<UsbFlashProduct> usb = usbFlashRepository.findById(id);
        ArrayList<UsbFlashProduct> result = new ArrayList<>();
        usb.ifPresent(result::add);
        result.forEach(System.out::println);
        model.addAttribute("product", result);
        return "/products/usb/usb-info";
    }

    // МОНИТОРЫ
    @GetMapping("/monitor/list")
    public String returnAllMonitors(Model model) {
        return "/products/monitors/monitor-list";
    }

    //    Прочие страницы (фиксированные) для пользователей
    @GetMapping("/")
    public String returnMain(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "/main/main";
    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }

    @GetMapping("/location")
    public String returnLocation(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "/main/location";
    }

    @GetMapping("/contacts")
    public String returnContacts(Model model) {
        return "/main/contacts";
    }

    @GetMapping("/reviews")
    public String returnReviews(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        Iterable<Review> reviewIterable = reviewRepository.findAll();
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("reviews", reviewIterable);
        return "/main/reviews";
    }

    @PostMapping("/reviews")
    public String saveReview(@RequestParam(name = "name") String name,  Review review, Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        review.setAnonymous(false);
        review.setUserName(name);
        review.setUserUsername(user.getUsername());
        reviewRepository.save(review);
        return "/main/reviews";
    }

    @GetMapping("/interesting_articles")
    public String returnInterestingArticles(Model model) {
        return "/main/interesting_articles";
    }

    @GetMapping("/about_company")
    public String returnAboutCompany(Model model) {
        return "/main/about_company";
    }

    @GetMapping("/feedback")
    public String returnFeedback(Model model) {
        return "/main/feedback";
    }
}
