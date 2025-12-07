package com.demoProject.demo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityRequest {
    private String name; // chỉ cần name, bỏ description
    private String country; // nếu không cần country có thể bỏ luôn
}
