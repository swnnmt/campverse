package com.demoProject.demo.model.dto.request;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String userId;
    private String content;
    private String imageBase64; // Base64 từ frontend
}
