package com.thesis.computer_workshop.services;

import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotebookService {
    private final NotebookRepository notebookRepository;

    public List<Notebook> listProducts(String keyword){
        if(keyword != null && !keyword.equals("")) return notebookRepository.findAllByKeyword(keyword);
        return notebookRepository.findAll();
    }
}
