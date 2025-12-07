package com.demoProject.demo.model.dto.response;

import com.demoProject.demo.common.enums.PaymentMethod;
import com.demoProject.demo.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String id;                      // Mã thanh toán
    private String bookingId;               // Mã booking liên kết
    private Double amount;                  // Số tiền thanh toán
    private PaymentMethod paymentMethod;    // Phương thức thanh toán (BANK_TRANSFER, MOMO...)
    private PaymentStatus paymentStatus;    // Trạng thái thanh toán
    private String qrCode;                  // Base64 hoặc URL mã QR (chỉ dùng khi BANK_TRANSFER)
    private String bankAccount;             // Số tài khoản nhận (tuỳ chọn, chỉ khi bank)
    private String payUrl;                  // Link chuyển hướng sang VNPay/Momo (nếu có)
    private LocalDateTime paymentDate;      // Thời gian tạo giao dịch
}