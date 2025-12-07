package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_camping_tent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingTent {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_id", nullable = false)
    private CampingInfor camping;

    @Column(nullable = false, length = 100)
    private String tentName;

    private Integer capacity;
    private Double pricePerNight;
    private Integer quantity;

    @Column(length = 500)
    private String thumbnail;

    // Tự động gán ID trước khi persist
    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
