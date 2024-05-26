package com.example.demo.service;

import com.example.demo.dto.ContactFormDTO;
import com.example.demo.model.ContactForm;
import com.example.demo.model.User;
import com.example.demo.repositories.ContactFormRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactFormServiceImpl implements ContactFormService {
    @Autowired
    private ContactFormRepository contactFormRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void saveContactForm(ContactFormDTO contactFormDTO) {
        User user = null;

        // Check if user ID is provided
        if (contactFormDTO.getUserId() != null) {
            user = userRepository.findById(contactFormDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + contactFormDTO.getUserId()));
        }

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contactFormDTO.getName());
        contactForm.setEmail(contactFormDTO.getEmail());
        contactForm.setPhone(contactFormDTO.getPhone());
        contactForm.setLocation(contactFormDTO.getLocation());
        contactForm.setSubject(contactFormDTO.getSubject());
        contactForm.setMessage(contactFormDTO.getMessage());

        // Set the user for the ContactForm
        contactForm.setUser(user);

        // Save the ContactForm
        contactFormRepository.save(contactForm);
    }

}
