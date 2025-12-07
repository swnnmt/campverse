package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.request.ServiceRequest;
import com.demoProject.demo.model.dto.response.ServiceResponse;
import com.demoProject.demo.model.entity.ServiceEntity;
import com.demoProject.demo.repository.ServiceRepository;
import com.demoProject.demo.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;

    @Override
    public ServiceResponse createService(ServiceRequest request) {
        ServiceEntity service = ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        repository.save(service);
        return toResponse(service);
    }

    @Override
    public ServiceResponse updateService(String id, ServiceRequest request) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        repository.save(service);
        return toResponse(service);
    }

    @Override
    public ServiceResponse getServiceById(String id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return toResponse(service);
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteService(String id) {
        repository.deleteById(id);
    }

    private ServiceResponse toResponse(ServiceEntity service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .build();
    }
}
