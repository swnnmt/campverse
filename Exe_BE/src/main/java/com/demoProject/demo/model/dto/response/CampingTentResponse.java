package com.demoProject.demo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingTentResponse {
    private String id;
    private String campingId;
    private String tentName;
    private Integer capacity;
    private Double pricePerNight;
    private Integer quantity;
    private String thumbnail;
}
