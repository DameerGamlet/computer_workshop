package com.thesis.computer_workshop.models.users;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usr{
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String username;     // логин (нужна для авторизации)
    @Column(length = 100)
    private String name;         // имя пользователя - используется при обращении
    @Column(length = 100)
    private String email;        // имя пользователя - используется при обращении
    @Column(length = 12)
    private String phoneNumber;  // номер телефона - необязательная часть
    private boolean active;      //признак активности
    @NotNull
    private String password;     // пароль пользователя

    private String activationCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime dateOfCreated;  // дата создания пользователя

    // добавление аватара пользователя
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private ImageUser avatar;

    // метод начальной инициализации - установить дату создания пользователя
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
}
