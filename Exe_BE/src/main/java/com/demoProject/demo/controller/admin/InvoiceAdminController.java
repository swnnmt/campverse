package com.demoProject.demo.controller.admin;

import com.demoProject.demo.model.dto.response.InvoiceDTO;
import com.demoProject.demo.service.InvoiceAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/invoices")
@RequiredArgsConstructor
public class InvoiceAdminController {

    private final InvoiceAdminService invoiceAdminService;


    @GetMapping
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(invoiceAdminService.getAllInvoices(pageable));
    }

    // ================= Tìm kiếm hóa đơn (bookingId, start, end, phân trang)
    // =================
    @GetMapping("/search")
    public ResponseEntity<Page<InvoiceDTO>> searchInvoices(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(invoiceAdminService.searchInvoices(bookingId, start, end, pageable));
    }

    // ================= Lấy chi tiết hóa đơn theo id =================
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable String id) {
        return ResponseEntity.ok(invoiceAdminService.getInvoiceById(id));
    }

    // ================= Cập nhật trạng thái hóa đơn =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> updateInvoiceStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(invoiceAdminService.updateInvoiceStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceAdminService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
