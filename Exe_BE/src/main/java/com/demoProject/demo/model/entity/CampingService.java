package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tbl_camping_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingService {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_id", nullable = false)
    private CampingInfor camping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id") // nullable để custom service có thể null
    private ServiceEntity service; // Dịch vụ có sẵn

    @Column(length = 100)
    private String customName; // Dịch vụ tự tạo, không cần service_id

    @Column(nullable = false)
    private Double price; // Giá dịch vụ theo camping

    @Column(length = 500)
    private String imageUrl; // Ảnh mô tả cho service

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
