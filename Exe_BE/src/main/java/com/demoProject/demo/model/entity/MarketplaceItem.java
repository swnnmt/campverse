package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_marketplace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketplaceItem {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 255)
    private String facebookLink;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 500)
    private String productImage;

    private Integer quantity;

    private Double price;

    @Column(length = 1000)
    private String description;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
