package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.BookingRequest;
import com.demoProject.demo.model.dto.request.UpdateBookingRequest;
import com.demoProject.demo.model.dto.response.BookingByUserIdResponse;
import com.demoProject.demo.model.dto.response.BookingResponse;
import com.demoProject.demo.model.dto.response.BookingByCampingIdResponse;
import com.demoProject.demo.model.entity.Booking;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);

    Page<BookingByUserIdResponse> getBookingsByUserId(String userId, int page, int size);

    Page<BookingByCampingIdResponse> getBookingsByCampingId(String campingId, int page, int size);

    BookingResponse updateBooking(String bookingId, UpdateBookingRequest request);

    void cancelBooking(String bookingId);
    void completedBooking(String bookingId);
    void confirmBoking(String bookingId);
    Page<BookingByUserIdResponse> getBookings(int page, int size);
}