package com.example.auth.config;

import com.example.auth.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * SecurityConfig의 기능을 테스트하는 클래스
 * Spring Security 설정이 올바르게 구성되는지 테스트
 */
class SecurityConfigTest {

    /**
     * SecurityFilterChain이 올바르게 구성되는지 테스트
     * - CSRF 보호 비활성화
     * - 세션 관리 설정 (STATELESS)
     * - 요청 권한 설정
     * - JWT 인증 필터 추가
     */
    @Test
    void securityFilterChain_ConfiguresCorrectly() throws Exception {
        // given
        JwtAuthenticationFilter jwtAuthFilter = mock(JwtAuthenticationFilter.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(jwtAuthFilter, userDetailsService);
        
        // HttpSecurity와 관련된 mock 객체 설정
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_SELF);
        when(http.csrf(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        DefaultSecurityFilterChain filterChainMock = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(filterChainMock);

        // when
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(http);

        // then
        assertNotNull(filterChain, "SecurityFilterChain이 null이 아니어야 합니다");
        verify(http).csrf(any());
        verify(http).sessionManagement(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).addFilterBefore(eq(jwtAuthFilter), any());
    }

    /**
     * PasswordEncoder가 BCryptPasswordEncoder 타입인지 테스트
     * - 비밀번호 암호화에 BCrypt 알고리즘이 사용되는지 확인
     */
    @Test
    void passwordEncoder_ReturnsBCryptPasswordEncoder() {
        // given
        JwtAuthenticationFilter jwtAuthFilter = mock(JwtAuthenticationFilter.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(jwtAuthFilter, userDetailsService);

        // when
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // then
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder,
            "PasswordEncoder는 BCryptPasswordEncoder 타입이어야 합니다");
    }
} 