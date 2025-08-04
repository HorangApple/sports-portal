package com.example.auth.service;

import com.example.auth.dto.LoginResponse;
import com.example.auth.dto.TokenRefreshResponse;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 인증을 처리하는 서비스
 * 로그인, 토큰 발급 등의 기능을 제공
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    /**
     * 사용자 로그인을 처리하고 JWT 토큰을 발급
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 로그인 응답 (JWT 토큰 및 사용자 정보)
     */
    public LoginResponse login(String email, String password) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자 정보 조회
        User user = userService.findByEmail(email);

        // JWT 토큰 생성
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        // 응답 생성
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급
     * @param refreshToken 갱신할 리프레시 토큰
     * @return 새로 발급된 토큰 정보
     * @throws RuntimeException 토큰이 유효하지 않거나 만료된 경우
     */
    public TokenRefreshResponse refreshToken(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenService.validateToken(refreshToken) || jwtTokenService.isTokenExpired(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 토큰에서 사용자 정보 추출
        String email = jwtTokenService.getEmailFromToken(refreshToken);
        User user = userService.findByEmail(email);

        // 새로운 토큰 발급
        String newAccessToken = jwtTokenService.generateAccessToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
} 