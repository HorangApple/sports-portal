package com.example.user.dto;

import com.example.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 정보 응답을 위한 DTO
 * 사용자의 기본 정보와 생성/수정 일시를 포함합니다.
 */
@Getter
@NoArgsConstructor
@Builder
public class UserResponse {
    /**
     * 사용자 고유 식별자
     */
    private Long id;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자 권한 (ROLE_USER, ROLE_ADMIN 등)
     */
    private String role;

    /**
     * 계정 생성 일시
     */
    private LocalDateTime createdDate;

    /**
     * 계정 정보 수정 일시
     */
    private LocalDateTime modifiedDate;

    @Builder
    private UserResponse(Long id, String email, String name, String role, 
                        LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    /**
     * User 엔티티를 UserResponse DTO로 변환
     * @param user 변환할 User 엔티티
     * @return 변환된 UserResponse 객체
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdDate(user.getCreatedAt())
                .modifiedDate(user.getUpdatedAt())
                .build();
    }
} 