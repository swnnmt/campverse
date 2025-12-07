package com.demoProject.demo.service;

import com.demoProject.demo.model.entity.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine emailTemplateEngine;

    /**
     * Gửi email xác thực tài khoản người dùng (dùng Thymeleaf template)
     */
    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activateCode,
            String subject
    ) throws MessagingException {
        String templateName;

        if (emailTemplateName == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplateName.getName();
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activateCode", activateCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("ntdat14092003@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = emailTemplateEngine.process(templateName, context);
        helper.setText(template, true);

        javaMailSender.send(mimeMessage);
    }

    /**
     * Gửi email HTML tuỳ chỉnh (dùng khi phê duyệt đối tác, cấp mật khẩu,...)
     *
     * @param to        địa chỉ email người nhận
     * @param subject   tiêu đề email
     * @param htmlBody  nội dung HTML của email
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("ntdat14092003@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true => nội dung là HTML

        javaMailSender.send(mimeMessage);
        log.info("✅ Email HTML đã gửi thành công tới {}", to);
    }
}
