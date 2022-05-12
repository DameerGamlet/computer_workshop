package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.logs.LogProduct;
import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.models.images.ImageNoteBook;
import com.thesis.computer_workshop.repositories.logsRepositories.LogProductRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.imagesRepositories.ImageNoteBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public ImageNoteBookRepository imageNoteBookRepository;
    @Autowired
    public LogProductRepository logProductRepository;

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
    public String setAdminNotebook(@RequestParam String name,
                                   Notebook notebook,
                                   Model model,
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

        System.out.println("Добавлен товар \"" + notebook.getName()
                + "\" ценой " + notebook.getPrice() + " (" + notebook.getDateTimeCreate() + ")");

        LogProduct newProduct = new LogProduct();
        newProduct.setCategory("Ноутбук");
        newProduct.setAction("Добавление товара");
        newProduct.setIdProduct(notebook.getId());
        newProduct.setNameProduct(notebook.getName());
        newProduct.setDescription("Добавлен товар \"" + notebook.getName() + "\" ценой " + notebook.getPrice() + " (" + notebook.getDateTimeCreate() + ")");
        logProductRepository.save(newProduct);

        return "/admin/admin_products/notebook/edit_notebook";
    }

    @GetMapping("/admin/notebook/{id}/edit")
    public String returnEditNotebook(@PathVariable(value = "id") long id, Model model) {
        if(!notebookRepository.existsById(id)){
            return "/admin/admin_products/notebook/update_notebook";
        }
        Optional<Notebook> notebooks = notebookRepository.findById(id);
        ArrayList<Notebook> result = new ArrayList<>();
        notebooks.ifPresent(result::add);
        model.addAttribute("notebook", result);
        return "/admin/admin_products/notebook/update_notebook";
    }

    @PostMapping("/admin/notebook/{id}/edit")
    public String setAdminNotebook(@PathVariable(value = "id") long id,
                                   @RequestParam String name, Notebook notebook, Model model,
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

        Notebook notebookFromDB = notebookRepository.findById(id).orElseThrow();
        notebookRepository.save(notebook);
        notebookFromDB.setPreviewImageId(notebookFromDB.getImageNoteBooksList().get(0).getId());
        notebookRepository.save(notebook);

        return "redirect:/admin/notebook";
    }

    @PostMapping("/admin/notebook/{id}/delete")
    public String deleteNotebookById(@PathVariable(value = "id") long id, Model model) throws IOException {
        Notebook notebook = notebookRepository.findById(id).orElseThrow();
        notebookRepository.delete(notebook);
        return "redirect:/admin/notebook";
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
