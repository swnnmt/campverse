package com.demoProject.demo.model.dto.request;

import lombok.Data;

@Data
public class CampingServiceRequest {
    private String serviceId;   // ID dịch vụ có sẵn (nullable)
    private String customName;  // Tên dịch vụ tự tạo (nullable)
    private Double price;       // Giá dịch vụ
    private String imageUrl;    // Ảnh mô tả cho service
}
