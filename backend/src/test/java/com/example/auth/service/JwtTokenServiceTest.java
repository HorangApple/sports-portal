package com.example.auth.service;

import com.example.auth.config.JwtProperties;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JwtTokenService의 기능을 테스트하는 클래스
 * 토큰 생성, 검증, 만료 확인 등의 기능을 테스트
 */
class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;
    private JwtProperties jwtProperties;
    private User user;

    /**
     * 각 테스트 메서드 실행 전에 수행되는 설정 메서드
     * JwtProperties와 JwtTokenService를 초기화하고 테스트용 User 객체를 생성
     */
    @BeforeEach
    void setUp() {
        // JwtProperties 모의 객체 생성 및 설정
        jwtProperties = mock(JwtProperties.class);
        when(jwtProperties.getSecret()).thenReturn("testSecretKey123456789012345678901234567890");
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600000L); // 1시간
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(86400000L); // 24시간

        // JwtTokenService 인스턴스 생성
        jwtTokenService = new JwtTokenService(jwtProperties);
        
        // 테스트용 User 객체 생성
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .role(UserRole.ROLE_USER)
                .build();
    }

    /**
     * 유효한 사용자 정보로 액세스 토큰을 생성하는 기능 테스트
     * 생성된 토큰이 null이 아니고 JWT 형식(3개의 파트)을 갖는지 확인
     */
    @Test
    void generateAccessToken_ValidUser_ReturnsToken() {
        // when
        String token = jwtTokenService.generateAccessToken(user);

        // then
        assertNotNull(token, "생성된 토큰은 null이 아니어야 합니다");
        assertTrue(token.split("\\.").length == 3, "JWT 토큰은 3개의 파트(헤더, 페이로드, 서명)로 구성되어야 합니다");
    }

    /**
     * 유효한 사용자 정보로 리프레시 토큰을 생성하는 기능 테스트
     * 생성된 토큰이 null이 아니고 JWT 형식(3개의 파트)을 갖는지 확인
     */
    @Test
    void generateRefreshToken_ValidUser_ReturnsToken() {
        // when
        String token = jwtTokenService.generateRefreshToken(user);

        // then
        assertNotNull(token, "생성된 토큰은 null이 아니어야 합니다");
        assertTrue(token.split("\\.").length == 3, "JWT 토큰은 3개의 파트(헤더, 페이로드, 서명)로 구성되어야 합니다");
    }

    /**
     * 유효한 토큰을 검증하는 기능 테스트
     * 생성된 토큰이 유효한지 확인
     */
    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // given
        String token = jwtTokenService.generateAccessToken(user);

        // when
        boolean isValid = jwtTokenService.validateToken(token);

        // then
        assertTrue(isValid, "유효한 토큰은 검증에 통과해야 합니다");
    }

    /**
     * 잘못된 형식의 토큰을 검증하는 기능 테스트
     * 잘못된 토큰이 검증에 실패하는지 확인
     */
    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean isValid = jwtTokenService.validateToken(invalidToken);

        // then
        assertFalse(isValid, "잘못된 형식의 토큰은 검증에 실패해야 합니다");
    }

    /**
     * 토큰에서 이메일을 추출하는 기능 테스트
     * 토큰에 포함된 이메일이 올바르게 추출되는지 확인
     */
    @Test
    void getEmailFromToken_ValidToken_ReturnsEmail() {
        // given
        String token = jwtTokenService.generateAccessToken(user);

        // when
        String email = jwtTokenService.getEmailFromToken(token);

        // then
        assertEquals(user.getEmail(), email, "토큰에서 추출한 이메일이 원본 이메일과 일치해야 합니다");
    }

    /**
     * 만료되지 않은 토큰의 만료 여부를 확인하는 기능 테스트
     * 만료되지 않은 토큰이 만료되지 않았다고 판단하는지 확인
     */
    @Test
    void isTokenExpired_NotExpiredToken_ReturnsFalse() {
        // given
        String token = jwtTokenService.generateAccessToken(user);

        // when
        boolean isExpired = jwtTokenService.isTokenExpired(token);

        // then
        assertFalse(isExpired, "만료되지 않은 토큰은 만료되지 않았다고 판단해야 합니다");
    }

    /**
     * 만료된 토큰의 만료 여부를 확인하는 기능 테스트
     * 만료된 토큰이 만료되었다고 판단하는지 확인
     */
    @Test
    void isTokenExpired_ExpiredToken_ReturnsTrue() {
        // given
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(1L); // 1ms로 설정하여 즉시 만료되도록 함
        jwtTokenService = new JwtTokenService(jwtProperties);
        String token = jwtTokenService.generateAccessToken(user);

        // when
        try {
            TimeUnit.MILLISECONDS.sleep(2); // 토큰이 만료될 때까지 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        boolean isExpired = jwtTokenService.isTokenExpired(token);

        // then
        assertTrue(isExpired, "만료된 토큰은 만료되었다고 판단해야 합니다");
    }
} 