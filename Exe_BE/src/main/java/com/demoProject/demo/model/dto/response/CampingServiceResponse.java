package com.demoProject.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CampingServiceResponse {
    private String id;           // ID bảng CampingService
    private String serviceId;    // ID của Service
    private String serviceName;  // Tên Service
    private Double price;        // Giá dịch vụ
    private String imageUrl;     // Ảnh mô tả cho dịch vụ
}
