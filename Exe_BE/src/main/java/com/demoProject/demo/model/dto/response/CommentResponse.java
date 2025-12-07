package com.demoProject.demo.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String userName;
    private String userAvatar;
    private LocalDateTime createdAt;
     private List<CommentResponse> replies;
}
