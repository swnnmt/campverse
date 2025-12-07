package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.response.InvoiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceAdminService {
    Page<InvoiceDTO> getAllInvoices(Pageable pageable);
    InvoiceDTO getInvoiceById(String id);
    InvoiceDTO updateInvoiceStatus(String id, String status);
    void deleteInvoice(String id);

    Page<InvoiceDTO> searchInvoices(String bookingId,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);
}
