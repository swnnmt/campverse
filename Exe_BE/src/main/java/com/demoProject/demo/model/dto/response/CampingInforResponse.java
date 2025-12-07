package com.demoProject.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class CampingInforResponse {
    private String id;
    private String userId;
    private String campingSiteId;
    private String campingSiteName;
    private String name;
    private String address;
    private String description;
    private Double basePrice;
    private String thumbnail;
    private Integer bookedCount;
    private Double revenue;
    private List<CampingServiceResponse> services;
    private List<CampingTentResponse> tents;
    private List<CampingGalleryResponse> galleries;
    private Boolean active;
    private Double rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
