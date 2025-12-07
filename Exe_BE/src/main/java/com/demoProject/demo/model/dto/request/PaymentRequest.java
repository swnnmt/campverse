package com.demoProject.demo.model.dto.request;

import com.demoProject.demo.common.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {
    private String bookingId;
    private PaymentMethod paymentMethod;
}