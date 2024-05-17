package com.example.demo.service;

import com.example.demo.dto.HomePageDto;
import com.example.demo.model.HomePage;
import com.example.demo.model.User;
import com.example.demo.repositories.HomePageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private final HomePageRepository repo;

    @Autowired
    public HomePageServiceImpl(HomePageRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<HomePageDto> getHomePages() {
        List<HomePage> homePages = (List<HomePage>) repo.findAll();
        return homePages.stream().map(homePage -> {
            HomePageDto dto = new HomePageDto();
            dto.setId(homePage.getId());
            dto.setTitle(homePage.getTitle());
            dto.setImageUrls(Arrays.asList(homePage.getImages().split(",")));
            dto.setDescriptions(Collections.singletonList(homePage.getDescription()));
            dto.setButtonTexts(Arrays.asList(homePage.getButtonTexts().split(",")));
            dto.setButtonTexts(Arrays.asList(homePage.getButtonTexts().split(",")));
            dto.setLink(homePage.getLink());
            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public HomePageDto addHomePage(HomePageDto newHomePageDto) {
        HomePage homePage = new HomePage();
        homePage.setTitle(newHomePageDto.getTitle());
        homePage.setImages(String.join(",", newHomePageDto.getImageUrls()));
        homePage.setDescription(String.join(",", newHomePageDto.getDescriptions()));
        homePage.setButtonTexts(String.join(",", newHomePageDto.getButtonTexts()));
        homePage.setLink(newHomePageDto.getLink());
        // Set the user_id to 1 by default (assuming user with ID 1 always exists)
        User defaultUser = new User();  // Assuming you have a User class
        defaultUser.setId(1L);  // Set this ID to the user you want to associate
        homePage.setUser(defaultUser);

        HomePage savedHomePage = repo.save(homePage);
        HomePageDto dto = new HomePageDto();
        dto.setId(savedHomePage.getId());
        dto.setTitle(savedHomePage.getTitle());
        dto.setImageUrls(Arrays.asList(savedHomePage.getImages().split(",")));
        dto.setDescriptions(Arrays.asList(savedHomePage.getDescription().split(",")));
        dto.setButtonTexts(Arrays.asList(savedHomePage.getButtonTexts().split(",")));
        dto.setLink(savedHomePage.getLink());
        return dto;
    }

    @Override
    public HomePageDto getHomePageById(Long id) {
        return repo.findById(id).map(homePage -> {
            HomePageDto dto = new HomePageDto();
            dto.setId(homePage.getId());
            dto.setTitle(homePage.getTitle());
            dto.setImageUrls(Arrays.asList(homePage.getImages().split(",")));
            dto.setDescriptions(Collections.singletonList(homePage.getDescription()));
            dto.setButtonTexts(Arrays.asList(homePage.getButtonTexts().split(",")));
            dto.setLink(homePage.getLink());
            return dto;
        }).orElse(null);
    }

    @Override
    public HomePageDto updateHomePage(HomePageDto updatedHomePageDto) {


        HomePage homePage = new HomePage();
        homePage.setId(updatedHomePageDto.getId());
        homePage.setTitle(updatedHomePageDto.getTitle());
        homePage.setImages(String.join(",", updatedHomePageDto.getImageUrls()));
        homePage.setDescription(String.join(",", updatedHomePageDto.getDescriptions()));
        homePage.setButtonTexts(String.join(",", updatedHomePageDto.getButtonTexts()));

        // Set the user_id to 1 by default (assuming user with ID 1 always exists)
        User defaultUser = new User();
        defaultUser.setId(1L);
        homePage.setUser(defaultUser);

        HomePage updatedHomePage = repo.save(homePage);
        HomePageDto dto = new HomePageDto();
        dto.setId(updatedHomePage.getId());
        dto.setTitle(updatedHomePage.getTitle());
        dto.setImageUrls(Arrays.asList(updatedHomePage.getImages().split(",")));
        dto.setDescriptions(Arrays.asList(updatedHomePage.getDescription().split(",")));
        dto.setButtonTexts(Arrays.asList(updatedHomePage.getButtonTexts().split(",")));
        return dto;
    }
    private HomePageDto convertToDto(HomePage homePage) {
        HomePageDto dto = new HomePageDto();
        dto.setId(homePage.getId());
        dto.setTitle(homePage.getTitle());
        dto.setImageUrls(Arrays.asList(homePage.getImages().split(",")));
        dto.setDescriptions(Arrays.asList(homePage.getDescription().split(",")));
        dto.setButtonTexts(Arrays.asList(homePage.getButtonTexts().split(",")));
        dto.setLink(homePage.getLink());
        return dto;
    }

}
