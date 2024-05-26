package com.example.demo.controller;
import com.example.demo.model.City;
import com.example.demo.service.CityService;
import com.example.demo.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

    @GetMapping
    public List<City> getAllCities() {
        return cityService.findAll();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Long id) {
        return cityService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody City city, @RequestParam Long countryId) {
        try {
            City savedCity = cityService.save(city, countryId);
            return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create city: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public City updateCity(@PathVariable Long id, @RequestBody City city,@RequestParam Long countryId) {
        city.setCityId(id);
        return cityService.save(city, countryId);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteById(id);
    }
}