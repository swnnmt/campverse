package com.demoProject.demo.controller.user;


import com.demoProject.demo.model.dto.request.MarketplaceRequest;
import com.demoProject.demo.model.dto.response.MarketplaceResponse;
import com.demoProject.demo.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceService service;

    @PostMapping
    public ResponseEntity<MarketplaceResponse> createItem(@RequestBody MarketplaceRequest request) {
        return ResponseEntity.ok(service.createItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketplaceResponse> updateItem(@PathVariable String id,
                                                          @RequestBody MarketplaceRequest request) {
        return ResponseEntity.ok(service.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketplaceResponse> getItem(@PathVariable String id) {
        return ResponseEntity.ok(service.getItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<MarketplaceResponse>> getAllItems() {
        return ResponseEntity.ok(service.getAllItems());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MarketplaceResponse>> getItemsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getItemsByUser(userId));
    }
}
