package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.Notebook;
import com.thesis.computer_workshop.models.images.ImageNoteBook;
import com.thesis.computer_workshop.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AdminController {
    @Autowired
    public NotebookRepository notebookRepository;

    // ADMIN
    @GetMapping("/admin")
    public String returnAdminCatalog(Model model) {
        return "/admin/catalog_for_edit";
    }

    // НОУТБУКИ
    @GetMapping("/admin/notebook")
    public String returnAdminNotebook(Model model) {
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        model.addAttribute("products", notebooks);
        return "/admin/admin_products/notebook/edit_notebook";
    }

    @PostMapping("/admin/notebook")
    public String setAdminNotebook(@RequestParam String name, Notebook notebook, Model model,
                                   @RequestParam("file1") MultipartFile file1,
                                   @RequestParam("file2") MultipartFile file2,
                                   @RequestParam("file3") MultipartFile file3,
                                   @RequestParam("file4") MultipartFile file4,
                                   @RequestParam("file5") MultipartFile file5,
                                   @RequestParam("file6") MultipartFile file6) throws IOException {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        notebook.setDateTimeCreate(Timestamp.valueOf(currentTime));

        ImageNoteBook image1 = addImage(notebook, file1),
                image2 = addImage(notebook, file2),
                image3 = addImage(notebook, file3),
                image4 = addImage(notebook, file4),
                image5 = addImage(notebook, file5),
                image6 = addImage(notebook, file6);
        image1.setPreviewImage(true);

        Notebook notebookFromDB = notebookRepository.save(notebook);
        notebookFromDB.setPreviewImageId(notebookFromDB.getImageNoteBooksList().get(0).getId());
        notebookRepository.save(notebook);

        return "/admin/admin_products/notebook/edit_notebook";
    }

    private ImageNoteBook toImageEntity(MultipartFile file) throws IOException {
        ImageNoteBook image = new ImageNoteBook();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    private ImageNoteBook addImage(Notebook notebook, MultipartFile file) throws IOException {
        ImageNoteBook image = new ImageNoteBook();
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            notebook.addImageToNotebook(image);
        }
        return image;
    }
}
