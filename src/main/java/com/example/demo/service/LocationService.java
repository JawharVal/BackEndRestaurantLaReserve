package com.example.demo.service;

import com.example.demo.dto.LocationDTO;
import com.example.demo.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    Location findById(Long id);
    Location saveLocation(LocationDTO locationDTO);
    Location saveLocation(Location location);
    void deleteById(Long id);
}