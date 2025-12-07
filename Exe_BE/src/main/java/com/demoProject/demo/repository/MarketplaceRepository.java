package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.MarketplaceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketplaceRepository extends JpaRepository<MarketplaceItem, String> {
    List<MarketplaceItem> findByUserId(String userId);
}
