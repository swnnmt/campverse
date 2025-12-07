package com.demoProject.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.demoProject.demo.model.entity.Post;
import com.demoProject.demo.model.entity.User;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>,JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByUser(User user);
}
