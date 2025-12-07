package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_camping_infor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingInfor {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    // Tham chiếu tới Owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    // Tham chiếu tới City
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "camping_site_id")
    private CampingSite campingSite;


    @Column(nullable = false, length = 155)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(length = 500)
    private String thumbnail;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount = 0;

    @Column(nullable = false)
    private Double revenue = 0.0;

    //  Thêm trường số lượng chỗ (capacity)
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 0; // số lượng chỗ cho khu camping

    // Quan hệ 1-nhiều với dịch vụ
    @OneToMany(mappedBy = "camping", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampingService> services;

    // Quan hệ 1-nhiều với lều
    @OneToMany(mappedBy = "camping", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampingTent> tents;

    // Quan hệ 1-nhiều với gallery
    @OneToMany(mappedBy = "camping", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampingGallery> galleries;

    @Column(nullable = false)
    private Boolean active = false; // Trường để admin duyệt

    @Column(nullable = false)
    private Double rate = 0.0; // Trung bình đánh giá từ người dùng

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
