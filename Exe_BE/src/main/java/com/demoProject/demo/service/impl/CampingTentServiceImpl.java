package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.request.CampingTentRequest;
import com.demoProject.demo.model.dto.response.CampingTentResponse;
import com.demoProject.demo.model.entity.CampingInfor;
import com.demoProject.demo.model.entity.CampingTent;
import com.demoProject.demo.repository.CampingInforRepository;
import com.demoProject.demo.repository.CampingTentRepository;
import com.demoProject.demo.service.CampingTentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampingTentServiceImpl implements CampingTentService {

    private final CampingTentRepository tentRepository;
    private final CampingInforRepository campingRepository;

    @Override
    public CampingTentResponse createTent(CampingTentRequest request) {
        CampingInfor camping = campingRepository.findById(request.getCampingId())
                .orElseThrow(() -> new RuntimeException("Camping not found"));

        CampingTent tent = CampingTent.builder()
                .camping(camping)
                .tentName(request.getTentName())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .quantity(request.getQuantity())
                .thumbnail(request.getThumbnail())
                .build();

        tentRepository.save(tent);
        return mapToResponse(tent);
    }

        @Override
        public CampingTentResponse getTentById(String id) {
            CampingTent tent = tentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tent not found"));
            return mapToResponse(tent);
        }

    @Override
    public List<CampingTentResponse> getTentsByCamping(String campingId) {
        return tentRepository.findByCamping_Id(campingId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public CampingTentResponse updateTent(String id, CampingTentRequest request) {
        CampingTent tent = tentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tent not found"));

        tent.setTentName(request.getTentName());
        tent.setCapacity(request.getCapacity());
        tent.setPricePerNight(request.getPricePerNight());
        tent.setQuantity(request.getQuantity());
        tent.setThumbnail(request.getThumbnail());

        tentRepository.save(tent);
        return mapToResponse(tent);
    }

    @Override
    public void deleteTent(String id) {
        tentRepository.deleteById(id);
    }

    private CampingTentResponse mapToResponse(CampingTent tent) {
        return CampingTentResponse.builder()
                .id(tent.getId())
                .campingId(tent.getCamping().getId())
                .tentName(tent.getTentName())
                .capacity(tent.getCapacity())
                .pricePerNight(tent.getPricePerNight())
                .quantity(tent.getQuantity())
                .thumbnail(tent.getThumbnail())
                .build();
    }
}
