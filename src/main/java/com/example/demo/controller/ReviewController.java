package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Review;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDto) {
        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDto);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error submitting review", e);
            return new ResponseEntity<>("Error submitting review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        try {
            ReviewDTO reviewDto = reviewService.getReviewById(id);
            return ResponseEntity.ok(reviewDto);
        } catch (Exception e) {
            logger.error("Review not found", e);
            return new ResponseEntity<>("Review not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDto) {
        try {
            ReviewDTO updatedReview = reviewService.updateReview(id, reviewDto);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            logger.error("Error updating review", e);
            return new ResponseEntity<>("Error updating review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            review.setApproved(true);
            reviewRepository.save(review);
            ReviewDTO dto = reviewService.convertToDto(review);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving review: " + e.getMessage());
        }
    }
    @GetMapping("/approved")
    public ResponseEntity<List<ReviewDTO>> getApprovedReviews() {
        List<Review> approvedReviews = reviewRepository.findAllByApproved(true);
        List<ReviewDTO> reviewDtos = approvedReviews.stream()
                .map(reviewService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting review", e);
            return new ResponseEntity<>("Error deleting review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
