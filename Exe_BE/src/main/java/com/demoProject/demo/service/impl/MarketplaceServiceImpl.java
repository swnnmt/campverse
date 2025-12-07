package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.request.MarketplaceRequest;
import com.demoProject.demo.model.dto.response.MarketplaceResponse;
import com.demoProject.demo.model.entity.MarketplaceItem;
import com.demoProject.demo.repository.MarketplaceRepository;
import com.demoProject.demo.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketplaceServiceImpl implements MarketplaceService {

    private final MarketplaceRepository repository;

    @Override
    public MarketplaceResponse createItem(MarketplaceRequest request) {
        MarketplaceItem item = MarketplaceItem.builder()
                .userId(request.getUserId())
                .phone(request.getPhone())
                .facebookLink(request.getFacebookLink())
                .productName(request.getProductName())
                .productImage(request.getProductImage())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
        repository.save(item);
        return mapToResponse(item);
    }

    @Override
    public MarketplaceResponse updateItem(String id, MarketplaceRequest request) {
        MarketplaceItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setPhone(request.getPhone());
        item.setFacebookLink(request.getFacebookLink());
        item.setProductName(request.getProductName());
        item.setProductImage(request.getProductImage());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());

        repository.save(item);
        return mapToResponse(item);
    }

    @Override
    public void deleteItem(String id) {
        repository.deleteById(id);
    }

    @Override
    public MarketplaceResponse getItemById(String id) {
        MarketplaceItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToResponse(item);
    }

    @Override
    public List<MarketplaceResponse> getAllItems() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketplaceResponse> getItemsByUser(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MarketplaceResponse mapToResponse(MarketplaceItem item) {
        return MarketplaceResponse.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .phone(item.getPhone())
                .facebookLink(item.getFacebookLink())
                .productName(item.getProductName())
                .productImage(item.getProductImage())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .description(item.getDescription())
                .build();
    }
}
