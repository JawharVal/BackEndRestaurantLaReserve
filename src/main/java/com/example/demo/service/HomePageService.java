package com.example.demo.service;

import com.example.demo.dto.HomePageDto;

import java.util.List;

public interface HomePageService {
    List<HomePageDto> getHomePages();
    HomePageDto getHomePageById(Long id);
    HomePageDto addHomePage(HomePageDto newHomePageDto);
    HomePageDto updateHomePage(HomePageDto updatedHomePageDto);
}
