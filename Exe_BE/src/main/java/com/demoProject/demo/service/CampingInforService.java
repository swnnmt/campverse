package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.CampingInforRequest;
import com.demoProject.demo.model.dto.response.CampingInforResponse;

import java.util.List;

public interface CampingInforService {
CampingInforResponse createCamping(CampingInforRequest request);
CampingInforResponse updateCamping(String id, CampingInforRequest request);
List<CampingInforResponse> getAllCamping();
CampingInforResponse getCampingById(String id);
void deleteCamping(String id);
}
