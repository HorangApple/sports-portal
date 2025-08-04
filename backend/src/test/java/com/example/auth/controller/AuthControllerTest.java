package com.example.auth.controller;

import com.example.auth.config.JwtProperties;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.service.AuthService;
import com.example.auth.service.JwtTokenService;
import com.example.auth.service.TokenService;
import com.example.user.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController 테스트 클래스
 * 인증 관련 API 엔드포인트를 테스트
 */
@WebMvcTest(controllers = AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtProperties jwtProperties;

    @MockBean
    private TokenService tokenService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String testName = "Test User";
    
    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn("testSecretKeyForJwtTokenGenerationInTestEnvironment123456789");
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600L);
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(86400L);
    }

    /**
     * 로그인 API 성공 테스트
     * 정상적인 로그인 요청 시 200 OK와 함께 토큰을 반환해야 함
     */
    @Test
    @DisplayName("로그인 API 성공 테스트")
    void login_Success() throws Exception {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail(testEmail);
        request.setPassword(testPassword);

        LoginResponse response = LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .userId(1L)
                .email(testEmail)
                .name(testName)
                .role(UserRole.ROLE_USER.name())
                .build();

        when(authService.login(any(), any())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.name").value(testName))
                .andExpect(jsonPath("$.role").value(UserRole.ROLE_USER.name()));
    }

    /**
     * 로그인 API 유효성 검사 테스트
     * 잘못된 이메일 형식으로 요청 시 400 Bad Request를 반환해야 함
     */
    @Test
    @DisplayName("로그인 API 유효성 검사 테스트")
    void login_ValidationFailure() throws Exception {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid-email");
        request.setPassword(testPassword);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 