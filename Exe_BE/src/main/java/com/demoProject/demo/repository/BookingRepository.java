package com.demoProject.demo.repository;

import com.demoProject.demo.model.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

   @Query("SELECT COUNT(b) FROM Booking b " +
       "WHERE b.createdAt BETWEEN :start AND :end " +
       "AND b.status = 'COMPLETED'")
Long countBookingsInRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

@Query("SELECT SUM(b.totalPrice) FROM Booking b " +
       "WHERE b.createdAt BETWEEN :start AND :end " +
       "AND b.status = 'COMPLETED'")
Double sumRevenueInRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    @Query("SELECT MONTH(b.createdAt), COALESCE(SUM(b.totalPrice),0), COUNT(b) " +
            "FROM Booking b " +
            "WHERE YEAR(b.createdAt) = :year " +
            "AND b.status = 'COMPLETED' " +
            "GROUP BY MONTH(b.createdAt) " +
            "ORDER BY MONTH(b.createdAt)")
    List<Object[]> findMonthlyRevenueAndBookings(int year);

    // Tổng doanh thu cả năm
    @Query(value = """
            SELECT COALESCE(SUM(b.total_price),0)
            FROM tbl_booking b
            WHERE YEAR(b.created_at) = :year
            """, nativeQuery = true)
    BigDecimal findTotalIncomeForYear(@Param("year") int year);


    @Query("SELECT b.campingSite.location, COUNT(b) AS totalBookings " +
       "FROM Booking b " +
       "WHERE YEAR(b.createdAt) = :year " +
       "GROUP BY b.campingSite.location " +
       "ORDER BY totalBookings DESC")
    List<Object[]> findTopCampingSitesByBookings(int year, org.springframework.data.domain.Pageable pageable);


   @Query("SELECT b.user, COUNT(b) as bookingCount " +
           "FROM Booking b " +
           "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY b.user " +
           "ORDER BY bookingCount DESC")
    List<Object[]> findTopUsersByBookingCount(LocalDateTime startDate, LocalDateTime endDate);


    // Lấy 5 booking mới nhất (hóa đơn gần nhất)
    List<Booking> findTop5ByOrderByCreatedAtDesc();


    Page<Booking> findByIdContainingIgnoreCase(String id, Pageable pageable);

    // Tìm kiếm theo startTime và endTime
    Page<Booking> findByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Kết hợp bookingId + startTime + endTime
    Page<Booking> findByIdContainingIgnoreCaseAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            String id,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

//    @EntityGraph(attributePaths = {
//            "details",
//            "details.room",
//            "details.campingTent",
//            "details.campingService",
//            "details.campingService.service"
//    })
    Page<Booking> findByUserId(String userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.campingInfor.id = :campingId")
    Page<Booking> findByCampingId(@Param("campingId") String campingId, Pageable pageable);
}
