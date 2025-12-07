package com.demoProject.demo.controller.admin;

import com.demoProject.demo.model.dto.response.CampingSiteBookingResponse;
import com.demoProject.demo.model.dto.response.DashBoardResponse;
import com.demoProject.demo.model.dto.response.MonthlyRevenueResponse;
import com.demoProject.demo.model.dto.response.InvoiceResponse;
import com.demoProject.demo.model.dto.response.TopUserReponse;
import com.demoProject.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Dashboard tổng (số liệu tháng trước, tháng này, tổng campsite, review...)
    @GetMapping
    public DashBoardResponse getDashboard() {
        return dashboardService.getDashboardData();
    }

    // Top N campsite có lượt booking nhiều nhất trong năm
    @GetMapping("/top-camping-sites")
    public List<CampingSiteBookingResponse> getTopCampingSites(
            @RequestParam int year,
            @RequestParam(defaultValue = "6") int limit
    ) {
        return dashboardService.findTopCampingSitesByBooking(year, limit);
    }

    // Biểu đồ doanh thu & số booking theo từng tháng trong năm
    @GetMapping("/monthly-revenue")
    public List<MonthlyRevenueResponse> getMonthlyRevenue(
            @RequestParam int year
    ) {
        return dashboardService.getMonthlyRevenue(year);
    }

    @GetMapping("/top-users")
    public List<TopUserReponse> getTopUsersLastMonth() {
        return dashboardService.getTopUsersLastMonth();
    }

    @GetMapping("/latest-invoices")
    public List<InvoiceResponse> getLatestInvoices() {
        return dashboardService.getLatestInvoices();
    }
}
