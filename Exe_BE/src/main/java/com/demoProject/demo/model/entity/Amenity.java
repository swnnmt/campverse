package com.demoProject.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_amenity")
public class Amenity {

    @Id
    private String id;

    private String name;
    private String iconUrl;

    // @ManyToMany(mappedBy = "amenities")
    // private List<CampingInfor> rooms;
}