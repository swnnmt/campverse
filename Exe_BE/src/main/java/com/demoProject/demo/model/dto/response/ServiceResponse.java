package com.demoProject.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse {
    private String id;
    private String name;
    private String description;
}
