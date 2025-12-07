package com.demoProject.demo.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CampingSiteResponse {
    private String id;
    private String location;
    private List<String> images;
}
