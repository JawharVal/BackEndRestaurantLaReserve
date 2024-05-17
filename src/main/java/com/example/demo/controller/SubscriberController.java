package com.example.demo.controller;


import com.example.demo.dto.SubscriberDTO;
import com.example.demo.model.Subscriber;
import com.example.demo.service.SubscriberService;
import com.example.demo.service.SubscriberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscribe")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping
    @Operation(summary = "Subscribe to newsletter", description = "Subscribe a user to the newsletter.")
    @ApiResponse(responseCode = "200", description = "Subscriber added successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Subscriber.class)))
    @ApiResponse(responseCode = "404", description = "Subscriber not found")
    @ApiResponse(responseCode = "400", description = "Bad request, email already exists")
    public ResponseEntity<?> subscribe(@RequestBody SubscriberDTO subscriberDTO) {
        try {
            Subscriber subscriber = subscriberService.saveSubscriber(subscriberDTO);
            if (subscriber != null) {
                return new ResponseEntity<>(subscriber, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch(SubscriberServiceImpl.EmailAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Fetch all subscribers", description = "Retrieve all subscribers from the database.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all subscribers",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Subscriber.class)))
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.findAllSubscribers();
        return new ResponseEntity<>(subscribers, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteSubscriber(@PathVariable String email) {
        Optional<Subscriber> subscriber = subscriberService.findSubscriberByEmail(email);
        if (subscriber.isPresent()) {
            subscriberService.deleteSubscriberByEmail(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}