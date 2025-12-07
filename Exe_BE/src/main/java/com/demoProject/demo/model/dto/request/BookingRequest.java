package com.demoProject.demo.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingRequest {
    @NotNull
    private String userId;

    @NotNull
    private String campingSiteId;

    @NotNull
    private String campingInforId;

    private String campingTentId;
    private List<String> campingServiceIds;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private Double totalPrice;
}