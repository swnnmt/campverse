package com.demoProject.demo.model.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class AdminUserResponse {
    private String id;
    private String address;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDateTime createdAt;
    private boolean enabled;
    private boolean locked;
}
