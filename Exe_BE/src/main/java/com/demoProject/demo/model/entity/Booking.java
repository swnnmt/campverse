package com.demoProject.demo.model.entity;

import com.demoProject.demo.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_booking")
public class Booking {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // khách đặt

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_site_id", nullable = false)
    private CampingSite campingSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_infor_id", nullable = false)
    private CampingInfor campingInfor;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> details;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "CHECKIN_TIME")
    private LocalDateTime checkinTime;

    @Column(name = "RETURN_TIME")
    private LocalDateTime returnTime;
}
