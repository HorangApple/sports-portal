package com.example.auth.controller;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.dto.TokenRefreshRequest;
import com.example.auth.dto.TokenRefreshResponse;
import com.example.auth.service.AuthService;
import com.example.user.dto.UserResponse;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 인증 관련 HTTP 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * 사용자 로그인을 처리하고 JWT 토큰을 발급
     * @param request 로그인 요청 데이터
     * @return 로그인 응답 (JWT 토큰 및 사용자 정보)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급
     * @param request 토큰 갱신 요청 데이터
     * @return 새로 발급된 토큰 정보
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 현재 인증된 사용자 정보를 조회
     * @return 현재 인증된 사용자 정보
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new RuntimeException("인증 정보가 없습니다.");
            }
            
            String email = authentication.getName();
            if (email == null || email.isEmpty()) {
                throw new RuntimeException("사용자 이메일을 가져올 수 없습니다.");
            }
            
            System.out.println("인증된 사용자 이메일: " + email);
            User user = userService.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
            }
            
            UserResponse response = UserResponse.from(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("getCurrentUser 에러: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 