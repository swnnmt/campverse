package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminUserRepository extends JpaRepository<User, String> {
        Page<User> findByUserInfo_FirstNameContainingIgnoreCaseOrUserInfo_LastNameContainingIgnoreCaseOrUserInfo_EmailContainingIgnoreCase(
                        String firstName, String lastName, String email, Pageable pageable);

        Page<User> findByUserInfo_Department(String department, Pageable pageable);

        @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.approveStatus = :partnerStatus")
        Page<User> findByRoleNameAndPartnerStatus(@Param("roleName") String roleName, @Param("partnerStatus") String partnerStatus, Pageable pageable);

        @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
        Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

        Page<User> findByApproveStatus(String approveStatus, Pageable pageable);

}
