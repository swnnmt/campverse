package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.AdminUserRequest;
import com.demoProject.demo.model.dto.response.AdminUserResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AdminUserService {
    Page<AdminUserResponse> getUsers(String keyword, int page, int size);

     Page<AdminUserResponse> getUsersByRole(String roleName, int page, int size);

     Page<AdminUserResponse> getListPartApprove(String roleName, int page, int size, String partnerStatus);

    Optional<AdminUserResponse> getUserDetail(String id);

    Optional<AdminUserResponse> updateUser(String id, AdminUserRequest request);

    boolean banUser(String id, boolean ban);
}
