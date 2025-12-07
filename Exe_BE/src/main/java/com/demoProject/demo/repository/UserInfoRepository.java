package com.demoProject.demo.repository;


import com.demoProject.demo.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    boolean existsByEmail(String email);
}
