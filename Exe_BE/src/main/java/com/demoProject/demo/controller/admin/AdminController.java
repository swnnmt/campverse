package com.demoProject.demo.controller.admin;

import com.demoProject.demo.model.dto.request.AdminUserRequest;
import com.demoProject.demo.model.dto.response.AdminUserResponse;
import com.demoProject.demo.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserService adminService;


    // Danh sách user/partner (search + phân trang)
    @GetMapping
    public ResponseEntity<Page<AdminUserResponse>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getUsers(keyword, page, size));
    }

     // Danh sách USER
    @GetMapping("/listusers")
    public ResponseEntity<Page<AdminUserResponse>> getListUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getUsersByRole("USER", page, size));
    }

    // Danh sách PARTNER
    @GetMapping("/listpartners")
    public ResponseEntity<Page<AdminUserResponse>> getListPartners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getListPartApprove("PARTNER", page, size,"APPROVED"));
    }

    // Xem chi tiết
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> getUserDetail(@PathVariable String id) {
        Optional<AdminUserResponse> user = adminService.getUserDetail(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<AdminUserResponse> updateUser(
            @PathVariable String id,
            @RequestBody AdminUserRequest request) {
        Optional<AdminUserResponse> updated = adminService.updateUser(id, request);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Ban/Unban
    @PutMapping("/{id}/ban")
    public ResponseEntity<String> banUser(
            @PathVariable String id,
            @RequestParam boolean ban) {
        boolean result = adminService.banUser(id, ban);
        return result ? ResponseEntity.ok("User status updated") : ResponseEntity.notFound().build();
    }
}
