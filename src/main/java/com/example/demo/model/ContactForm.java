package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class ContactForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    @Size(max = 255, message = "Location must be less than 255 characters")
    private String location;

    @NotBlank(message = "Subject cannot be empty")
    @Size(min = 1, max = 150, message = "Subject must be between 1 and 150 characters")
    private String subject;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 1, max = 2000, message = "Message must be between 1 and 2000 characters")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
