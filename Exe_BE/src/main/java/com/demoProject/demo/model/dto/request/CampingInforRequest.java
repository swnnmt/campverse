package com.demoProject.demo.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CampingInforRequest {
    private String userId;  // ID User tạo camping
    private String campingSiteId;   // ID camping site
    private String name;
    private String address;
    private String description;
    private Double basePrice;
    private String thumbnail;
    private Boolean active;
    private Integer capacity; // số lượng chỗ cho khu camping
    private List<CampingServiceRequest> services;
    private List<CampingTentRequest> tents;
    private List<CampingGalleryRequest> galleries;
}
