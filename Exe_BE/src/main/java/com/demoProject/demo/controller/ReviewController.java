package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.request.ReviewRequest;
import com.demoProject.demo.model.dto.response.ReviewRespone;
import com.demoProject.demo.model.entity.Review;
import com.demoProject.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request) {
        reviewService.createReview(request);
        return ResponseEntity.ok("Review submitted successfully");
    }

    @GetMapping("/camping/{campingInforId}")
    public ResponseEntity<List<ReviewRespone>> getReviewsByCamping(@PathVariable String campingInforId) {
        List<ReviewRespone> reviews = reviewService.getReviewsByCampingInforId(campingInforId);
        return ResponseEntity.ok(reviews);
    }
}
