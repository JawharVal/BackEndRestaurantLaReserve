package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Set;

@Data
@NoArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private String section;
    private String imageUrl;
    private Double price;
    private Set<Long> bookingIds;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private String categoryName;
    private Long categoryId;
}
