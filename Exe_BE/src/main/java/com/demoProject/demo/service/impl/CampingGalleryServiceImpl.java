package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.request.CampingGalleryRequest;
import com.demoProject.demo.model.dto.response.CampingGalleryResponse;
import com.demoProject.demo.model.entity.CampingGallery;
import com.demoProject.demo.model.entity.CampingInfor;
import com.demoProject.demo.repository.CampingGalleryRepository;
import com.demoProject.demo.repository.CampingInforRepository;
import com.demoProject.demo.service.CampingGalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampingGalleryServiceImpl implements CampingGalleryService {

    private final CampingGalleryRepository galleryRepository;
    private final CampingInforRepository campingRepository;

    @Override
    public CampingGalleryResponse createGallery(CampingGalleryRequest request) {
        CampingInfor camping = campingRepository.findById(request.getCampingId())
                .orElseThrow(() -> new RuntimeException("Camping not found"));

        CampingGallery gallery = CampingGallery.builder()
                .id(UUID.randomUUID().toString()) // tạo id mới kiểu String
                .camping(camping)
                .imageUrl(request.getImageUrl())
                .build(); // bỏ description

        galleryRepository.save(gallery);

        return mapToResponse(gallery);
    }

    @Override
    public List<CampingGalleryResponse> getGalleriesByCamping(String campingId) {
        return galleryRepository.findByCamping_Id(campingId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGallery(String id) {
        galleryRepository.deleteById(id);
    }

    private CampingGalleryResponse mapToResponse(CampingGallery gallery) {
        return CampingGalleryResponse.builder()
                .id(gallery.getId())
                .campingId(gallery.getCamping().getId())
                .imageUrl(gallery.getImageUrl())
                .build(); // bỏ description
    }
}
