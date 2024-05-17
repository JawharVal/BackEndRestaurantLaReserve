package com.example.demo.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private int foodRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private boolean approved;

    // Getters and Setters
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private int serviceRating;
    private int ambienceRating;
    private int valueForMoneyRating;
    private int cleanlinessRating;
    private String comments;

    public int getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(int foodRating) {
        this.foodRating = foodRating;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getAmbienceRating() {
        return ambienceRating;
    }

    public void setAmbienceRating(int ambienceRating) {
        this.ambienceRating = ambienceRating;
    }

    public int getValueForMoneyRating() {
        return valueForMoneyRating;
    }

    public void setValueForMoneyRating(int valueForMoneyRating) {
        this.valueForMoneyRating = valueForMoneyRating;
    }

    public int getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(int cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    private double averageRating;

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    private LocalDateTime reviewDate;
    private String username;

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}