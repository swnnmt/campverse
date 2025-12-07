package com.demoProject.demo.model.dto.request;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateBookingRequest {
    private String campingInforId;
    private String campingTentId;
    private List<String> campingServiceIds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;
}