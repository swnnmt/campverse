package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.RegisterPartnerRequest;
import com.demoProject.demo.model.dto.response.RegisterPartnerResponse;
import com.demoProject.demo.model.dto.response.RegistrationResponse;

public interface PartnerService {
    RegisterPartnerResponse registerPartner(RegisterPartnerRequest request);
}
