package com.thesis.computer_workshop.models.images;

import com.thesis.computer_workshop.models.products.Notebook;
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
public class ImageNoteBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String originalFileName;
    private Long size;
    private String contentType;
    private boolean isPreviewImage;
    @Lob
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) // REFRESH - обновить
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;
}
