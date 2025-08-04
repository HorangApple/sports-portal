package com.example.auth.service;

import com.example.auth.dto.LoginResponse;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AuthService 테스트 클래스
 * 사용자 인증 관련 기능을 테스트
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private String testEmail = "test@example.com";
    private String testPassword = "password123";
    private String testName = "Test User";

    /**
     * 테스트 전 초기 설정
     */
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email(testEmail)
                .password(testPassword)
                .name(testName)
                .role(UserRole.ROLE_USER)
                .build();
    }

    /**
     * 로그인 성공 테스트
     * 정상적인 이메일과 비밀번호로 로그인 시도 시 성공해야 함
     */
    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findByEmail(testEmail)).thenReturn(testUser);
        when(jwtTokenService.generateAccessToken(testUser)).thenReturn("accessToken");
        when(jwtTokenService.generateRefreshToken(testUser)).thenReturn("refreshToken");

        // when
        LoginResponse response = authService.login(testEmail, testPassword);

        // then
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(testEmail, response.getEmail());
        assertEquals(testName, response.getName());
        assertEquals(UserRole.ROLE_USER.name(), response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByEmail(testEmail);
        verify(jwtTokenService).generateAccessToken(testUser);
        verify(jwtTokenService).generateRefreshToken(testUser);
    }

    /**
     * 인증 실패 테스트
     * 잘못된 비밀번호로 로그인 시도 시 예외가 발생해야 함
     */
    @Test
    @DisplayName("인증 실패 테스트")
    void login_AuthenticationFailure() {
        // given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("인증 실패"));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            authService.login(testEmail, "wrongPassword");
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userService, jwtTokenService);
    }
} 