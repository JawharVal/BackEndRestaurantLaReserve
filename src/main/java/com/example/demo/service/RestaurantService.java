package com.example.demo.service;

import com.example.demo.dto.RestaurantDTO;
import java.util.List;

public interface RestaurantService {
    List<RestaurantDTO> getAllRestaurants();
    RestaurantDTO getRestaurantById(Integer id);
    RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO);
    RestaurantDTO updateRestaurant(Integer id, RestaurantDTO restaurantDTO);
    void deleteRestaurant(Integer id);
}
