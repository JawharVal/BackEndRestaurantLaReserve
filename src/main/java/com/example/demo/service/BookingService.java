package com.example.demo.service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO bookingDTO, String email);
    List<Booking> getBookingsByUserEmail(String email);
    List<BookingDTO> getBookingsByUserId(Long userId);
    BookingDTO createBookingForUser(Long userId, String userEmail, BookingDTO bookingDTO);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    BookingDTO getBookingById(Long id);
    void deleteBooking(Long id);

}
