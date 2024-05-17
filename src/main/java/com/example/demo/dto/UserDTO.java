package com.example.demo.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String region;

    @Getter
    @Setter
    private String role;

    private List<BookingDTO> bookings;

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }

    public UserDTO(Long id, String email, String username, String password, String city, String region, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.region = region;
        this.role = role;
    }
}

