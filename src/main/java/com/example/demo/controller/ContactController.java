package com.example.demo.controller;

import com.example.demo.dto.ContactFormDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.ContactForm;
import com.example.demo.service.ContactFormService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class ContactController {

    private final JavaMailSender mailSender;
    private final ContactFormService contactFormService;
    private final UserService userService; // Ensure you inject UserService
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    public ContactController(JavaMailSender mailSender, ContactFormService contactFormService, UserService userService) {
        this.mailSender = mailSender;
        this.contactFormService = contactFormService;
        this.userService = userService; // Initialize userService
    }

    @PostMapping("/contact")
    @Operation(summary = "Handle contact form submission", description = "Receives contact form data and sends an email to the administrator, then saves to database.")
    @ApiResponse(responseCode = "204", description = "Contact form processed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<?> handleContactSubmission(@RequestBody ContactForm form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("jawharstrike35@gmail.com");
            message.setTo("jawharstrike35@gmail.com");
            message.setSubject("New contact form submission from " + form.getName());
            message.setText("Name: " + form.getName() + "\n"
                    + "Email: " + form.getEmail() + "\n"
                    + "Phone: " + form.getPhone() + "\n"
                    + "Location: " + form.getLocation() + "\n"
                    + "Subject: " + form.getSubject() + "\n"
                    + "Message: " + form.getMessage());
            mailSender.send(message);

            ContactFormDTO dto = new ContactFormDTO();
            dto.setName(form.getName());
            dto.setEmail(form.getEmail());
            dto.setPhone(form.getPhone());
            dto.setLocation(form.getLocation());
            dto.setSubject(form.getSubject());
            dto.setMessage(form.getMessage());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
                String email = authentication.getName();
                UserDTO userDTO = userService.getUserByEmail(email);
                if (userDTO != null) {
                    dto.setUserId(userDTO.getId());
                }
            }

            contactFormService.saveContactForm(dto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error processing contact form submission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process contact form. Please try again later.");
        }

    }


    private ContactFormDTO convertToDTO(ContactForm form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Authentication object: {}", authentication);
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            logger.error("No authenticated user available, authentication: {}", authentication);
            throw new IllegalStateException("No authenticated user available for contact form submission.");
        }

        String email = authentication.getName();
        logger.debug("User email from authentication: {}", email);
        UserDTO userDTO = userService.getUserByEmail(email);

        if (userDTO == null) {
            logger.error("Authenticated user not found in database, email: {}", email);
            throw new IllegalStateException("Authenticated user could not be found in the database.");
        }

        ContactFormDTO dto = new ContactFormDTO();
        dto.setName(form.getName());
        dto.setEmail(form.getEmail());
        dto.setPhone(form.getPhone());
        dto.setLocation(form.getLocation());
        dto.setSubject(form.getSubject());
        dto.setMessage(form.getMessage());
        dto.setUserId(userDTO.getId());
        return dto;
    }
}
