package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.CampingGalleryRequest;
import com.demoProject.demo.model.dto.response.CampingGalleryResponse;

import java.util.List;

public interface CampingGalleryService {
    CampingGalleryResponse createGallery(CampingGalleryRequest request);
    List<CampingGalleryResponse> getGalleriesByCamping(String campingId);
    void deleteGallery(String id);
}
