package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 갱신 요청을 위한 DTO
 */
@Getter
@Setter
public class TokenRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
} 