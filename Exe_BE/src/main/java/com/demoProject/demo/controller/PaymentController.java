package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.request.PaymentRequest;
import com.demoProject.demo.model.dto.response.PaymentResponse;
import com.demoProject.demo.model.entity.Payment;
import com.demoProject.demo.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/booking")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> payBooking(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.payBooking(request);
        return ResponseEntity.ok(response);
    }
}