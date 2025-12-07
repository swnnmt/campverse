package com.demoProject.demo.controller.user;

import com.demoProject.demo.model.dto.request.SearchCampingRequest;
import com.demoProject.demo.model.dto.response.SearchCampingResponse;
import com.demoProject.demo.service.CampingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final CampingService campingService;

    @PostMapping("/camping")
    public ResponseEntity<?> searchCamping(@Valid @RequestBody SearchCampingRequest request) {
        var response = campingService.searchCamping(request);
        return ResponseEntity.ok(response);
    }


}