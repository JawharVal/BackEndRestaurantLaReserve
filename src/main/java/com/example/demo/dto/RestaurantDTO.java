package com.example.demo.dto;

import java.util.Set;

public class RestaurantDTO {

    private int id;
    private String name;
    private String locationName;
    private String cuisine;
    private int capacity;

    private Long locationId;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    private Set<Long> bookingIds;

    // Constructors, Getters, and Setters
    public RestaurantDTO() {
    }

    public RestaurantDTO(int id, String name, String locationName, String cuisine, int capacity) {
        this.id = id;
        this.name = name;
        this.locationName = locationName;
        this.cuisine = cuisine;
        this.capacity = capacity;
    }

    // Standard getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Long> getBookingIds() {
        return bookingIds;
    }

    public void setBookingIds(Set<Long> bookingIds) {
        this.bookingIds = bookingIds;
    }
}
