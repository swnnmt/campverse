package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.request.CampingInforRequest;
import com.demoProject.demo.model.dto.response.BookingByCampingIdResponse;
import com.demoProject.demo.model.dto.response.BookingByUserIdResponse;
import com.demoProject.demo.model.dto.response.CampingInforResponse;
import com.demoProject.demo.service.CampingInforService;
import com.demoProject.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/camping")
@RequiredArgsConstructor
public class CampingInforController {

    private final CampingInforService service;
    private final BookingService bookingService;

    // Tạo camping mới kèm danh sách dịch vụ
    @PostMapping
    public ResponseEntity<CampingInforResponse> createCamping(@RequestBody CampingInforRequest request) {
        return ResponseEntity.ok(service.createCamping(request));
    }

    //xem thông tin booking
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/booking/{campingId}")
    public ResponseEntity<Page<BookingByCampingIdResponse>> getBookingsByCampingId(
            @PathVariable String campingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BookingByCampingIdResponse> responses = bookingService.getBookingsByCampingId(campingId, page, size);
        return ResponseEntity.ok(responses);
    }

    // Cập nhật thông tin camping và dịch vụ kèm giá
    @PutMapping("update/{id}")
    public ResponseEntity<CampingInforResponse> updateCamping(
            @PathVariable String id,
            @RequestBody CampingInforRequest request
    ) {
        return ResponseEntity.ok(service.updateCamping(id, request));
    }

    // Lấy tất cả camping
    @GetMapping
    public ResponseEntity<List<CampingInforResponse>> getAllCamping() {
        return ResponseEntity.ok(service.getAllCamping());
    }

    // Lấy camping theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CampingInforResponse> getCampingById(@PathVariable String id) {
        return ResponseEntity.ok(service.getCampingById(id));
    }

    // Xóa camping theo ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCamping(@PathVariable String id) {
        service.deleteCamping(id);
        return ResponseEntity.ok("Camping deleted successfully");
    }
}
