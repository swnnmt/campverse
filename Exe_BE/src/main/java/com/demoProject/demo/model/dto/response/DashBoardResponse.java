package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashBoardResponse {
    private Long totalBookingsLastMonth;
    private Double totalRevenueLastMonth;
    private Long totalBookingsThisMonth;
    private Double totalRevenueThisMonth;
    private Long totalCampingSites;
    private Long totalReviews;
}
