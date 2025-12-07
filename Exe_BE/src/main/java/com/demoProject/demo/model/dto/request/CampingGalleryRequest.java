package com.demoProject.demo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingGalleryRequest {
    private String campingId;     // Tham chiếu đến CampingInfor
    private String imageUrl;
    // Bỏ description
}
