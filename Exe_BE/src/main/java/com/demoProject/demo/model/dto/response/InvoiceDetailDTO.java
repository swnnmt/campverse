package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho chi tiết hóa đơn (mỗi phòng / dịch vụ trong hóa đơn).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailDTO {
    private String roomId;       // Mã phòng
    private String roomName;     // Tên phòng
    private String checkInDate;  // Ngày nhận phòng
    private String checkOutDate; // Ngày trả phòng
    private Double price;        // Giá tiền cho dịch vụ/phòng này
}
