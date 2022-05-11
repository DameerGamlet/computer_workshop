package com.thesis.computer_workshop.models.products;

import com.sun.istack.NotNull;
import com.thesis.computer_workshop.models.images.ImageNoteBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;            // наименование товара
    private String brand;           // марка товара (lenovo, HP и так далее)
    @NotNull
    private double price;           // цена
    @Column(length = 3000)          // ограничение по длине описания
    private String description;     // описание
    private String status;          // новый или же БУ
    private boolean inStock;        // товар в наличии или же нет
    private String display;             // дисплей. Пример - "матрица /  15.6 / FullHD / матовая"
    private String processorName;       // процессор. Пример - "i5-6200U / кол. ядер / кол. потоков"
    private String videoCardName;       // видеокарта. Пример - "Intel HD Graphics 520 / AMD Radeon R5 M330 2Gb"
    private int ramSize;                // ОП. Количество памяти. Пример - "8" в ГБ (конвертация в МБ)
    private String memoryDrives;        // ЖД или ГД. Пример, "SSD120Gb / HDD 1000 Gb"
    private String batteryLife;         // время работы от батареи. Пример, "90 - 120" в минутах (конвертация в часы)
    private String osName;              // установленная система. Пример, "Windows 10 Pro X64 лицензия"
    private String termOfUse;           // время использования. Пример, "36" в месяцах
    private Timestamp dateTimeCreate;   // дата создания товара

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notebook")
    private List<ImageNoteBook> imageNoteBooksList = new ArrayList<>();
    private Long previewImageId;

    public void addImageToNotebook(ImageNoteBook imageNoteBook){
        imageNoteBook.setNotebook(this);
        imageNoteBooksList.add(imageNoteBook);
    }
}
