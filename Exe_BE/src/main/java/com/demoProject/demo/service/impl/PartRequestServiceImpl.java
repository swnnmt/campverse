package com.demoProject.demo.service.impl;

import com.demoProject.demo.model.dto.response.CampingSiteResponse;
import com.demoProject.demo.model.dto.response.PartnerResponse;
import com.demoProject.demo.model.entity.CampingGallery;
import com.demoProject.demo.model.entity.CampingInfor;
import com.demoProject.demo.model.entity.CampingSite;
import com.demoProject.demo.model.entity.User;
import com.demoProject.demo.repository.AdminUserRepository;
import com.demoProject.demo.repository.CampingGalleryRepository;
import com.demoProject.demo.repository.CampingInforRepository;
import com.demoProject.demo.repository.CampingSiteRepository;
import com.demoProject.demo.service.EmailService;
import com.demoProject.demo.service.PartRequestService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartRequestServiceImpl implements PartRequestService {

    private final AdminUserRepository userRepository;
    private final CampingSiteRepository campingSiteRepository;
    private final CampingInforRepository campingInforRepository;
    private final CampingGalleryRepository campingGalleryRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final String PARTNER_PORTAL_URL = "https://campverse.com/partner/login";

    /**
     * Lấy danh sách partner đang chờ duyệt (phân trang)
     */
    @Override
    public Page<PartnerResponse> getPendingPartners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> partnersPage = userRepository.findByApproveStatus("PENDING", pageable);

        List<PartnerResponse> responses = partnersPage.getContent().stream()
                .map(this::mapToPartnerResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, partnersPage.getTotalElements());
    }

    /**
     * Xem chi tiết một partner
     */
    @Override
    public PartnerResponse getPartnerDetail(String partnerId) {
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy partner với id: " + partnerId));
        return mapToPartnerResponse(partner);
    }

    /**
     * Phê duyệt partner
     */
    @Override
    public void approvePartner(String partnerId) throws MessagingException {
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy partner với id: " + partnerId));

        if (!"PENDING".equalsIgnoreCase(partner.getApproveStatus())) {
            throw new IllegalStateException("Yêu cầu đã được xử lý trước đó!");
        }

        // Sinh mật khẩu ngẫu nhiên
        String rawPassword = generateRandomPassword();
        partner.setPassword(passwordEncoder.encode(rawPassword));
        partner.setEnabled(true);
        partner.setApproveStatus("APPROVED");
        userRepository.save(partner);

        // Gửi email thông báo
        String fullName = partner.getUserInfo() != null ? partner.getUserInfo().getFullName() : "Đối tác";
        String email = partner.getUserInfo() != null ? partner.getUserInfo().getEmail() : "";

        String subject = "Tài khoản đối tác Campverse được phê duyệt";
        String content = String.format("""
                <h3>Xin chào %s,</h3>
                <p>Yêu cầu trở thành đối tác của bạn đã được <b>phê duyệt thành công!</b></p>
                <p>Thông tin đăng nhập của bạn:</p>
                <ul>
                    <li><b>Email:</b> %s</li>
                    <li><b>Mật khẩu:</b> %s</li>
                </ul>
                <p>Bạn có thể đăng nhập tại: <a href="%s">%s</a></p>
                <p>Trân trọng,<br/>Đội ngũ Campverse</p>
                """, fullName, email, rawPassword, PARTNER_PORTAL_URL, PARTNER_PORTAL_URL);

        emailService.sendHtmlEmail(email, subject, content);
    }

    /**
     * Từ chối partner
     */
    @Override
    public void rejectPartner(String partnerId) throws MessagingException {
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy partner với id: " + partnerId));

        if (!"PENDING".equalsIgnoreCase(partner.getApproveStatus())) {
            throw new IllegalStateException("Yêu cầu đã được xử lý trước đó!");
        }

        partner.setApproveStatus("REJECTED");
        userRepository.save(partner);

        String fullName = partner.getUserInfo() != null ? partner.getUserInfo().getFullName() : "Đối tác";
        String email = partner.getUserInfo() != null ? partner.getUserInfo().getEmail() : "";

        String subject = "Yêu cầu trở thành đối tác Campverse bị từ chối";
        String content = String.format("""
                <h3>Xin chào %s,</h3>
                <p>Rất tiếc, yêu cầu trở thành đối tác của bạn đã bị <b>từ chối</b>.</p>
                <p>Nếu cần thêm thông tin, vui lòng liên hệ đội ngũ hỗ trợ Campverse.</p>
                <p>Trân trọng,<br/>Đội ngũ Campverse</p>
                """, fullName);

        emailService.sendHtmlEmail(email, subject, content);
    }

    /**
     * Map từ entity User (partner) sang PartnerResponse (DTO)
     */
    private PartnerResponse mapToPartnerResponse(User partner) {
        PartnerResponse dto = new PartnerResponse();
        dto.setId(partner.getId());
        dto.setApproveStatus(partner.getApproveStatus());

        if (partner.getUserInfo() != null) {
            dto.setFullName(partner.getUserInfo().getFullName());
            dto.setEmail(partner.getUserInfo().getEmail());
            dto.setPhoneNumber(partner.getUserInfo().getPhoneNumber());
            dto.setAvatarUrl(partner.getUserInfo().getAvatarUrl());
        }

        List<CampingInfor> campingInfors = campingInforRepository.findByOwner_Id(partner.getId());
        if (!campingInfors.isEmpty()) {
            dto.setCampingSites(
                    campingInfors.stream().map(site -> {
                        CampingSiteResponse siteDto = new CampingSiteResponse();
                        siteDto.setId(site.getId());
                        siteDto.setLocation(site.getName());

                        List<String> images = campingGalleryRepository
                                .findByCamping_CampingSite_Id(site.getId())
                                .stream()
                                .map(CampingGallery::getImageUrl)
                                .collect(Collectors.toList());

                        siteDto.setImages(images);
                        return siteDto;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }

    /**
     * Sinh mật khẩu ngẫu nhiên
     */
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
