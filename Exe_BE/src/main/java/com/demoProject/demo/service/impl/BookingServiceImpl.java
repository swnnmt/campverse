package com.demoProject.demo.service.impl;

import com.demoProject.demo.common.enums.BookingStatus;
import com.demoProject.demo.model.dto.request.BookingRequest;
import com.demoProject.demo.model.dto.request.UpdateBookingRequest;
import com.demoProject.demo.model.dto.response.BookingByCampingIdResponse;
import com.demoProject.demo.model.dto.response.BookingByUserIdResponse;
import com.demoProject.demo.model.dto.response.BookingResponse;
import com.demoProject.demo.model.entity.*;
import com.demoProject.demo.repository.*;
import com.demoProject.demo.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CampingInforRepository campingInforRepository;
    private final UserRepository userRepository;
    private final CampingTentRepository campingTentRepository;
    private final CampingServiceRepository campingServiceRepository;
    private final BookingDetailRepository bookingDetailRepository;

    // ==========================
    // Create booking
    // ==========================
    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Lấy user hiện tại từ SecurityContext (bảo đảm an toàn)
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserInfoEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy CampingInfor (phải chính xác)
        CampingInfor campingInfor = campingInforRepository.findById(request.getCampingInforId())
                .orElseThrow(() -> new IllegalArgumentException("Camping Infor not found"));

        // Tạo booking chính
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setUser(user);
        // Dùng campingInfor.getCampingSite() để đảm bảo nhất quán
        booking.setCampingSite(campingInfor.getCampingSite());
        booking.setCampingInfor(campingInfor);// lấy cả them cả campinginforId để lấy booking cho owner xem
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Lưu booking trước để có booking_id cho FK
        bookingRepository.save(booking);

        // Chuẩn bị details list
        List<BookingDetail> details = new ArrayList<>();

        // Lấy ngày checkin/checkout từ LocalDateTime (chỉ dùng phần LocalDate)
        LocalDate checkIn = request.getStartTime().toLocalDate();
        LocalDate checkOut = request.getEndTime().toLocalDate();

        // 1) Thêm BookingDetail cho CampingInfor (room) — tiện cho truy vấn chi tiết
        BookingDetail inforDetail = new BookingDetail();
        inforDetail.setId(UUID.randomUUID().toString());
        inforDetail.setBooking(booking);
        inforDetail.setRoom(campingInfor);
        // Giá room có thể là basePrice hoặc 0. Tùy cách bạn tính tổng. Ở đây mình set basePrice.
        inforDetail.setPrice(campingInfor.getBasePrice() != null ? campingInfor.getBasePrice() : 0.0);
        inforDetail.setCheckInDate(checkIn);
        inforDetail.setCheckOutDate(checkOut);
        details.add(inforDetail);

        // 2) Nếu có tent -> thêm BookingDetail và set campingTent
        if (request.getCampingTentId() != null) {
            CampingTent tent = campingTentRepository.findById(request.getCampingTentId())
                    .orElseThrow(() -> new IllegalArgumentException("Camping Tent not found"));
            BookingDetail tentDetail = new BookingDetail();
            tentDetail.setId(UUID.randomUUID().toString());
            tentDetail.setBooking(booking);
            tentDetail.setCampingTent(tent);
            tentDetail.setPrice(tent.getPricePerNight() != null ? tent.getPricePerNight() : 0.0);
            tentDetail.setCheckInDate(checkIn);
            tentDetail.setCheckOutDate(checkOut);
            details.add(tentDetail);
        }

        // 3) Nếu có services -> thêm từng BookingDetail và set campingService
        if (request.getCampingServiceIds() != null && !request.getCampingServiceIds().isEmpty()) {
            for (String serviceId : request.getCampingServiceIds()) {
                CampingService cs = campingServiceRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Camping Service not found: " + serviceId));
                BookingDetail sd = new BookingDetail();
                sd.setId(UUID.randomUUID().toString());
                sd.setBooking(booking);
                sd.setCampingService(cs);
                sd.setPrice(cs.getPrice() != null ? cs.getPrice() : 0.0);
                sd.setCheckInDate(checkIn);
                sd.setCheckOutDate(checkOut);
                details.add(sd);
            }
        }

        // Lưu tất cả booking details (sẽ ghi đúng booking_id và các FK tent/service/room)
        bookingDetailRepository.saveAll(details);

        // (tuỳ chọn) gán lại details vào booking và save nếu bạn muốn trả về booking với details eager
        booking.setDetails(details);
        bookingRepository.save(booking);

        return new BookingResponse(booking.getId(), "SUCCESS");
    }

    // ==========================
    // Get bookings by user id
    // ==========================
    @Override
    public Page<BookingByUserIdResponse> getBookingsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookingsPage = bookingRepository.findByUserId(userId, pageable);

        return bookingsPage.map(booking -> {
            BookingByUserIdResponse resp = new BookingByUserIdResponse();
            resp.setBookingId(booking.getId());
            resp.setUserId(booking.getUser() != null ? booking.getUser().getId() : null);
            resp.setCampingSiteId(booking.getCampingSite() != null ? booking.getCampingSite().getId() : null);

            String campingInforId = null;
            String campingTentId = null;
            List<String> serviceNames = new ArrayList<>();

            if (booking.getDetails() != null && !booking.getDetails().isEmpty()) {
                for (BookingDetail detail : booking.getDetails()) {
                    if (detail.getRoom() != null) {
                        campingInforId = detail.getRoom().getId();
                    }
                    if (detail.getCampingTent() != null) {
                        campingTentId = detail.getCampingTent().getId();
                    }
                    if (detail.getCampingService() != null) {
                        CampingService cs = detail.getCampingService();
                        if (cs.getService() != null && cs.getService().getName() != null) {
                            serviceNames.add(cs.getService().getName());
                        } else if (cs.getCustomName() != null) {
                            serviceNames.add(cs.getCustomName());
                        }
                    }
                }
            }

            resp.setCampingInforId(campingInforId);
            resp.setCampingTentId(campingTentId);
            resp.setServiceNames(serviceNames);
            resp.setStartTime(booking.getStartTime());
            resp.setEndTime(booking.getEndTime());
            resp.setTotalPrice(booking.getTotalPrice());
            resp.setStatus(booking.getStatus() != null ? booking.getStatus().name() : null);

            return resp;
        });
    }

    @Override
    public Page<BookingByCampingIdResponse> getBookingsByCampingId(String campingId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookingsPage = bookingRepository.findByCampingId(campingId, pageable);

        return bookingsPage.map(booking -> {
            BookingByCampingIdResponse resp = new BookingByCampingIdResponse();
            resp.setBookingId(booking.getId());
            resp.setUserId(booking.getUser() != null ? booking.getUser().getId() : null);

            // Populate only the user's name (no User object to serialize)
            if (booking.getUser() != null && booking.getUser().getUserInfo() != null) {
                UserInfo ui = booking.getUser().getUserInfo();
                String first = ui.getFirstName() != null ? ui.getFirstName().trim() : "";
                String last = ui.getLastName() != null ? ui.getLastName().trim() : "";
                String fullName = (first + " " + last).trim();
                if (fullName.isEmpty()) {
                    fullName = ui.getEmail() != null ? ui.getEmail() : null;
                }
                resp.setUserName(fullName);
            } else {
                resp.setUserName(null);
            }

            resp.setCampingSiteId(booking.getCampingSite() != null ? booking.getCampingSite().getId() : null);

            String campingInforId = null;
            String campingTentId = null;
            List<String> serviceNames = new ArrayList<>();

            if (booking.getDetails() != null && !booking.getDetails().isEmpty()) {
                for (BookingDetail detail : booking.getDetails()) {
                    if (detail.getRoom() != null) {
                        campingInforId = detail.getRoom().getId();
                    }
                    if (detail.getCampingTent() != null) {
                        campingTentId = detail.getCampingTent().getId();
                    }
                    if (detail.getCampingService() != null) {
                        CampingService cs = detail.getCampingService();
                        if (cs.getService() != null && cs.getService().getName() != null) {
                            serviceNames.add(cs.getService().getName());
                        } else if (cs.getCustomName() != null) {
                            serviceNames.add(cs.getCustomName());
                        }
                    }
                }
            }

            resp.setCampingInforId(campingInforId);
            resp.setCampingTentId(campingTentId);
            resp.setServiceNames(serviceNames);
            resp.setStartTime(booking.getStartTime());
            resp.setEndTime(booking.getEndTime());
            resp.setTotalPrice(booking.getTotalPrice());
            resp.setStatus(booking.getStatus() != null ? booking.getStatus().name() : null);
            resp.setCreatedAt(booking.getCreatedAt());
            return resp;
        });
    }
    // ==========================
    // Update booking
    // ==========================
    @Override
    @Transactional
    public BookingResponse updateBooking(String bookingId, UpdateBookingRequest request) {
        // Lấy user hiện tại
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserInfoEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Kiểm tra quyền
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        // Lấy campingInfor mới
        CampingInfor campingInfor = campingInforRepository.findById(request.getCampingInforId())
                .orElseThrow(() -> new IllegalArgumentException("Camping Infor not found"));

        booking.setCampingSite(campingInfor.getCampingSite());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Xóa detail cũ (nếu có)
        if (booking.getDetails() != null && !booking.getDetails().isEmpty()) {
            bookingDetailRepository.deleteAll(booking.getDetails());
        }

        // Tạo và lưu detail mới (tương tự create)
        List<BookingDetail> newDetails = new ArrayList<>();
        LocalDate checkIn = request.getStartTime().toLocalDate();
        LocalDate checkOut = request.getEndTime().toLocalDate();

        BookingDetail infoDetail = new BookingDetail();
        infoDetail.setId(UUID.randomUUID().toString());
        infoDetail.setBooking(booking);
        infoDetail.setRoom(campingInfor);
        infoDetail.setPrice(campingInfor.getBasePrice() != null ? campingInfor.getBasePrice() : 0.0);
        infoDetail.setCheckInDate(checkIn);
        infoDetail.setCheckOutDate(checkOut);
        newDetails.add(infoDetail);

        if (request.getCampingTentId() != null) {
            CampingTent tent = campingTentRepository.findById(request.getCampingTentId())
                    .orElseThrow(() -> new IllegalArgumentException("Camping Tent not found"));
            BookingDetail tentDetail = new BookingDetail();
            tentDetail.setId(UUID.randomUUID().toString());
            tentDetail.setBooking(booking);
            tentDetail.setCampingTent(tent);
            tentDetail.setPrice(tent.getPricePerNight() != null ? tent.getPricePerNight() : 0.0);
            tentDetail.setCheckInDate(checkIn);
            tentDetail.setCheckOutDate(checkOut);
            newDetails.add(tentDetail);
        }

        if (request.getCampingServiceIds() != null && !request.getCampingServiceIds().isEmpty()) {
            for (String serviceId : request.getCampingServiceIds()) {
                CampingService cs = campingServiceRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Camping Service not found"));
                BookingDetail sd = new BookingDetail();
                sd.setId(UUID.randomUUID().toString());
                sd.setBooking(booking);
                sd.setCampingService(cs);
                sd.setPrice(cs.getPrice() != null ? cs.getPrice() : 0.0);
                sd.setCheckInDate(checkIn);
                sd.setCheckOutDate(checkOut);
                newDetails.add(sd);
            }
        }

        bookingDetailRepository.saveAll(newDetails);
        booking.setDetails(newDetails);
        bookingRepository.save(booking);

        return new BookingResponse(booking.getId(), "UPDATED");
    }

    // ==========================
    // Delete booking
    // ==========================
    @Override
    @Transactional
    public void cancelBooking(String bookingId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserInfoEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🔹 2. Tìm booking theo ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // 🔹 3. Kiểm tra quyền sở hữu booking
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }

        // 🔹 4. Chỉ cho phép hủy nếu booking đang ở trạng thái "PENDING"
        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus().name()) && !"PENDING".equalsIgnoreCase(booking.getStatus().name())) {
            throw new IllegalArgumentException("Only PENDING bookings can be cancelled");
        }

        // 🔹 5. Cập nhật trạng thái sang "CANCELLED"
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }
    @Override
    @Transactional
    public void completedBooking(String bookingId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserInfoEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🔹 2. Tìm booking theo ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // 🔹 4. Chỉ cho phép hủy nếu booking đang ở trạng thái "PENDING"
        if (!"PENDING".equalsIgnoreCase(booking.getStatus().name())) {
            throw new IllegalArgumentException("Only PENDING bookings can be cancelled");
        }

        // 🔹 5. Cập nhật trạng thái sang "CANCELLED"
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void confirmBoking(String bookingId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserInfoEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🔹 2. Tìm booking theo ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // 🔹 4. Chỉ cho phép hủy nếu booking đang ở trạng thái "PENDING"
        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus().name()) && !"PENDING".equalsIgnoreCase(booking.getStatus().name())) {
            throw new IllegalArgumentException("Only PENDING bookings can be cancelled");
        }
        // 🔹 5. Cập nhật trạng thái sang "CANCELLED"
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    // ==========================
    // Get all bookings (for admin)
    // ==========================
    @Override
    public Page<BookingByUserIdResponse> getBookings(int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // Lấy tất cả bookings (mọi người dùng)
        Page<Booking> bookingsPage = bookingRepository.findAll(pageable);

        // Ánh xạ trực tiếp trong map
        return bookingsPage.map(booking -> {
            BookingByUserIdResponse resp = new BookingByUserIdResponse();

            // --- Thông tin cơ bản ---
            resp.setBookingId(booking.getId());
            resp.setUserId(booking.getUser() != null ? booking.getUser().getId() : null);
            resp.setCampingSiteId(booking.getCampingSite() != null ? booking.getCampingSite().getId() : null);
            resp.setStartTime(booking.getStartTime());
            resp.setEndTime(booking.getEndTime());
            resp.setTotalPrice(booking.getTotalPrice());
            resp.setStatus(booking.getStatus() != null ? booking.getStatus().name() : null);

            // --- Chi tiết booking ---
            String campingInforId = null;
            String campingTentId = null;
            List<String> serviceNames = new ArrayList<>();

            if (booking.getDetails() != null && !booking.getDetails().isEmpty()) {
                for (BookingDetail detail : booking.getDetails()) {

                    if (detail.getRoom() != null) {
                        campingInforId = detail.getRoom().getId();
                    }

                    if (detail.getCampingTent() != null) {
                        campingTentId = detail.getCampingTent().getId();
                    }

                    if (detail.getCampingService() != null) {
                        CampingService cs = detail.getCampingService();

                        // Ưu tiên lấy tên service chuẩn, nếu không có thì lấy tên custom
                        if (cs.getService() != null && cs.getService().getName() != null) {
                            serviceNames.add(cs.getService().getName());
                        } else if (cs.getCustomName() != null) {
                            serviceNames.add(cs.getCustomName());
                        }
                    }
                }
            }

            resp.setCampingInforId(campingInforId);
            resp.setCampingTentId(campingTentId);
            resp.setServiceNames(serviceNames);

            return resp;
        });
    }

}

