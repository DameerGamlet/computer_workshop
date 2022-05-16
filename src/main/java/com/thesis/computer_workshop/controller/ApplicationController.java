package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.application.RepairApplication;
import com.thesis.computer_workshop.models.images.ImagesApplication;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RepairApplicationRepository repairApplicationRepository;

    @GetMapping("/application")
    public String applicationPage(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        model.addAttribute("user_name", user.getName());
        return "applications/application_form";
    }

    @PostMapping("/application")
    public String setApplication(Principal principal,
                                 Model model,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "title") String title,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam("file1") MultipartFile file1,
                                 @RequestParam("file2") MultipartFile file2,
                                 @RequestParam("file3") MultipartFile file3,
                                 @RequestParam("file4") MultipartFile file4,
                                 @RequestParam("file5") MultipartFile file5,
                                 @RequestParam("file6") MultipartFile file6) throws IOException {
        System.out.println(title);
        RepairApplication repairApplication = new RepairApplication();
        ImagesApplication image1 = addImage(repairApplication, file1),
                image2 = addImage(repairApplication, file2),
                image3 = addImage(repairApplication, file3),
                image4 = addImage(repairApplication, file4),
                image5 = addImage(repairApplication, file5),
                image6 = addImage(repairApplication, file6);
        Usr user = getUserByPrincipal(principal);
        repairApplication.setTitle(title);
        repairApplication.setDescription(description);
        repairApplication.setUserName(name);
        repairApplication.setUserEmail(user.getEmail());
        repairApplication.setUserNumber(user.getPhoneNumber());
        repairApplication.setUserUsername(user.getUsername());

        repairApplicationRepository.save(repairApplication);

        return "redirect:/";
    }

    private ImagesApplication toImageEntity(MultipartFile file) throws IOException {
        ImagesApplication image = new ImagesApplication();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    private ImagesApplication addImage(RepairApplication repairApplication, MultipartFile file) throws IOException {
        ImagesApplication image = new ImagesApplication();
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            repairApplication.addImageApplication(image);
        }
        return image;
    }


    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
