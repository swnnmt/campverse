package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_booking_promotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingPromotion {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "discount_applied", precision = 19, scale = 2, nullable = false)
    private BigDecimal discountApplied; // số tiền giảm thực tế
}