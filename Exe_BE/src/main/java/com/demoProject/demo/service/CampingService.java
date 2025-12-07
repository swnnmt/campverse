package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.SearchCampingRequest;
import com.demoProject.demo.model.dto.response.CampingInforResponse;
import com.demoProject.demo.model.dto.response.CampingRoomListResponse;
import com.demoProject.demo.model.dto.response.SearchCampingResponse;
import com.demoProject.demo.model.dto.response.CampingSiteSimpleResponse;

import java.util.List;

public interface CampingService {
    List<SearchCampingResponse> searchCamping(SearchCampingRequest request);
    List<CampingSiteSimpleResponse> getAllCampingSiteLocations();
    List<CampingRoomListResponse> getAllCampingRooms();

    List<CampingInforResponse> getCampingRoomsBySiteId(String campingSiteId);

    // New: search camping infos (rooms) by name
    List<CampingInforResponse> searchCampingInforsByName(String name);
}