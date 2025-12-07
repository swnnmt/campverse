package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.response.PostResponse;
import com.demoProject.demo.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.demoProject.demo.model.entity.User;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface PostService {
    Post createPost(String userId, String content, String imageUrl);
     Page<PostResponse> getAllPosts(Pageable pageable);
     List<PostResponse> getPostsByUser(User user);
    PostResponse updatePost(Long postId, User user, String content, String image);
    void deletePost(Long postId, User user);
}
