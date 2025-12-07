package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.PaymentRequest;
import com.demoProject.demo.model.dto.response.PaymentResponse;
import com.demoProject.demo.model.entity.Payment;

public interface PaymentService {
    PaymentResponse payBooking(PaymentRequest request);
}