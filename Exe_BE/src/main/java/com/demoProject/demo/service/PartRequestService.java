package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.response.PartnerResponse;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

public interface PartRequestService {
    Page<PartnerResponse> getPendingPartners(int page, int size);

    PartnerResponse getPartnerDetail(String partnerId);

    void approvePartner(String partnerId) throws MessagingException;

    void rejectPartner(String partnerId) throws MessagingException;
}
