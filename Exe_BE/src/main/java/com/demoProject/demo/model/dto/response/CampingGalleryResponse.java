package com.demoProject.demo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampingGalleryResponse {
    private String id;
    private String campingId;
    private String imageUrl;
}
