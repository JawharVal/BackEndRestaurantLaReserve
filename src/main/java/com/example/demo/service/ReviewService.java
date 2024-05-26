package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    ReviewDTO createReview(ReviewDTO reviewDto);
    ReviewDTO getReviewById(Long id);

    ReviewDTO convertToDto(Review review);
    void deleteReview(Long id);
    List<ReviewDTO> getAllReviews();
    ReviewDTO updateReview(Long id, ReviewDTO reviewDto);
}
