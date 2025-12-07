package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.MarketplaceRequest;
import com.demoProject.demo.model.dto.response.MarketplaceResponse;

import java.util.List;

public interface MarketplaceService {
    MarketplaceResponse createItem(MarketplaceRequest request);
    MarketplaceResponse updateItem(String id, MarketplaceRequest request);
    void deleteItem(String id);
    MarketplaceResponse getItemById(String id);
    List<MarketplaceResponse> getAllItems();
    List<MarketplaceResponse> getItemsByUser(String userId);
}
