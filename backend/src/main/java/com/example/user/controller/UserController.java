package com.example.user.controller;

import com.example.user.dto.UserRegisterRequest;
import com.example.user.dto.UserResponse;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 HTTP 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 새로운 사용자 등록
     * @param request 사용자 등록 요청 데이터
     * @return 등록된 사용자 정보
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        var user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        return ResponseEntity.ok(UserResponse.from(user));
    }
} 