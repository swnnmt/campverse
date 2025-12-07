package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.response.CommentResponse;
import com.demoProject.demo.model.entity.Comment;
import java.util.List;

public interface CommentService {
    Comment addComment(Long postId, String userId, String content);
    List<CommentResponse> getCommentsByPost(Long postId);
    Comment replyToComment(Long postId, String userId, String content, Long parentId);
}
