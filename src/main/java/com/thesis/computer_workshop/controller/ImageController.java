package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.models.images.ImageNoteBook;
import com.thesis.computer_workshop.repositories.imagesRepositories.ImageNoteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private  final ImageNoteBookRepository imageNoteBookRepository;

    @GetMapping("/images/notebook/{id}")
    private  ResponseEntity<?> getImageById(@PathVariable Long id){
        ImageNoteBook imageNoteBook = imageNoteBookRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("filename", imageNoteBook.getOriginalFileName())
                .contentType(MediaType.valueOf(imageNoteBook.getContentType()))
                .contentLength(imageNoteBook.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(imageNoteBook.getBytes())));
    }
}
