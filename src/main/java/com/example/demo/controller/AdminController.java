package com.example.demo.controller;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.BookingService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.MenuItemService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private CategoryService categoryService;
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/user")
    @Operation(summary = "Add a new user", description = "Creates a new user with admin privileges.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request, failed to create user")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO newUser = userService.createUser(userDTO);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/user/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by their ID.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/user/{userId}/bookings")
    @Operation(summary = "Get bookings by user ID", description = "Retrieves all bookings for a specific user.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error, failed to retrieve bookings")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/user/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }



    @PreAuthorize("hasRole('admin')")
    @PutMapping("/user/{id}")
    @Operation(summary = "Edit user details", description = "Updates user details for the specified user ID.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/users/{userId}/role")
    @Operation(summary = "Update user role", description = "Updates the role of a user based on the user ID.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "User role updated successfully")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        userService.updateUserRole(userId, newRole);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieves all users.",
            security = @SecurityRequirement(name = "ROLE_ADMIN"))
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO newCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PreAuthorize("hasRole('admin')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete this category because it is in use.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to delete the category.");
        }
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/menuItems")
    public ResponseEntity<MenuItemDto> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        try {
            MenuItemDto newMenuItem = menuItemService.addMenuItem(menuItemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newMenuItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PreAuthorize("hasRole('admin')")
    @PutMapping("/menuItems/{id}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto menuItemDto) {
        try {
            MenuItemDto updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDto);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/menuItems/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/menuItems")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems() {
        try {
            List<MenuItemDto> menuItems = menuItemService.getAllMenuItems();
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
