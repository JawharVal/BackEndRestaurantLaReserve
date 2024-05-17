package com.example.demo.service;

import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.repositories.CityRepository;
import com.example.demo.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    @Override
    public City save(City city, Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found with ID: " + countryId));
        city.setCountry(country);
        return cityRepository.save(city);
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
