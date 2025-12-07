package com.demoProject.demo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponse {
    private String id; // sửa thành String
    private String name;
}
