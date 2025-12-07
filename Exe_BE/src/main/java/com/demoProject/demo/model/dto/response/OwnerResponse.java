package com.demoProject.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OwnerResponse {
    private String id;
    private String username;
    private String email;
    private String phone;
}
