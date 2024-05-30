package com.example.demo.service;

import com.example.demo.dto.RestaurantDTO;
import com.example.demo.model.Location;
import com.example.demo.model.Restaurant;
import com.example.demo.repositories.LocationRepository;
import com.example.demo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private LocationRepository locationRepository;


    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setLocationName(restaurant.getLocation() != null ? restaurant.getLocation().getAddress() : "No Location Specified");
        restaurantDTO.setCuisine(restaurant.getCuisine());
        restaurantDTO.setCapacity(restaurant.getCapacity());
        return restaurantDTO;
    }



    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setCuisine(restaurantDTO.getCuisine());
        restaurant.setCapacity(restaurantDTO.getCapacity());

        if (restaurantDTO.getLocationId() != null) {
            Location location = locationRepository.findById(restaurantDTO.getLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid location ID: " + restaurantDTO.getLocationId()));
            restaurant.setLocation(location);
        } else {
            throw new IllegalArgumentException("Location ID must not be null");
        }

        restaurant = restaurantRepository.save(restaurant);
        return convertToDTO(restaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(Integer id, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found for id: " + id));

        restaurant.setName(restaurantDTO.getName());
        restaurant.setCuisine(restaurantDTO.getCuisine());
        restaurant.setCapacity(restaurantDTO.getCapacity());

        Location location = locationRepository.findByAddress(restaurantDTO.getLocationName())
                .orElseThrow(() -> new IllegalArgumentException("Location not found with name: " + restaurantDTO.getLocationName()));
        restaurant.setLocation(location);

        restaurant = restaurantRepository.save(restaurant);
        return convertToDTO(restaurant);
    }

    @Override
    public void deleteRestaurant(Integer id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public RestaurantDTO getRestaurantById(Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found for id: " + id));
        return convertToDTO(restaurant);
    }

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAllRestaurantsWithLocations();
        return restaurants.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
