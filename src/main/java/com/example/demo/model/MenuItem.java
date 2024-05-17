package com.example.demo.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String description;
    private String imageUrl;
    private String section;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
