package com.example.demo.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BookingDTO {
    private Long id;
    private String date;
    private String time;
    private int numberOfPersons;
    private String firstName;
    private String lastName;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    private Long userId;
    private String comment;
    private Long restaurantId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    private Set<Long> menuItemIds = new HashSet<>();
    // Getters and Setters
    private Long categoryId;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(Set<Long> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }

}
