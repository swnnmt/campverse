package com.demoProject.demo.controller;

import com.demoProject.demo.model.dto.request.CampingTentRequest;
import com.demoProject.demo.model.dto.response.CampingTentResponse;
import com.demoProject.demo.service.CampingTentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tents")
@RequiredArgsConstructor
public class CampingTentController {

    private final CampingTentService tentService;

    @PostMapping
    public ResponseEntity<CampingTentResponse> createTent(@RequestBody CampingTentRequest request) {
        return ResponseEntity.ok(tentService.createTent(request));
    }

    @GetMapping("/byCampingId/{campingId}")
    public ResponseEntity<List<CampingTentResponse>> getTents(@PathVariable String campingId) {
        return ResponseEntity.ok(tentService.getTentsByCamping(campingId));
    }

    @GetMapping("/byTentId/{id}")
    public ResponseEntity<CampingTentResponse> getTentById(@PathVariable String id) {
        CampingTentResponse tent = tentService.getTentById(id);
        return ResponseEntity.ok(tent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampingTentResponse> updateTent(@PathVariable String id, @RequestBody CampingTentRequest request) {
        return ResponseEntity.ok(tentService.updateTent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTent(@PathVariable String id) {
        tentService.deleteTent(id);
        return ResponseEntity.noContent().build();
    }
}
