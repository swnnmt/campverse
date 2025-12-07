package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CampingSiteBookingResponse {
    private String campingSiteName; 
    private long totalBookings;     
}
