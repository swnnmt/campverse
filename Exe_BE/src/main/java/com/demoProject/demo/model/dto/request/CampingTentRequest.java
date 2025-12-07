package com.demoProject.demo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingTentRequest {
    private String campingId;       // Tham chiếu đến CampingInfor
    private String tentName;
    private Integer capacity;
    private Double pricePerNight;
    private Integer quantity;
    private String thumbnail;
}
