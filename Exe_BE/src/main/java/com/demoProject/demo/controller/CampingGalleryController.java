package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.request.CampingGalleryRequest;
import com.demoProject.demo.model.dto.response.CampingGalleryResponse;
import com.demoProject.demo.service.CampingGalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/galleries")
@RequiredArgsConstructor
public class CampingGalleryController {

    private final CampingGalleryService galleryService;

    @PostMapping
    public ResponseEntity<CampingGalleryResponse> createGallery(@RequestBody CampingGalleryRequest request) {
        return ResponseEntity.ok(galleryService.createGallery(request));
    }

    @GetMapping("/{campingId}")
    public ResponseEntity<List<CampingGalleryResponse>> getGalleries(@PathVariable String campingId) {
        return ResponseEntity.ok(galleryService.getGalleriesByCamping(campingId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable String id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }
}
