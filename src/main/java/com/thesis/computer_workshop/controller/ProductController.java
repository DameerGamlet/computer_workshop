package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.Notebook;
import com.thesis.computer_workshop.models.UsbFlashProduct;
import com.thesis.computer_workshop.repositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.UsbFlashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProductController {
    @Autowired
    public UsbFlashRepository usbFlashRepository;

    @Autowired
    public NotebookRepository notebookRepository;

    @GetMapping("/notebooks/list")
    public String returnAllNoteBooks(Model model) {
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("counts", counts);
        return "/products/notebook/notebooks_list";
    }

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
        ArrayList <UsbFlashProduct> result = new ArrayList<>();
        usb.ifPresent(result::add);
        result.forEach(System.out::println);
        model.addAttribute("product", result);
        return "/products/usb/usb-info";
    }

    @GetMapping("/notebook/{id}")
    public String notebookInfo(@PathVariable(value = "id") Long id, Model model) {
        Optional<Notebook> notebook = notebookRepository.findById(id);
        ArrayList <Notebook> result = new ArrayList<>();
        notebook.ifPresent(result::add);
        result.forEach(System.out::println);
        model.addAttribute("product", result);
        return "/products/notebook/notebook-info";
    }

    @GetMapping("/")
    public String returnMain(Model model) {
        return "/main/main";
    }

    @GetMapping("/location")
    public String returnLocation(Model model) {
        return "/main/location";
    }

    @GetMapping("/shop")
    public String returnShop(Model model) {
        return "/main/shop";
    }

    @GetMapping("/contacts")
    public String returnContacts(Model model) {
        return "/main/contacts";
    }

    @GetMapping("/reviews")
    public String returnReviews(Model model) {
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

    // ADMIN
    @GetMapping("/admin")
    public String returnAdminCatalog(Model model) {
        return "/admin/catalog_for_edit";
    }

    @GetMapping("/admin/notebook")
    public String returnAdminNotebook(Model model) {
        return "/admin/admin_products/notebook/edit_notebook";
    }

    @PostMapping("/admin/notebook")
    public String setAdminNotebook(@RequestParam String name, Notebook notebook, Model model) {
        System.out.println(name);
        Date dt = new java.util.Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        notebook.setDateTimeCreate(Timestamp.valueOf(currentTime));
        notebookRepository.save(notebook);
        return "/admin/admin_products/notebook/edit_notebook";
    }

}
