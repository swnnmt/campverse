package com.demoProject.demo.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private String userId;
    @NotNull
    private String campingInforId;
    @NotNull
    private String bookingId;

    private int rating; // e.g. 1-5

    private String comment;
}