package com.example.course.entity;

/**
 * 수강신청 상태 열거형
 */
public enum EnrollmentStatus {
    /**
     * 대기 중 - 신청은 완료되었으나 아직 승인/거절 결정이 되지 않은 상태
     */
    PENDING,
    
    /**
     * 승인됨 - 신청이 승인되어 수강이 가능한 상태
     */
    APPROVED,
    
    /**
     * 거절됨 - 신청이 거절된 상태
     */
    REJECTED,
    
    /**
     * 취소됨 - 사용자가 직접 신청을 취소한 상태
     */
    CANCELLED
} 