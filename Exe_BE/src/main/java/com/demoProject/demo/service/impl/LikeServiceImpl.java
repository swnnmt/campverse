package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.entity.Like;
import com.demoProject.demo.model.entity.Post;
import com.demoProject.demo.model.entity.User;
import com.demoProject.demo.repository.LikeRepository;
import com.demoProject.demo.repository.PostRepository;
import com.demoProject.demo.repository.UserRepository;
import com.demoProject.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void toggleLike(Long postId, String userId) {
        Like existingLike = likeRepository.findByUserIdAndPostId(userId, postId).orElse(null);
        if (existingLike != null) {
            likeRepository.delete(existingLike);
        } else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    @Override
    public int countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
