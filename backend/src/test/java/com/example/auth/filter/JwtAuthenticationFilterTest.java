package com.example.auth.filter;

import com.example.auth.service.TokenService;
import com.example.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JwtAuthenticationFilter의 기능을 테스트하는 클래스
 * JWT 토큰 기반 인증 필터의 다양한 시나리오를 테스트
 */
class JwtAuthenticationFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 각 테스트 메서드 실행 전에 수행되는 설정 메서드
     * Mock 객체들을 초기화하고 JwtAuthenticationFilter 인스턴스를 생성
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    /**
     * 유효한 JWT 토큰으로 인증이 성공적으로 이루어지는지 테스트
     * - Authorization 헤더가 올바른 형식인 경우
     * - 토큰이 유효하고 만료되지 않은 경우
     * - 사용자 정보가 존재하는 경우
     */
    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // given
        String token = "valid.token.here";
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(true);
        when(tokenService.getEmailFromToken(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), 
            "유효한 토큰으로 인증이 성공하면 SecurityContext에 Authentication이 설정되어야 합니다");
        verify(tokenService).validateToken(token);
        verify(tokenService).getEmailFromToken(token);
        verify(userDetailsService).loadUserByUsername(email);
    }

    /**
     * Authorization 헤더가 없는 경우 필터 체인이 계속 진행되는지 테스트
     * - 인증이 설정되지 않아야 함
     */
    @Test
    void doFilterInternal_NoAuthorizationHeader_ContinuesFilterChain() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "Authorization 헤더가 없으면 인증이 설정되지 않아야 합니다");
        verify(tokenService, never()).validateToken(any());
        verify(tokenService, never()).getEmailFromToken(any());
    }

    /**
     * 잘못된 형식의 Authorization 헤더로 필터 체인이 계속 진행되는지 테스트
     * - Bearer 접두어가 없는 경우
     * - 인증이 설정되지 않아야 함
     */
    @Test
    void doFilterInternal_InvalidAuthorizationHeader_ContinuesFilterChain() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("Invalid " + "token");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "잘못된 형식의 Authorization 헤더로는 인증이 설정되지 않아야 합니다");
        verify(tokenService, never()).validateToken(any());
        verify(tokenService, never()).getEmailFromToken(any());
    }

    /**
     * 잘못된 형식의 JWT 토큰으로 인증이 실패하는지 테스트
     * - 토큰 검증에 실패하는 경우
     * - 인증이 설정되지 않아야 함
     */
    @Test
    void doFilterInternal_InvalidToken_ContinuesFilterChain() throws ServletException, IOException {
        // given
        String token = "invalid.token.here";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "잘못된 토큰으로는 인증이 설정되지 않아야 합니다");
        verify(tokenService).validateToken(token);
        verify(tokenService, never()).getEmailFromToken(any());
    }

    /**
     * 만료된 JWT 토큰으로 인증이 실패하는지 테스트
     * - 토큰은 유효하지만 만료된 경우
     * - 인증이 설정되지 않아야 함
     */
    @Test
    void doFilterInternal_ExpiredToken_ContinuesFilterChain() throws ServletException, IOException {
        // given
        String token = "expired.token.here";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "만료된 토큰으로는 인증이 설정되지 않아야 합니다");
        verify(tokenService).validateToken(token);
        verify(tokenService, never()).getEmailFromToken(any());
    }
} 