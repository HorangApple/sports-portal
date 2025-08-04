package com.example.user.dto.response;

import com.example.user.entity.User;
import com.example.user.entity.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String name,
    UserRole role,
    boolean enabled,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole(),
            user.isEnabled(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
} 