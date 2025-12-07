package com.demoProject.demo.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PartnerResponse {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String approveStatus;
    private List<CampingSiteResponse> campingSites;
}
