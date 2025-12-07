package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.CampingService;
import com.demoProject.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampingServiceRepository extends JpaRepository<CampingService, String> {
}
