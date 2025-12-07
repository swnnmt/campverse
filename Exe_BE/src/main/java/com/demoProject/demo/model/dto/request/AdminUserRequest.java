package com.demoProject.demo.model.dto.request;

import lombok.Data;

@Data
public class AdminUserRequest {
    private String phoneNumber;
    private String address;
    private boolean enabled;
}
