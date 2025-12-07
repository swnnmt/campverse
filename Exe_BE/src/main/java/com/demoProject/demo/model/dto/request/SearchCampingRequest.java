package com.demoProject.demo.model.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchCampingRequest {
    @NotNull
    private String destination;
    @NotNull private LocalDateTime startTime;
    @NotNull private LocalDateTime endTime;

}
