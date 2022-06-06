package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.Variables;
import com.thesis.computer_workshop.models.application.RepairApplication;
import com.thesis.computer_workshop.models.buy.CompletedApplication;
import com.thesis.computer_workshop.models.mail.MaiLSender;
import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.basketRepositories.BasketRepository;
import com.thesis.computer_workshop.repositories.completedApplicationRepositories.CompletedApplicationRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;

@Controller
public class BasketController {
    @Autowired
    public BasketRepository basketRepository;
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public CompletedApplicationRepository completedApplicationRepository;
    @Autowired
    private MaiLSender maiLSender;

    @GetMapping("/basket")
    public String returnAdminCatalog(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("user", user);
        model.addAttribute("counts", counts);
        return "/basket/user_basket";
    }

    @PostMapping("/basket")
    public String postMessageForBuyAll(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "numberPhone") String numberPhone,
            @RequestParam(name = "comment") String comment,
            Model model, Principal principal) {
        String way_to_get = "самовывоз";
        String payment_method = "наличными";
        int sumProduct = 112500;
        String listBuyProduct = "Принтер лазерный HP LaserJet PRO M104a (6000 р.)" + "\n" +
                "Ноутбук Acer NITROAn515-55 (84000 р.)" + "\n" +
                "Компьютер MacMini A1347 (22500 р.)";
        Usr user = getUserByPrincipal(principal);
        CompletedApplication completedApplication = new CompletedApplication();
        completedApplication.setName(name);
        completedApplication.setEmail(email);
        completedApplication.setComment(comment);
        completedApplication.setUsername(user.getUsername());
        completedApplication.setPayment_method(payment_method);
        completedApplication.setWay_to_get(way_to_get);
        completedApplication.setListBuyProduct(listBuyProduct);
        completedApplicationRepository.save(completedApplication);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String dateMessage = completedApplication.getDateOfCreated().toString().split("T")[0];
            String timeMessage = completedApplication.getDateOfCreated().toString().split("T")[1].split("\\.")[0];

            String messageClient = "Добрый день, " + user.getName()
                    + "!\nСпасибо за совершённую вами покупку.\n"
                    + "\nВаша заявка содержит следующие товары:\n" + "-".repeat(50)
                    + "\n\t *" + String.join("\n\t* ", listBuyProduct.split("\n"))
                    + "\nОбщая сумма товаров: " + sumProduct + " рублей"
                    + "\nВыбранный способ оплаты: \"" + payment_method + "\""
                    + "\nВыбранный способ получения: \"" + way_to_get + "\""
                    + "\n" + "-".repeat(50)
                    + "\nДата оформления: " + dateMessage + " и время оформления " + timeMessage
                    + "\n\nПо всем вопросам обращайтесь по номеру: " + Variables.companyNumber
                    + "\nИли напишите на нашу почту: " + "sar.comp.workshop@gmail.com"
                    + "\nМы находимся: Саратов, ул. Сакко и Ванцетти, д.47."
                    + "\nРаботаем Пн-Пт 9:00-18:00 (Сб, Вс - выходные)";
            System.out.println(messageClient);
            System.out.println();
            maiLSender.send(user.getEmail(), "Покупка товаров в \"" + Variables.companyName + "\" от " + dateMessage + " " + timeMessage, messageClient);

            String messageShop = "Заявка от " + user.getName()
                    + "\nEmail: " + user.getEmail()
                    + "\nНомер телефона: " + numberPhone
                    + "\nЗаявка содержит следующие товары:\n" + "-".repeat(50)
                    + "\n\t * " + String.join("\n\t * ", listBuyProduct.split("\n"))
                    + "\nОбщая сумма товаров: " + sumProduct + " рублей"
                    + "\nВыбранный способ оплаты: \"" + " наличными" + "\""
                    + "\nВыбранный способ получения: \"" + "самовывоз" + "\""
                    + "\nДата оформления: " + dateMessage + " и время оформления " + timeMessage;
            System.out.println(messageShop);
            maiLSender.send(Variables.companyEmail, "Заявка на покупку товаров от " + dateMessage + " " + timeMessage, messageShop);
        }
        return "/basket/user_basket";
    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
