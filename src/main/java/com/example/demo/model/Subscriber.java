package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "subscribers")
@Data
@NoArgsConstructor
public class Subscriber {

    @Id
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "First name cannot be empty")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Column(nullable = false)
    private String lastName;

    private String promoCode;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;
}
