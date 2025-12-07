package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.CampingTentRequest;
import com.demoProject.demo.model.dto.response.CampingTentResponse;

import java.util.List;

public interface CampingTentService {
    CampingTentResponse createTent(CampingTentRequest request);
    List<CampingTentResponse> getTentsByCamping(String campingId);
    CampingTentResponse updateTent(String id, CampingTentRequest request);
    void deleteTent(String id);
    CampingTentResponse getTentById(String id);

}
