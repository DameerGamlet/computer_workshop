package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.application.RepairApplication;
import com.thesis.computer_workshop.models.mail.MaiLSender;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.applicationRepositories.RepairApplicationRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RepairApplicationRepository repairApplicationRepository;
    @Autowired
    private MaiLSender maiLSender;

    @GetMapping("/application")
    public String applicationPage(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        model.addAttribute("user_name", user.getName());
        model.addAttribute("user_email", user.getEmail());
        model.addAttribute("user_number", user.getPhoneNumber());
        return "applications/application_form";
    }

//    @GetMapping("/application_2")
//    public String saveApplication(@RequestParam(name = "category") String category,
//                                  @RequestParam(name = "email") String email,
//                                  @RequestParam(name = "description") String description,
//                                  @RequestParam(name = "number") String number,
//                                  @RequestParam(name = "address") String address,
//                                  @RequestParam(name = "data") String data,
//
//                                  Principal principal, Model model) {
//        String character = "Работает, но с зависаниями. Самопроизвольно выключается. Сильно нагревается. Сильно гудит вентилятор.";
//        String location = "В мастерской магазина услуг.";
//        System.out.println(
//                "category" + " = " + category + "\n" +
//                        "character" + " = " + character + "\n" +
//                        "description" + " = " + description + "\n" +
//                        "email" + " = " + email + "\n" +
//                        "number" + " = " + number + "\n" +
//                        "address" + " = " + address + "\n" +
//                        "data" + " = " + data + "\n" +
//                        "location" + " = " + location + "\n"
//        );
//        Usr user = getUserByPrincipal(principal);
//        model.addAttribute("check", user.getUsername() != null);
//        model.addAttribute("user", user);
//        System.out.println("category: " + category);
//        return "main/main";
//    }

    @PostMapping("/application")
    public String setApplication(Principal principal,
                                 Model model,
                                 @RequestParam(name = "category") String category,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "number") String number,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "data") String data) throws IOException {
        RepairApplication repairApplication = new RepairApplication();
        System.out.println(123);
//        ImagesApplication image1 = addImage(repairApplication, file1),
//                image2 = addImage(repairApplication, file2),
//                image3 = addImage(repairApplication, file3);
        Usr user = getUserByPrincipal(principal);

        String character = "Работает, но с зависаниями. Самопроизвольно выключается. Сильно нагревается. Сильно гудит вентилятор.";
        String location = "В мастерской магазина услуг.";
        repairApplication.setUsername(user.getUsername());
        repairApplication.setName(user.getName());
        repairApplication.setEmail(user.getEmail());
        repairApplication.setAddress(address);
        repairApplication.setCategory(category);
        repairApplication.setDescription(description);
        repairApplication.setCharacter(character);
        repairApplication.setLocation(location);
        repairApplication.setData(data);
        repairApplicationRepository.save(repairApplication);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String dateMessage = repairApplication.getDateOfCreated().toString().split("T")[0];
            String timeMessage = repairApplication.getDateOfCreated().toString().split("T")[1].split("\\.")[0];
            String messageClient = "Добрый день, " + user.getName()
                    + "!\nСпасибо за вашу заявку на нашем сервисе\n"
                    + "\nВы оставили следующую заявку:\n" + "-".repeat(50)
                    + "\nСломался: \"" + category + "\""
                    + "\nХарактер ошибки следующий:\n"
                    + String.join("\n\t", character.split("\\."))
                    + "\nДата оформления: " + dateMessage +
                    " и время оформления " + timeMessage;
            maiLSender.send(user.getEmail(), "Заявка на услуга ремонта от " + dateMessage + " " + timeMessage, messageClient);

            String messageShop = "Заявка от " + user.getName() + "\nEmail: " + user.getEmail()
                    + "\nНомер телефона: " + number
                    + "\nСломался " + category
                    + "\nОписание: " + description
                    + "\nХарактер ошибки следующий:\n\t"
                    + String.join("\n\t", character.split("\\."));
            maiLSender.send("sar.comp.workshop@gmail.com", "Заявка на услуга ремонта от " + dateMessage + " " + timeMessage, messageShop);
        }
        return "applications/application_form";
    }

//    private ImagesApplication toImageEntity(MultipartFile file) throws IOException {
//        ImagesApplication image = new ImagesApplication();
//        image.setName(file.getName());
//        image.setOriginalFileName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setSize(file.getSize());
//        image.setBytes(file.getBytes());
//        return image;
//    }
//
//    private ImagesApplication addImage(RepairApplication repairApplication, MultipartFile file) throws IOException {
//        ImagesApplication image = new ImagesApplication();
//        if (file.getSize() != 0) {
//            image = toImageEntity(file);
//            repairApplication.addImageApplication(image);
//        }
//        return image;
//    }


    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
