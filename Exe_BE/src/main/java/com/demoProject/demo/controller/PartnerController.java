// src/main/java/com/demoProject/demo/controller/PartnerController.java
package com.demoProject.demo.controller;

import com.demoProject.demo.common.payload.Response;
import com.demoProject.demo.model.dto.request.RegisterPartnerRequest;
import com.demoProject.demo.model.dto.response.RegisterPartnerResponse;
import com.demoProject.demo.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/partner")
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerPartner(@Valid @RequestBody RegisterPartnerRequest request) {
        RegisterPartnerResponse result = partnerService.registerPartner(request); // call once
        return ResponseEntity.ok(Response.ofSucceeded(result));
    }
}