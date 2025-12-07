package com.demoProject.demo.service.impl;

import com.demoProject.demo.common.exception.CustomException;
import com.demoProject.demo.common.payload.ResponseCode;
import com.demoProject.demo.common.utils.JwtUtils;
import com.demoProject.demo.mapper.UserMapper;
import com.demoProject.demo.model.dto.request.LoginRequest;
import com.demoProject.demo.model.dto.response.AuthResponse;
import com.demoProject.demo.model.dto.response.UserResponse;
import com.demoProject.demo.model.entity.User;
import com.demoProject.demo.repository.RefreshTokenRepository;
import com.demoProject.demo.repository.UserRepository;
import com.demoProject.demo.service.AuthService;
import com.demoProject.demo.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LogManager.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public AuthResponse doLogin(LoginRequest request, HttpServletResponse response) {
        try {
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

            if (!user.isEnabled()) {
                throw new CustomException(ResponseCode.USER_DISABLE);
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                user.setLoginCount(user.getLoginCount() + 1);
                if (user.getLoginCount() == 3) {
                    user.setLocked(true);
                    userRepository.save(user);
                    throw new CustomException(ResponseCode.ACCOUNT_LOCKED);
                }
                userRepository.save(user);
                throw new CustomException(ResponseCode.INVALID_PASSWORD);
            } else {
                user.setLoginCount(0);
                userRepository.save(user);
            }

            var authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("User {} logged in with roles {}", userDetails.getUsername(), userDetails.getAuthorities());

            var claims = new HashMap<String, Object>();
            claims.put("user", user.getUserInfo().getFullName());

            String accessToken = jwtUtils.generateToken(claims, (User) authentication.getPrincipal());
            String refreshToken = jwtUtils.generateRefreshToken(user);

            var refreshTokenCookies = new Cookie("refreshToken", refreshToken);
            refreshTokenCookies.setHttpOnly(true);
            refreshTokenCookies.setSecure(true);
            refreshTokenCookies.setPath("/");
            refreshTokenCookies.setMaxAge((int) (refreshTokenExpiration / 1000));
            response.addCookie(refreshTokenCookies);

            return new AuthResponse(accessToken, refreshToken);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void doLogout(HttpServletRequest request) {
        try {

            String accessToken;

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            } else {
                throw new CustomException(ResponseCode.TOKEN_INVALID);
            }

            long expirationMillis = jwtUtils.extractExpiration(accessToken).getTime() - System.currentTimeMillis();

            if (expirationMillis > 0) {
                String key = "BlackList:" + accessToken;
                redisService.setValue(key, accessToken, expirationMillis, TimeUnit.MILLISECONDS);
            }

            String refreshToken = null;
            Cookie[] cookies = request.getCookies();

            if (request.getCookies() != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken != null) {
                var rfToken = refreshTokenRepository.findByTokenAndIsRevoked(refreshToken);
                if (rfToken == null) {
                    throw new CustomException(ResponseCode.REFRESH_TOKEN_NOT_FOUND);
                }
                rfToken.setRevoked(true);
                redisService.deleteCacheToken(String.valueOf(rfToken.getId()));
                refreshTokenRepository.save(rfToken);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }


    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        try {
            var tokenEntity = Optional.ofNullable(redisService.getCacheRefreshToken(refreshToken))
                    .orElseGet(() -> refreshTokenRepository.findByTokenAndIsRevoked(refreshToken));

            if (tokenEntity == null) {
                throw new CustomException(ResponseCode.REFRESH_TOKEN_NOT_FOUND);
            }

            User user = tokenEntity.getUser();

            var claims = new HashMap<String, Object>();
            claims.put("user", user.getUserInfo().getFullName());
            String newAccessToken = jwtUtils.generateToken(claims, user);

            String newRefreshToken = jwtUtils.generateRefreshToken(user);
            tokenEntity.setRevoked(true);
            refreshTokenRepository.save(tokenEntity);
            redisService.deleteCacheToken(refreshToken);

            Cookie refreshTokenCookies = new Cookie("refreshToken", newRefreshToken);
            refreshTokenCookies.setHttpOnly(true);
            refreshTokenCookies.setSecure(true);
            refreshTokenCookies.setPath("/");
            refreshTokenCookies.setMaxAge((int) (refreshTokenExpiration / 1000));
            response.addCookie(refreshTokenCookies);

            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserResponse getProfile(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            String accessToken = authHeader.substring(7);

            String userEmail = jwtUtils.extractUsername(accessToken);

            var user = userRepository.findByEmail(userEmail);

            if (user.isEmpty()) {
                throw new CustomException(ResponseCode.USER_NOT_FOUND);
            }

            var userResponse = userMapper.toUserResponse(user.get().getUserInfo());
            userResponse.setId(user.get().getId());
            userResponse.setReset(user.get().isReset());

            return userResponse;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
