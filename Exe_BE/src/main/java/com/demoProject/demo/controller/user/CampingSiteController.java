package com.demoProject.demo.controller.user;

import com.demoProject.demo.model.dto.response.CampingInforResponse;
import com.demoProject.demo.model.dto.response.CampingRoomListResponse;
import com.demoProject.demo.model.dto.response.CampingSiteSimpleResponse;
import com.demoProject.demo.service.CampingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/camping-sites")
@RequiredArgsConstructor
public class CampingSiteController {
    private final CampingService campingservice;

    @GetMapping
    public ResponseEntity<List<CampingSiteSimpleResponse>> getAllCampingSites() {
        return ResponseEntity.ok(campingservice.getAllCampingSiteLocations());
    }

    @GetMapping("/campinginfor")
    public ResponseEntity<List<CampingRoomListResponse>> getAllCampingRooms() {
        return ResponseEntity.ok(campingservice.getAllCampingRooms());
    }

    @GetMapping("/{campingSiteId}")
    public ResponseEntity<List<CampingInforResponse>> getCampingRoomsBySiteId(@PathVariable String campingSiteId) {
        return ResponseEntity.ok(campingservice.getCampingRoomsBySiteId(campingSiteId));
    }

    @GetMapping("/search-infors")
    public ResponseEntity<List<CampingInforResponse>> searchInforsByName(
            @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(campingservice.searchCampingInforsByName(name));
    }
}
