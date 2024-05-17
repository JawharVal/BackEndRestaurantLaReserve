package com.example.demo.controller;



import com.example.demo.config.JWTGenerator;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    private final BookingService bookingService;


    private AuthenticationManager authenticationManager;

    private JWTGenerator jwtGenerator;

    Authentication authentication;

    @Autowired
    public UserController(UserService userService, BookingService bookingService, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user.")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with a default role.")
    @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setRole("user");  // Set default role to user
        UserDTO registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    @Operation(summary = "Login", description = "Logs in a user and generates an access token.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDTO.class)))
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);  // Ensure this method includes the role in the token

        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user.")
    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            // Return an error response or prompt for authentication
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is required");
        }
        String email = authentication.getName(); // The email should be the username set by Spring Security
        System.out.println("Email from Authentication: " + email);
        UserDTO userDTO = userService.getUserByEmail(email);

        // Get the user's bookings
        List<Booking> bookings = bookingService.getBookingsByUserEmail(email);

        // Convert the bookings to BookingDTOs and set them in the UserDTO
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(booking -> convertToDTO(booking)) // You'll need to implement this method
                .collect(Collectors.toList());
        userDTO.setBookings(bookingDTOs);

        return ResponseEntity.ok(userDTO);
    }
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setTime(booking.getTime());
        bookingDTO.setNumberOfPersons(booking.getNumberOfPersons());
        bookingDTO.setFirstName(booking.getFirstName());
        bookingDTO.setLastName(booking.getLastName());
        bookingDTO.setComment(booking.getComment());
        return bookingDTO;
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "User logged out successfully")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Since we cannot truly "invalidate" a JWT, we simply clear the security context
        SecurityContextHolder.clearContext();

        // Optionally, you can implement token blacklisting here if your application has that capability
        // This would involve storing the token in a list of revoked tokens and checking against it on each request

        // Inform client to clear the token
        return ResponseEntity.ok().body("Logged out successfully");
    }

}



