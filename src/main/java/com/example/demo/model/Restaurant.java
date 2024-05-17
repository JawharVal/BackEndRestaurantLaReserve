package com.example.demo.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer restaurantId) {
        this.Id = restaurantId;
    }

    @Column(name = "Name")
    private String name;

    @OneToOne
    @JoinColumn(name = "LocationID")
    private Location location;

    @OneToMany(mappedBy = "restaurant")
    private Set<Booking> bookings = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }



    @Column(name = "Cuisine")
    private String cuisine;

    @Column(name = "Capacity")
    private int capacity;

    // Constructors
    public Restaurant() {
    }

}