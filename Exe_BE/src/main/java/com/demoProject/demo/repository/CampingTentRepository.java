package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.CampingTent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampingTentRepository extends JpaRepository<CampingTent, String> {
    List<CampingTent> findByCamping_Id(String campingId);
}
