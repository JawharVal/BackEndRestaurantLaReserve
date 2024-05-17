package com.example.demo.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
public class HomePage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String images;
    private String description;
    private String buttonTexts;
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}
