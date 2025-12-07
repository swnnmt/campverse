package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRevenueResponse {
    private int month;       
    private double revenue;  
    private long bookings;   
}
