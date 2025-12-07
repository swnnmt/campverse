package com.demoProject.demo.service;

public interface LikeService {
    void toggleLike(Long postId, String userId);
    int countLikes(Long postId);
}
