package com.demoProject.demo.model.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingResponse {
    private String bookingId;
    private String status;
}