package com.thesis.computer_workshop.models.logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    private Long idProduct;
    private String nameProduct;
    private String action;
    @Column(length = 3000)          // ограничение по длине описания
    private String description;
}
