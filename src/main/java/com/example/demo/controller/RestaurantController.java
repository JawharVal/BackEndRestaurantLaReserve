package com.example.demo.controller;

import com.example.demo.dto.RestaurantDTO;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // POST method to create a new restaurant
    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        System.out.println("Received location ID: " + restaurantDTO.getLocationId()); // This will confirm what you are receiving
        try {
            RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurantDTO);
            return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println("Error creating restaurant: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // PUT method to update an existing restaurant
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Integer id, @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(id, restaurantDTO);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    // DELETE method to delete a restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Integer id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }

    // GET method to retrieve a restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Integer id) {
        RestaurantDTO restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    // GET method to retrieve all restaurants
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
}
