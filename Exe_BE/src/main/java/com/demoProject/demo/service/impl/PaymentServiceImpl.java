package com.demoProject.demo.service.impl;

import com.demoProject.demo.common.enums.BookingStatus;
import com.demoProject.demo.model.dto.request.PaymentRequest;
import com.demoProject.demo.model.dto.response.PaymentResponse;
import com.demoProject.demo.model.entity.*;
import com.demoProject.demo.common.enums.PaymentStatus;
import com.demoProject.demo.common.enums.PaymentMethod;
import com.demoProject.demo.repository.BookingRepository;
import com.demoProject.demo.repository.PaymentRepository;
import com.demoProject.demo.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse payBooking(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        double total = booking.getDetails().stream()
                .mapToDouble(detail -> {
                    double price = 0.0;
                    if (detail.getCampingTent() != null)
                        price += detail.getCampingTent().getPricePerNight();
                    if (detail.getCampingService() != null)
                        price += detail.getCampingService().getPrice();
                    if (detail.getPrice() != null)
                        price += detail.getPrice();
                    return price;
                })
                .sum();

        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .booking(booking)
                .amount(total)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        // ✅ Nếu là BANK_TRANSFER → tạo QR code (ví dụ)
        if (request.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" +
                    "BankTransfer|Amount=" + total + "|Booking=" + booking.getId();
            // (sau này có thể thay bằng VNPay hoặc Momo link thực tế)
            payment.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        }

        paymentRepository.save(payment);
        booking.setPayment(payment);
        bookingRepository.save(booking);

        // ✅ Tạo response rõ ràng
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(booking.getId())
                .amount(total)
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .qrCode(request.getPaymentMethod() == PaymentMethod.BANK_TRANSFER
                        ? "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" +
                        "BankTransfer|Amount=" + total + "|Booking=" + booking.getId()
                        : null)
                .bankAccount("MB Bank - 123456789 - Campverse Co.")
                .payUrl(null) // hoặc link VNPay nếu có
                .paymentDate(payment.getPaymentDate())
                .build();
    }


    // ✅ Hàm tạo QR giả lập (có thể tích hợp thật sau)
    private String generateBankTransferQR(double amount, String bookingId) {
        String bankCode = "VCB"; // Mã ngân hàng (Vietcombank)
        String accountNumber = "9704361234567899";
        String content = "Thanh toan booking " + bookingId;

        // encode nội dung thành URL-safe
        String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8);

        return "https://img.vietqr.io/image/"
                + bankCode + "-" + accountNumber
                + "-compact.png?amount=" + (int) amount
                + "&addInfo=" + encodedContent;
    }

}
