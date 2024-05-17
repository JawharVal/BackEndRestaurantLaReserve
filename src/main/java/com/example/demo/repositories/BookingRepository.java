package com.example.demo.repositories;

import com.example.demo.dto.BookingDTO;
import com.example.demo.model.Booking;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByDate(String date);
    List<Booking> findByDateAndTime(String date, String time);
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b.time, COUNT(b) FROM Booking b WHERE b.date = :date GROUP BY b.time")
    List<Object[]> countBookingsByDateGroupedByTime(String date);
    List<Booking> findByUserAndDate(User user, String date);
}
