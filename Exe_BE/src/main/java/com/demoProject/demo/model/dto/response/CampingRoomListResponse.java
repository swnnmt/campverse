package com.demoProject.demo.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class CampingRoomListResponse {
    private String roomId;
    private String description;
    private Integer capacity;
    private Double pricePerNight;
    private String siteName;
    private String location;
    private List<String> imageUrls;
}