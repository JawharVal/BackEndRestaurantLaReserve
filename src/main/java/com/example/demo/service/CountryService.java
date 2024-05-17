package com.example.demo.service;

import com.example.demo.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> findAll();
    Country findById(Long id);
    Country save(Country country);
    void deleteById(Long id);
}