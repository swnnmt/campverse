package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private String invoiceId;       
    private String status;           
    private Double totalPrice;      
    private LocalDateTime createdAt; 
    private LocalDateTime startTime;
    private LocalDateTime endTime;  

    // Thông tin khách hàng
    private String customerName;
    private String customerEmail;
    private String customerPhone;

     private CampingSiteDTO campingSite;

    // Danh sách chi tiết hóa đơn
    private List<InvoiceDetailDTO> details;
}
