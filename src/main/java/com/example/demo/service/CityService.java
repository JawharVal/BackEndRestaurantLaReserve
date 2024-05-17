package com.example.demo.service;

import com.example.demo.model.City;

import java.util.List;

public interface CityService {
    List<City> findAll();
    City findById(Long id);
    City save(City city, Long countryId);
    void deleteById(Long id);
}