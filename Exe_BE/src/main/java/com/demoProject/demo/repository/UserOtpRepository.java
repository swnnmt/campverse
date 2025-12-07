package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserOtpRepository extends JpaRepository<UserOtp, String> {

    Optional<UserOtp> findByOtpCode(String validOtp);

    @Query("""
            SELECT u
            FROM UserOtp u
            WHERE u.user.userInfo.email = :userEmail
             AND u.otpCode = :otpKey
        """)
    Optional<UserOtp> findValidOtp(String userEmail, String otpKey);
}
