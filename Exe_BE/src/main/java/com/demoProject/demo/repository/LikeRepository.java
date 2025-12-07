package com.demoProject.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demoProject.demo.model.entity.Like;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(String userId, Long postId);
    int countByPostId(Long postId);
}
