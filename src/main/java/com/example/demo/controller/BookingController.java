package com.example.demo.controller;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/bookings")
public class BookingController {
    @Autowired
    private BookingServiceImpl bookingService; // Changed from BookingService to BookingServiceImpl

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    @Operation(summary = "Create a new booking")
    @ApiResponse(responseCode = "201", description = "Booking created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDTO.class)))
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Ensure that you handle authentication properly.

        BookingDTO newBooking = bookingService.createBooking(bookingDTO, email);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking")
    @ApiResponse(responseCode = "200", description = "Booking deleted successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            bookingService.deleteBooking(id); // Implement this method in your service
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting booking");
        }
    }

    @PostMapping("/admin/user/{userId}")
    @Operation(summary = "Create a booking for a specific user", description = "Creates a booking for a user based on the user ID provided and sends an email notification.")
    @ApiResponse(responseCode = "201", description = "Booking created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error, failed to create booking")
    public ResponseEntity<?> createBookingForUser(@PathVariable Long userId, @RequestBody BookingDTO bookingDTO) {
        try {
            // Get the user's email based on userId
            UserDTO userDTO = userService.getUserById(userId);
            String userEmail = userDTO.getEmail();

            // Create booking for the user
            BookingDTO createdBooking = bookingService.createBookingForUser(userId, userEmail, bookingDTO);

            // Send email notification
            String subject = "Booking Confirmation";
            String message = "Your booking has been successfully created. Booking ID: " + createdBooking.getId();
            emailService.sendEmail(userEmail, subject, message);

            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create booking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/{id}")
    @Operation(summary = "Update an existing booking", description = "Updates a booking based on the booking ID provided.")
    @ApiResponse(responseCode = "200", description = "Booking updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDTO.class)))
    @ApiResponse(responseCode = "404", description = "Booking not found, failed to update")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        try {
            BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update booking: " + e.getMessage());
        }
    }





    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by its ID.")
    @ApiResponse(responseCode = "200", description = "Booking found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDTO.class)))
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        try {
            BookingDTO booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // New endpoint to fetch bookings by user ID
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get bookings by user ID", description = "Fetch all bookings for a specific user.")
    @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDTO.class)))
    @ApiResponse(responseCode = "500", description = "Error retrieving bookings")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable Long userId) {
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/check-availability")
    @Operation(summary = "Check availability", description = "Check availability for a specific date.")
    @ApiResponse(responseCode = "200", description = "Availability information retrieved successfully")
    public ResponseEntity<Map<String, Integer>> checkAvailability(@RequestParam String date) {
        Map<String, Integer> availability = bookingService.getAvailabilityForDate(date);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if booking exists", description = "Check if there is an existing booking for the user on a given date.")
    @ApiResponse(responseCode = "200", description = "Existence of booking checked successfully")
    public ResponseEntity<Boolean> checkExistingBooking(@RequestParam String date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean exists = bookingService.hasExistingBooking(email, date);
        return ResponseEntity.ok(exists);
    }
}
