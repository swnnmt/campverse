
package com.demoProject.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopUserReponse {
    private String userId;
    private String fullName;
    private Long bookingCount;
}
