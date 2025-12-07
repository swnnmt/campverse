package com.demoProject.demo.model.dto.response;
import com.demoProject.demo.common.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceResponse {
    private String bookingId;
    private String customerName;
    private Double totalPrice;
    private String createdAt;
    private BookingStatus status;
}
