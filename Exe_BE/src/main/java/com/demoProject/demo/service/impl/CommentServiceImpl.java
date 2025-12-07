package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.response.CommentResponse;
import com.demoProject.demo.model.entity.Comment;
import com.demoProject.demo.model.entity.Post;
import com.demoProject.demo.model.entity.User;
import com.demoProject.demo.repository.CommentRepository;
import com.demoProject.demo.repository.PostRepository;
import com.demoProject.demo.repository.UserRepository;
import com.demoProject.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment addComment(Long postId, String userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    @Override
    public Comment replyToComment(Long postId, String userId, String content, Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment reply = new Comment();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(content);
        reply.setParentComment(parent);
        return commentRepository.save(reply);
    }

    @Override
    public List<CommentResponse> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::convertToResponseDto)
                               .collect(Collectors.toList());
    }

    private CommentResponse convertToResponseDto(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUserName(comment.getUser().getUserInfo().getFullName());
        response.setUserAvatar(comment.getUser().getUserInfo().getAvatarUrl()); 
        
          if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            response.setReplies(comment.getReplies()
                    .stream()
                    .map(this::convertToResponseDto)
                    .toList());
        }
        return response;
    }
}
