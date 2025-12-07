package com.demoProject.demo.controller.user;

import com.demoProject.demo.model.dto.request.BookingRequest;
import com.demoProject.demo.model.dto.request.UpdateBookingRequest;
import com.demoProject.demo.model.dto.response.BookingByCampingIdResponse;
import com.demoProject.demo.model.dto.response.BookingByUserIdResponse;
import com.demoProject.demo.model.dto.response.BookingResponse;
import com.demoProject.demo.model.entity.Booking;
import com.demoProject.demo.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Ensure the user is authenticated
    public ResponseEntity<BookingResponse> createBooking(@Validated @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingByUserIdResponse>> getBookingsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookingByUserIdResponse> responses = bookingService.getBookingsByUserId(userId, page, size);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{bookingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable String bookingId,
            @Validated @RequestBody UpdateBookingRequest request) {
        BookingResponse response = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Failed to cancel booking");
        }
    }
    @PutMapping("/{bookingId}/completed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> completedBooking(@PathVariable String bookingId) {
        try {
            bookingService.completedBooking(bookingId);
            return ResponseEntity.ok("Booking completed successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Failed to complete booking");
        }
    }

    @PutMapping("/{bookingId}/confirmed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmedBooking(@PathVariable String bookingId) {
        try {
            bookingService.completedBooking(bookingId);
            return ResponseEntity.ok("Booking confirm successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Failed to confirm booking");
        }
    }
}