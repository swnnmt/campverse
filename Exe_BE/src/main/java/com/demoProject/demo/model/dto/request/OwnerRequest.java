package com.demoProject.demo.model.dto.request;

import lombok.Data;

@Data
public class OwnerRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
}
