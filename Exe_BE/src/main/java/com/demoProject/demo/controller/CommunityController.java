package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.response.CommentResponse;
import com.demoProject.demo.model.dto.request.CommentRequest;
import com.demoProject.demo.model.dto.request.CreatePostRequest;
import com.demoProject.demo.model.dto.response.PostResponse;
import com.demoProject.demo.model.entity.Comment;
import com.demoProject.demo.model.entity.Post;
import com.demoProject.demo.service.CommentService;
import com.demoProject.demo.service.LikeService;
import com.demoProject.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.demoProject.demo.model.entity.User;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = "*")
public class CommunityController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

     @PostMapping("/posts")
    public Post createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(
                request.getUserId(),
                request.getContent(),
                request.getImageBase64()
        );
    }
     @GetMapping("/posts")
    public Page<PostResponse> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/posts/user/{userId}")
    public List<PostResponse> getPostsByUser(@PathVariable String userId) {
        User user = new User();
        user.setId(userId);
        return postService.getPostsByUser(user);
    }

    @PutMapping("/posts/{postId}")
public PostResponse updatePost(
        @PathVariable Long postId,
        @RequestBody CreatePostRequest request
) {
    User user = new User();
    user.setId(request.getUserId());
    return postService.updatePost(postId, user, request.getContent(), request.getImageBase64());
}

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId, @RequestParam String userId) {
        User user = new User();
        user.setId(userId);
        postService.deletePost(postId, user);
        return "Post deleted successfully";
    }


    @PostMapping("/comments")
    public Comment addComment(@RequestBody CommentRequest request) {
        return commentService.addComment(request.getPostId(), request.getUserId(), request.getContent());
    }

    @GetMapping("/comments/{postId}")
    public List<CommentResponse> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @PostMapping("/comments/reply")
public Comment replyToComment(@RequestBody CommentRequest request) {
    return commentService.replyToComment(
            request.getPostId(),
            request.getUserId(),
            request.getContent(),
            request.getParentId()
    );
}

    @PostMapping("/likes/toggle")
    public String toggleLike(@RequestParam Long postId,
                             @RequestParam String userId) {
        likeService.toggleLike(postId, userId);
        return "Like toggled successfully";
    }

    @GetMapping("/likes/count/{postId}")
    public int countLikes(@PathVariable Long postId) {
        return likeService.countLikes(postId);
    }
}
