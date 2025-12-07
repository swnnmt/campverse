package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_booking_detail")
public class BookingDetail {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_infor_id", referencedColumnName = "id")
    private CampingInfor room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_tent_id")
    private CampingTent campingTent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_service_id")
    private CampingService campingService;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double price;
}