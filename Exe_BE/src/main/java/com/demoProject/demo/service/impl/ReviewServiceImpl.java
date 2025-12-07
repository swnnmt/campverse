package com.demoProject.demo.service.impl;

import com.demoProject.demo.common.enums.BookingStatus;
import com.demoProject.demo.model.dto.request.ReviewRequest;
import com.demoProject.demo.model.dto.response.ReviewRespone;
import com.demoProject.demo.model.entity.*;
import com.demoProject.demo.repository.*;
import com.demoProject.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final CampingInforRepository campingInforRepository;
    private final UserRepository userRepository;

    @Override
    public void createReview(ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Unauthenticated");
        }

        // Use userId from the request to load the user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        // Only allow review when booking status is COMPLETED
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Booking not completed");
        }

        // Check if already reviewed for this booking
        if (reviewRepository.existsByBooking_Id(booking.getId())) {
            throw new IllegalArgumentException("Already reviewed");
        }

        CampingInfor campingInfor = campingInforRepository.findById(request.getCampingInforId())
                .orElseThrow(() -> new IllegalArgumentException("CampingInfor not found"));

        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setUser(user);
        review.setBooking(booking);
        review.setCampingInfor(campingInfor);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        // Update average rating for the campingInfor
        List<Review> reviews = reviewRepository.findByCampingInfor_Id(campingInfor.getId());
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        campingInfor.setRate(avg);
        campingInforRepository.save(campingInfor);
    }

    @Override
    public List<ReviewRespone> getReviewsByCampingInforId(String campingInforId) {
        return reviewRepository.findByCampingInfor_Id(campingInforId)
                .stream()
                .map(r -> {
                    ReviewRespone response = new ReviewRespone();
                    response.setUserId(r.getUser().getId());
                    response.setUserName(r.getUser().getUserInfo().getFullName());
                    response.setCampingInforId(r.getCampingInfor().getId());
                    response.setBookingId(r.getBooking().getId());
                    response.setRating(r.getRating());
                    response.setComment(r.getComment());
                    return response;
                })
                .toList();
    }
}