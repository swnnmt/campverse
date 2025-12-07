package com.demoProject.demo.service;

import com.demoProject.demo.model.dto.request.LoginRequest;
import com.demoProject.demo.model.dto.response.AuthResponse;
import com.demoProject.demo.model.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {


    AuthResponse doLogin(LoginRequest request, HttpServletResponse response);

    void doLogout(HttpServletRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    UserResponse getProfile(HttpServletRequest request);
}
