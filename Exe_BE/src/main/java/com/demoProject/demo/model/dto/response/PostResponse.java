package com.demoProject.demo.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    private String userName;
    private String userAvatar;

    private int totalLikes;
    private int totalComments;

    private List<CommentResponse> comments;
}
