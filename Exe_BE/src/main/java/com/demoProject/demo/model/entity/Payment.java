package com.demoProject.demo.model.entity;
import com.demoProject.demo.common.enums.PaymentMethod;
import com.demoProject.demo.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbl_payment")
public class Payment {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(length = 500)
    private String qrCode; // Link ảnh QR hoặc nội dung QR (base64)

    private LocalDateTime paymentDate;
}