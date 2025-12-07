package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.ServiceRequest;
import com.demoProject.demo.model.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse updateService(String id, ServiceRequest request);
    ServiceResponse getServiceById(String id);
    List<ServiceResponse> getAllServices();
    void deleteService(String id);
}
