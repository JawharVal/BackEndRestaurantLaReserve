package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "users")
public class User  {
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public List<Booking> getBookings() {
        return bookings;
    }
    @OneToMany(mappedBy = "user")
    private List<ContactForm> contactForms;
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Email
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;


    @NotBlank
    @Size(min = 8, max = 100)
    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    @Getter @Setter
    private String username;

    @Getter @Setter
    @Column(nullable = false)
    private String City;

    @Getter @Setter
    @Column (nullable = false)
    private String region;

    @Column(nullable = false)
    @Getter @Setter
    private String role;



}
