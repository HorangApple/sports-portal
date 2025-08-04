package com.example.user.entity;

/**
 * 사용자 역할을 정의하는 enum
 * 시스템 내에서 사용자의 권한과 접근 수준을 결정
 */
public enum UserRole {
    /**
     * 일반 사용자 역할
     * 기본적인 교육 플랫폼 기능 사용 가능
     */
    ROLE_USER,

    /**
     * 관리자 역할
     * 시스템 관리 및 모든 기능에 대한 접근 권한 보유
     */
    ROLE_ADMIN,

    /**
     * 강사 역할
     * 교육 과정 관리 및 강의 진행 권한 보유
     */
    ROLE_INSTRUCTOR;

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    UserRole() {
        this.description = null;
    }
} 