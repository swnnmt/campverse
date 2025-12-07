package com.demoProject.demo.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingByUserIdResponse {
    private String bookingId;
    private String userId;
    private String campingSiteId;
    private String campingInforId;
    private String campingTentId;
    private List<String> serviceNames;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;
    private String status; // e.g. "CONFIRMED", "CANCELLED"
}
