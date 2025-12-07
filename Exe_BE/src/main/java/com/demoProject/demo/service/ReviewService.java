package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.ReviewRequest;
import com.demoProject.demo.model.dto.response.ReviewRespone;

import com.demoProject.demo.model.entity.Review;
import java.util.List;

public interface ReviewService {
    void createReview(ReviewRequest request);
    List<ReviewRespone> getReviewsByCampingInforId(String campingInforId);
}
