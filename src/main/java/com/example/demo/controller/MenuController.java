package com.example.demo.controller;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.service.BookingService;
import com.example.demo.service.EmailService;
import com.example.demo.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menuItems") // Updated to match the API base path and resource
public class MenuController {

    private final MenuItemService menuItemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    public MenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // Retrieve a single menu item by ID
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a single menu item by ID", description = "Retrieves a menu item by its ID along with booking details.")
    @ApiResponse(responseCode = "200", description = "Menu item retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemDto.class)))
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable Long id) {
        MenuItemDto menuItemDto = menuItemService.getMenuItem(id);
        if (menuItemDto != null) {
            return ResponseEntity.ok(menuItemDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Retrieve all menu items by section
    // Retrieve all menu items by section and optional category
    @GetMapping("/section/{section}")
    @Operation(summary = "Retrieve all menu items by section", description = "Retrieves all menu items by their section and optional category.")
    @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "204", description = "No content, empty list")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsBySection(@PathVariable String section, @RequestParam(required = false) Long category) {
        List<MenuItemDto> menuItems = menuItemService.getMenuItemsBySectionAndCategory(section, category);
        if (menuItems != null && !menuItems.isEmpty()) {
            return ResponseEntity.ok(menuItems);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping
    @Operation(summary = "Retrieve all menu items", description = "Retrieves all menu items from the database.")
    @ApiResponse(responseCode = "200", description = "All menu items retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems() {
        List<MenuItemDto> menuItems = menuItemService.getAllMenuItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            return ResponseEntity.ok(menuItems);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // Add a new menu item
    @PostMapping
    @Operation(summary = "Add a new menu item", description = "Creates a new menu item with a category.")
    @ApiResponse(responseCode = "201", description = "Menu item created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemDto.class)))
    public ResponseEntity<MenuItemDto> addMenuItem(@RequestBody MenuItemDto menuItemDto) {
        MenuItemDto newMenuItem = menuItemService.addMenuItem(menuItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMenuItem);
    }


    // Update an existing menu item
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing menu item", description = "Updates an existing menu item by its ID, including its category.")
    @ApiResponse(responseCode = "200", description = "Menu item updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemDto.class)))
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto menuItemDto) {
        MenuItemDto updatedMenuItemDto = menuItemService.updateMenuItem(id, menuItemDto);
        if (updatedMenuItemDto != null) {
            return ResponseEntity.ok(updatedMenuItemDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a menu item
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item", description = "Deletes a menu item by its ID.")
    @ApiResponse(responseCode = "204", description = "Menu item deleted successfully")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Assuming deletion can fail if id not found
        }
    }


}
