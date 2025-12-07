package com.demoProject.demo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_user_info")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    private String id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Lob
    @Column(name = "AVATAR_URL", columnDefinition = "LONGTEXT")
    private String avatarUrl;
}
