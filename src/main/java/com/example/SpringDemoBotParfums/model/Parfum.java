package com.example.SpringDemoBotParfums.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parfums_table")
public class Parfum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "img")
    private String img;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "article")
    private String article;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    // Конструктор по умолчанию (нужен для Hibernate)
    // public Parfum() {
    // }
    // Конструктор
}


