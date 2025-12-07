package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampingSiteDTO {
    private String id;
    private String location;
    private Boolean isActive;
}
