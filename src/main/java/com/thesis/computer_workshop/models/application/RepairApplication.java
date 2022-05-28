package com.thesis.computer_workshop.models.application;

//import com.sun.istack.NotNull;
//import com.thesis.computer_workshop.models.images.ImageNoteBook;
//import com.thesis.computer_workshop.models.images.ImagesApplication;
//import com.thesis.computer_workshop.models.users.ImageUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepairApplication {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String email;
    @Column(length = 1000)          // ограничение по длине описания
    private String address;
    private String category;
    @Column(length = 3000)          // ограничение по длине описания
    private String description;
    @Column(name = "characterDB")
    private String character;
    @Column(name = "locationDB")
    private String location;
    @Column(name = "dataDB")
    private String data;
    private LocalDateTime dateOfCreated;  // дата создания пользователя

    // метод начальной инициализации - установить дату создания пользователя
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "repairApplication")
//    private List<ImagesApplication> imagesApplicationArrayList = new ArrayList<>();
//
//    public void addImageApplication(ImagesApplication imagesApplication) {
//        imagesApplication.setRepairApplication(this);
//        imagesApplicationArrayList.add(imagesApplication);
//    }
}
