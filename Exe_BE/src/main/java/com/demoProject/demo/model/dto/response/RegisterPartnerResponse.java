package com.demoProject.demo.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RegisterPartnerResponse {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address_partner;

    private String campingSiteId;

    private String name_camping;

    private String description_camping;

    private String email;

    private List<String> imageUrls;
}
