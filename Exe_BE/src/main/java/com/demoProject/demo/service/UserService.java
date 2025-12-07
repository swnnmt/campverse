package com.demoProject.demo.service;



import com.demoProject.demo.model.dto.request.*;
import com.demoProject.demo.model.dto.response.RegistrationResponse;
import com.demoProject.demo.model.dto.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    RegistrationResponse register(RegistrationRequest request);

    void changePassword(ChangePasswordRequest request, Authentication authentication);

    void handleForgotPassword(ForgotPasswordRequest request);

    void verifyForgotPassword(ForgotPasswordVerifyRequest request);

    void activateAccount(String validOtp);

    void resendOtp(ResendOtpRequest request);

    UserResponse updateProfile(UpdateProfileRequest request, Authentication authentication);

    List<UserResponse> getAllUser();
}
