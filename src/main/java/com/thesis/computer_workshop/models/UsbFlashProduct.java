package com.thesis.computer_workshop.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "usb")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UsbFlashProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;              //ID
    private String name;             //название
    private String model;            //модель
    private double price;            //цена
    private int warranty;            //гарантия
    private String country_origin;   //страна производства
    private int volume;              //объём
    private double max_wr_speed;     //максимальная скорость записи данных
    private double max_rd_speed;     //максимальная скорость чтения данных
    private int counts;
    private String description;
}