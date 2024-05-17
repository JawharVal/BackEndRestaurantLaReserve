package com.example.demo.controller;

import com.example.demo.dto.HomePageDto;
import com.example.demo.service.HomePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Ensuring the same base route for comparison
public class HomePageController {

    private final HomePageService service;

    @Autowired
    public HomePageController(HomePageService service) {
        this.service = service;
    }

    @GetMapping("/homePages")
    @Operation(summary = "Get all home pages", description = "Retrieves all home pages.")
    @ApiResponse(responseCode = "200", description = "Home pages retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<HomePageDto>> getHomePages() {
        List<HomePageDto> homePageDtos = service.getHomePages();
        if (homePageDtos != null && !homePageDtos.isEmpty()) {
            return ResponseEntity.ok(homePageDtos); // Returning OK response with data
        } else {
            return ResponseEntity.noContent().build(); // Returning no content if list is empty
        }
    }

    @PutMapping("/homePage/{id}")
    @Operation(summary = "Update home page", description = "Updates a home page by its ID.")
    @ApiResponse(responseCode = "200", description = "Home page updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HomePageDto.class)))
    @ApiResponse(responseCode = "404", description = "Home page not found")
    public ResponseEntity<HomePageDto> updateHomePage(@PathVariable Long id, @RequestBody HomePageDto updatedHomePageDto) {
        HomePageDto updatedHomePage = service.updateHomePage(updatedHomePageDto);
        if (updatedHomePage != null) {
            return ResponseEntity.ok(updatedHomePage); // Returning OK response with updated data
        } else {
            return ResponseEntity.notFound().build(); // If no entity was found to update
        }
    }

    @GetMapping("/homePage/{id}")
    @Operation(summary = "Get a home page by ID", description = "Retrieves a single home page by its ID.")
    @ApiResponse(responseCode = "200", description = "Home page retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HomePageDto.class)))
    public ResponseEntity<HomePageDto> getHomePageById(@PathVariable Long id) {
        HomePageDto homePage = service.getHomePageById(id);
        if (homePage != null) {
            return ResponseEntity.ok(homePage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
