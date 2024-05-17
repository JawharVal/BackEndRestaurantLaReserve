package com.example.demo.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int foodRating;
    private int serviceRating;
    private int ambienceRating;
    private int valueForMoneyRating;
    private int cleanlinessRating;
    private String comments;
    private boolean approved = false;
    // Getters and Setters
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    public double calculateAverageRating() {
        int totalRatings = foodRating + serviceRating + ambienceRating + valueForMoneyRating + cleanlinessRating;
        return totalRatings / 5.0; // Since there are 5 categories
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime reviewDate;

    @PrePersist
    protected void onCreate() {
        reviewDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        reviewDate = LocalDateTime.now();
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
