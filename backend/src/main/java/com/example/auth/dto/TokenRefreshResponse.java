package com.example.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 토큰 갱신 응답을 위한 DTO
 */
@Getter
@Builder
public class TokenRefreshResponse {

    /**
     * 새로 발급된 액세스 토큰
     */
    private String accessToken;

    /**
     * 새로 발급된 리프레시 토큰
     */
    private String refreshToken;
} 