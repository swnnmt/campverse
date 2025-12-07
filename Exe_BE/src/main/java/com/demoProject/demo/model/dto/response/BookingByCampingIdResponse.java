package com.demoProject.demo.model.dto.response;

import com.demoProject.demo.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingByCampingIdResponse {
    private String bookingId;
    private String UserId;
    private String userName;
    private String campingSiteId;
    private String campingInforId;
    private String campingTentId;
    private List<String> serviceNames;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;
    private String status; // e.g. "CONFIRMED", "CANCELLED"
    private LocalDateTime createdAt;
}
