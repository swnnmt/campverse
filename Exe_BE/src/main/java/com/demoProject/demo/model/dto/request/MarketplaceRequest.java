package com.demoProject.demo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketplaceRequest {
    private String userId;
    private String phone;
    private String facebookLink;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;
    private String description;
}