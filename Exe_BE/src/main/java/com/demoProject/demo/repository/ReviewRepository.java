package com.demoProject.demo.repository;
import com.demoProject.demo.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    boolean existsByBooking_Id(String bookingId);
    List<Review> findByCampingInfor_Id(String campingInforId);
}
