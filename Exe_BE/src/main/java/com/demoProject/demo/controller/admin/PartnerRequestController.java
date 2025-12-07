package com.demoProject.demo.controller.admin;

import com.demoProject.demo.model.dto.response.PartnerResponse;
import com.demoProject.demo.service.PartRequestService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý yêu cầu đăng ký của đối tác (Partner)
 * Bao gồm: xem danh sách chờ duyệt, xem chi tiết, phê duyệt và từ chối.
 */
@RestController
@RequestMapping("/api/v1/admin/partners")
@RequiredArgsConstructor
public class PartnerRequestController {

    private final PartRequestService partnerRequestService;

    /**
     * Lấy danh sách đối tác đang chờ duyệt
     */
    @GetMapping("/pending")
    public ResponseEntity<Page<PartnerResponse>> getPendingPartners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PartnerResponse> pendingPartners = partnerRequestService.getPendingPartners(page, size);
        return ResponseEntity.ok(pendingPartners);
    }

    /**
     * Xem chi tiết một đối tác theo ID
     */
    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerResponse> getPartnerDetail(@PathVariable String partnerId) {
        PartnerResponse partnerDetail = partnerRequestService.getPartnerDetail(partnerId);
        return ResponseEntity.ok(partnerDetail);
    }

    /**
     * Phê duyệt đối tác
     */
    @PostMapping("/{partnerId}/approve")
    public ResponseEntity<String> approvePartner(@PathVariable String partnerId) {
        try {
            partnerRequestService.approvePartner(partnerId);
            return ResponseEntity.ok("✅ Partner đã được phê duyệt và email thông báo đã được gửi.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Lỗi khi gửi email: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("⚠️ " + e.getMessage());
        }
    }

    /**
     * Từ chối đối tác
     */
    @PostMapping("/{partnerId}/reject")
    public ResponseEntity<String> rejectPartner(@PathVariable String partnerId) {
        try {
            partnerRequestService.rejectPartner(partnerId);
            return ResponseEntity.ok("🚫 Partner đã bị từ chối và email thông báo đã được gửi.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Lỗi khi gửi email: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("⚠️ " + e.getMessage());
        }
    }
}
