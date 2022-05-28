package com.thesis.computer_workshop.models.reviews;

import com.thesis.computer_workshop.models.users.ImageUser;
import com.thesis.computer_workshop.models.users.Usr;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String userUsername;
    @Column(length = 3000)
    private String message;
    private int rating;
    private boolean anonymous;

    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
}
