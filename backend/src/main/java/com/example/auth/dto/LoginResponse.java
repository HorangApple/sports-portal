package com.example.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 응답을 위한 DTO
 * JWT 토큰과 사용자 정보를 포함
 */
@Getter
@Builder
public class LoginResponse {
    /**
     * JWT 액세스 토큰
     */
    private String accessToken;

    /**
     * JWT 리프레시 토큰
     */
    private String refreshToken;

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자 역할
     */
    private String role;
} 