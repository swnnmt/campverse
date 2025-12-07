package com.demoProject.demo.model.entity;

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
@Table(name = "tbl_camping_site")
public class CampingSite {
    @Id
    private String id;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "campingSite", cascade = CascadeType.ALL)
    private List<CampingInfor> rooms;
}
