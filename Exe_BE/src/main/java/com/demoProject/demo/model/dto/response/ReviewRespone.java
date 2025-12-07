package com.demoProject.demo.model.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRespone {
    @NotNull
    private String userId;
    @NotNull
    private String userName;
    @NotNull
    private String campingInforId;
    @NotNull
    private String bookingId;

    private int rating; // e.g. 1-5

    private String comment;
}