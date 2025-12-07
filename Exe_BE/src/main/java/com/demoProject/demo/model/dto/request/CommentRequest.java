package com.demoProject.demo.model.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long postId;
    private String userId;
    private String content;
    private Long parentId; 
}
