package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.response.DashBoardResponse;
import com.demoProject.demo.model.dto.response.InvoiceResponse;
import com.demoProject.demo.model.dto.response.TopUserReponse;
import com.demoProject.demo.model.dto.response.CampingSiteBookingResponse;
import com.demoProject.demo.model.dto.response.MonthlyRevenueResponse;
import java.util.List;

public interface DashboardService {
    DashBoardResponse getDashboardData();
    List<CampingSiteBookingResponse> findTopCampingSitesByBooking(int year, int limit);
    List<MonthlyRevenueResponse> getMonthlyRevenue(int year);
    List<TopUserReponse> getTopUsersLastMonth();
    List<InvoiceResponse> getLatestInvoices();
}
