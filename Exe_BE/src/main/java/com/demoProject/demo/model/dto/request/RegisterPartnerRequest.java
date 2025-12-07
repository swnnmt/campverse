package com.demoProject.demo.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RegisterPartnerRequest {
    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @NotBlank(message = "Phone Number is mandatory")
    private String phoneNumber;

    @NotBlank(message = "Address partner is mandatory")
    private String address_partner;

    private String campingSiteId;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "Camping is mandatory")
    private String name_camping;

    @NotBlank(message = "Description is mandatory")
    private String description_camping;

    @NotEmpty(message = "Image camping is mandatory")
    private List<String> imageUrls;

    @NotBlank(message = "Role is mandatory")
    private String role;
}
