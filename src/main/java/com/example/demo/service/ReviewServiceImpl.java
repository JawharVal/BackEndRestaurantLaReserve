package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Review;
import com.example.demo.model.User;

import com.example.demo.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDto) {
        Review review = convertToEntity(reviewDto);
        review = reviewRepository.save(review);
        return convertToDto(review);
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDto(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private Review convertToEntity(ReviewDTO reviewDto) {
        User user = userService.getUserEntityById(reviewDto.getUserId());
        Review review = new Review();
        review.setUser(user);
        review.setFoodRating(reviewDto.getFoodRating());
        review.setServiceRating(reviewDto.getServiceRating());
        review.setAmbienceRating(reviewDto.getAmbienceRating());
        review.setValueForMoneyRating(reviewDto.getValueForMoneyRating());
        review.setCleanlinessRating(reviewDto.getCleanlinessRating());
        review.setComments(reviewDto.getComments());
        return review;
    }

    public ReviewDTO convertToDto(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setFoodRating(review.getFoodRating());
        dto.setServiceRating(review.getServiceRating());
        dto.setAmbienceRating(review.getAmbienceRating());
        dto.setValueForMoneyRating(review.getValueForMoneyRating());
        dto.setCleanlinessRating(review.getCleanlinessRating());
        dto.setComments(review.getComments());
        dto.setApproved(review.isApproved());
        dto.setAverageRating(review.calculateAverageRating());
        dto.setReviewDate(review.getReviewDate());
        return dto;
    }
    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDto) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + id));

        updateReviewFromDto(existingReview, reviewDto);
        reviewRepository.save(existingReview);
        return convertToDto(existingReview);
    }
    private void updateReviewFromDto(Review review, ReviewDTO reviewDto) {
        review.setFoodRating(reviewDto.getFoodRating());
        review.setServiceRating(reviewDto.getServiceRating());
        review.setAmbienceRating(reviewDto.getAmbienceRating());
        review.setValueForMoneyRating(reviewDto.getValueForMoneyRating());
        review.setCleanlinessRating(reviewDto.getCleanlinessRating());
        review.setComments(reviewDto.getComments());

    }
}
