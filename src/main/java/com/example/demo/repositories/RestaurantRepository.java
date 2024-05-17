package com.example.demo.repositories;

import com.example.demo.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.location")
    List<Restaurant> findAllRestaurantsWithLocations();
}