package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.entity.Booking;
import com.demoProject.demo.model.entity.User;

import com.demoProject.demo.model.dto.response.CampingSiteBookingResponse;
import com.demoProject.demo.model.dto.response.InvoiceResponse;
import com.demoProject.demo.model.dto.response.TopUserReponse;
import com.demoProject.demo.model.dto.response.DashBoardResponse;
import com.demoProject.demo.model.dto.response.MonthlyRevenueResponse;
import com.demoProject.demo.repository.BookingRepository;
import com.demoProject.demo.repository.CampingInforRepository;
import com.demoProject.demo.repository.ReviewRepository;
import com.demoProject.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.stream.Collectors;
import java.util.List;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

        private final BookingRepository bookingRepository;
        private final CampingInforRepository campingInforRepository;
        private final ReviewRepository reviewRepository;

        @Override
        public DashBoardResponse getDashboardData() {
                // Tháng trước
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                LocalDateTime lastStart = lastMonth.atDay(1).atStartOfDay();
                LocalDateTime lastEnd = lastMonth.atEndOfMonth().atTime(23, 59, 59);

                // Tháng hiện tại
                YearMonth thisMonth = YearMonth.now();
                LocalDateTime thisStart = thisMonth.atDay(1).atStartOfDay();
                LocalDateTime thisEnd = thisMonth.atEndOfMonth().atTime(23, 59, 59);

                Long totalBookingsLastMonth = bookingRepository.countBookingsInRange(lastStart, lastEnd);
                Double totalRevenueLastMonth = bookingRepository.sumRevenueInRange(lastStart, lastEnd);

                Long totalBookingsThisMonth = bookingRepository.countBookingsInRange(thisStart, thisEnd);
                Double totalRevenueThisMonth = bookingRepository.sumRevenueInRange(thisStart, thisEnd);

                Long totalCampingSites = campingInforRepository.count();
                Long totalReviews = reviewRepository.count();

                return new DashBoardResponse(
                                totalBookingsLastMonth != null ? totalBookingsLastMonth : 0L,
                                totalRevenueLastMonth != null ? totalRevenueLastMonth : 0.0,
                                totalBookingsThisMonth != null ? totalBookingsThisMonth : 0L,
                                totalRevenueThisMonth != null ? totalRevenueThisMonth : 0.0,
                                totalCampingSites,
                                totalReviews);
        }

        @Override
        public List<CampingSiteBookingResponse> findTopCampingSitesByBooking(int year, int limit) {
                List<Object[]> results = bookingRepository.findTopCampingSitesByBookings(year,
                                PageRequest.of(0, limit));
                return results.stream()
                                .map(obj -> new CampingSiteBookingResponse(
                                                (String) obj[0],
                                                (Long) obj[1]))
                                .collect(Collectors.toList());
        }

        @Override
        public List<MonthlyRevenueResponse> getMonthlyRevenue(int year) {
                List<Object[]> results = bookingRepository.findMonthlyRevenueAndBookings(year);

                return results.stream()
                                .map(obj -> new MonthlyRevenueResponse(
                                                (Integer) obj[0], // month
                                                obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0, // totalRevenue
                                                obj[2] != null ? ((Number) obj[2]).longValue() : 0L // totalBookings
                                ))
                                .collect(Collectors.toList());
        }

        @Override
        public List<TopUserReponse> getTopUsersLastMonth() {
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                LocalDateTime start = lastMonth.atDay(1).atStartOfDay();
                LocalDateTime end = lastMonth.atEndOfMonth().atTime(23, 59, 59);

                List<Object[]> results = bookingRepository.findTopUsersByBookingCount(start, end);

                return results.stream()
                                .map((Object[] arr) -> {
                                        User user = (User) arr[0];
                                        Number cnt = (Number) arr[1];
                                        return new TopUserReponse(user.getId(), user.getUserInfo().getFullName(),
                                                        cnt.longValue());
                                })
                                .limit(5)
                                .collect(Collectors.toList());
        }

        @Override
        public List<InvoiceResponse> getLatestInvoices() {
                return bookingRepository.findTop5ByOrderByCreatedAtDesc()
                                .stream()
                                .map(b -> new InvoiceResponse(
                                                b.getId(),
                                                b.getUser().getUserInfo().getFullName(),
                                                b.getTotalPrice(),
                                                b.getCreatedAt().toString(),
                                                b.getStatus()))
                                .collect(Collectors.toList());
        }

}
