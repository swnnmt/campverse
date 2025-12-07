package com.demoProject.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demoProject.demo.model.entity.Comment;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    List<Comment> findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(Long postId);
}