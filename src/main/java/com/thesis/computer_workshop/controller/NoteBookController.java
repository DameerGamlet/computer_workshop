package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.Notebook;
import com.thesis.computer_workshop.models.images.ImageNoteBook;
import com.thesis.computer_workshop.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class NoteBookController {
    @Autowired
    public NotebookRepository notebookRepository;

    //    Список ноутбуков
    @GetMapping("/notebooks/list")
    public String returnAllNoteBooks(Model model) {
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("counts", counts);
        ImageNoteBook imageNoteBook = notebooks.iterator().next().getImageNoteBooksList().get(0);
        model.addAttribute("imagePreview", imageNoteBook);
        return "/products/notebook/notebooks_list";
    }

    //      Вывод каждого отдельного ноутбука
    @GetMapping("/notebook/{id}")
    public String notebookInfo(@PathVariable(value = "id") Long id, Model model) {
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
}
