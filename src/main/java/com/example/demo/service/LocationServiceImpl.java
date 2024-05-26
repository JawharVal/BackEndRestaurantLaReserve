package com.example.demo.service;

import com.example.demo.dto.LocationDTO;
import com.example.demo.model.City;
import com.example.demo.model.Location;
import com.example.demo.repositories.CityRepository;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Location saveLocation(Location location) {
        if (location.getCity() != null && location.getCity().getCityId() != null) {
            City city = cityRepository.findById(location.getCity().getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found with ID: " + location.getCity().getCityId()));
            location.setCity(city);
        } else {
            throw new RuntimeException("City information is missing");
        }
        return locationRepository.save(location);
    }

    @Override
    public Location saveLocation(LocationDTO locationDTO) {
        // Conversion from DTO to entity happens here
        Location location = convertToEntity(locationDTO);
        return locationRepository.save(location);
    }

    private Location convertToEntity(LocationDTO locationDTO) {
        Location location = new Location();
        location.setAddress(locationDTO.getAddress());
        location.setPostalCode(locationDTO.getPostalCode());
        if (locationDTO.getCityId() != null) {
            City city = cityRepository.findById(locationDTO.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found"));
            location.setCity(city);
        }
        return location;
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + id));
    }

    @Override
    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }
}
