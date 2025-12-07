package com.demoProject.demo.model.dto.response;

import com.demoProject.demo.common.enums.RoomStatus;
import com.demoProject.demo.model.entity.CampingTent;
import lombok.Data;

@Data
public class SearchCampingResponse {
    private String roomId;
    private String roomName;
    private String description;
    private Integer capacity;
    private Double pricePerNight;
    private CampingTent campingTent;
    private double rate;
    private boolean active;
}