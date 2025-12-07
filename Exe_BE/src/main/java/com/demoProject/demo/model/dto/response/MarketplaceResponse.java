package com.demoProject.demo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketplaceResponse {
    private String id;
    private String userId;
    private String phone;
    private String facebookLink;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;
    private String description;
}