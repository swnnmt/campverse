package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_promotion")
public class Promotion {

    @Id
    private String id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code; // Mã giảm giá: CAMP10, SUMMER2025

    @Column(name = "description")
    private String description;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent; // Giảm theo %

    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount; // Giảm theo số tiền

    @Column(name = "max_discount", precision = 19, scale = 2)
    private BigDecimal maxDiscount; // Giảm tối đa (optional)

    @Column(name = "min_order_amount", precision = 19, scale = 2)
    private BigDecimal minOrderAmount; // Áp dụng từ đơn tối thiểu

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "usage_limit")
    private Integer usageLimit; // Số lần sử dụng tối đa (toàn hệ thống)

    @Column(name = "used_count")
    private Integer usedCount = 0; // Số lần đã dùng

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<BookingPromotion> bookings; // mapping với booking
}