package com.example.demo.service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.model.Booking;
import com.example.demo.model.Category;
import com.example.demo.model.Restaurant;
import com.example.demo.model.User;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.RestaurantRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EmailService emailService;
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Final availability check before creating the booking
        if (!isSlotAvailable(bookingDTO.getDate(), bookingDTO.getTime())) {
            throw new RuntimeException("The selected time slot is no longer available.");
        }
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDate(bookingDTO.getDate());
        booking.setTime(bookingDTO.getTime());
        booking.setNumberOfPersons(bookingDTO.getNumberOfPersons());
        booking.setFirstName(bookingDTO.getFirstName());
        booking.setLastName(bookingDTO.getLastName());
        booking.setComment(bookingDTO.getComment());

        if (bookingDTO.getRestaurantId() != null) {
            Integer restaurantId = bookingDTO.getRestaurantId().intValue();
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            booking.setRestaurant(restaurant);
        }

        if (bookingDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(bookingDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            booking.setCategory(category);
        }
        sendBookingConfirmation(email,booking);

        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO createBookingForUser(Long userId, String userEmail, BookingDTO bookingDTO) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Category category = categoryRepository.findById(bookingDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            Booking booking = new Booking();
            booking.setDate(bookingDTO.getDate());
            booking.setTime(bookingDTO.getTime());
            booking.setNumberOfPersons(bookingDTO.getNumberOfPersons());
            booking.setFirstName(bookingDTO.getFirstName());
            booking.setLastName(bookingDTO.getLastName());
            booking.setComment(bookingDTO.getComment());
            booking.setCategory(category);
            booking.setUser(user);
            if (bookingDTO.getRestaurantId() != null) {
                Integer restaurantId = bookingDTO.getRestaurantId().intValue();
                Restaurant restaurant = restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new RuntimeException("Restaurant not found"));
                booking.setRestaurant(restaurant);
            }

            booking = bookingRepository.save(booking);

            String subject = "Booking Confirmation";
            String message = "Your booking has been successfully created. Booking ID: " + booking.getId();
            emailService.sendEmail(userEmail, subject, message);

            return convertToDTO(booking);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create booking", e);
        }
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return convertToDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            if (bookingDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(bookingDTO.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                booking.setCategory(category);
            } else {
                booking.setCategory(null);
            }

            if (bookingDTO.getRestaurantId() != null) {
                Integer restaurantId = bookingDTO.getRestaurantId().intValue();
                Restaurant restaurant = restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
                booking.setRestaurant(restaurant);
            } else {
                booking.setRestaurant(null);
            }

            booking.setDate(bookingDTO.getDate());
            booking.setTime(bookingDTO.getTime());
            booking.setNumberOfPersons(bookingDTO.getNumberOfPersons());
            booking.setFirstName(bookingDTO.getFirstName());
            booking.setLastName(bookingDTO.getLastName());
            booking.setComment(bookingDTO.getComment());

            booking = bookingRepository.save(booking);

            String userEmail = booking.getUser().getEmail();
            String subject = "Booking Update";
            String message = "Your booking has been updated. Booking ID: " + booking.getId();
            emailService.sendEmail(userEmail, subject, message);

            return convertToDTO(booking);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update booking", e);
        }
    }

    @Override
    public List<BookingDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setDate(booking.getDate());
        dto.setTime(booking.getTime());
        dto.setNumberOfPersons(booking.getNumberOfPersons());
        dto.setFirstName(booking.getFirstName());
        dto.setLastName(booking.getLastName());
        dto.setComment(booking.getComment());
        if (booking.getRestaurant() != null) {
            dto.setRestaurantId((long) booking.getRestaurant().getId());
        }
        if (booking.getCategory() != null) {
            dto.setCategoryId(booking.getCategory().getId());
        }
        return dto;
    }

    private Map<String, Integer> initializeAvailability() {
        return IntStream.rangeClosed(9, 16)
                .boxed()
                .collect(Collectors.toMap(
                        hour -> String.format("%02d:00", hour),
                        hour -> 2,
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        LinkedHashMap::new
                ));
    }

    public boolean hasExistingBooking(String email, String date) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        List<Booking> bookings = bookingRepository.findByUserAndDate(user.get(), date);
        return !bookings.isEmpty();
    }

    public boolean isSlotAvailable(String date, String time) {
        List<Booking> bookings = bookingRepository.findByDateAndTime(date, time);
        return bookings.size() < 2;
    }
    private void sendBookingConfirmation(String userEmail, Booking booking) {
        String subject = "Booking Confirmation";
        String body = "Dear " + booking.getFirstName() + " " + booking.getLastName() + ",\n\n"
                + "Your booking for " + booking.getDate() + " at " + booking.getTime() + " has been confirmed.\n\n"
                + "Number of persons: " + booking.getNumberOfPersons() + "\n\n"
                + "Thank you for choosing our service.\n\n"
                + "Best regards,\nYour Booking Team";

        try {
            emailService.sendEmail(userEmail, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send booking confirmation email", e);
        }
    }
    @Override
    public List<Booking> getBookingsByUserEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        return user.getBookings();
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.deleteById(id);
        sendDeletionConfirmation(booking.getUser().getEmail(), booking);
    }

    private void sendDeletionConfirmation(String userEmail, Booking booking) {
        String subject = "Booking Cancellation Confirmation";
        String body = "Dear " + booking.getFirstName() + " " + booking.getLastName() + ",\n\n"
                + "Your booking for " + booking.getDate() + " at " + booking.getTime() + " has been successfully cancelled.\n\n"
                + "Thank you,\nYour Booking Team";
        try {
            emailService.sendEmail(userEmail, subject, body);
            emailService.sendEmail("jawharstrike35@gmail.com", subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send booking cancellation email", e);
        }
    }

    public Map<String, Integer> getAvailabilityForDate(String date) {
        List<Object[]> bookingsCount = bookingRepository.countBookingsByDateGroupedByTime(date);
        Map<String, Integer> availability = initializeAvailability();

        for (Object[] result : bookingsCount) {
            String time = (String) result[0];
            Long count = (Long) result[1];
            availability.put(time, Math.max(0, 2 - count.intValue()));
        }

        return availability;
    }
}

