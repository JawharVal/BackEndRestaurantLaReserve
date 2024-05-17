package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HomePageDto {
    private Long id;
    private String title;
    private List<String> imageUrls;
    private List<String> descriptions;
    private List<String> buttonTexts;
    private String link;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
