package com.example.course.entity;

/**
 * 교육과정 차수/기수 상태 열거형
 */
public enum CourseSessionStatus {
    /**
     * 예정됨 - 아직 모집 시작 전
     */
    UPCOMING,
    
    /**
     * 모집 중 - 수강생 모집 진행 중
     */
    RECRUITING,
    
    /**
     * 진행 중 - 교육이 진행 중
     */
    IN_PROGRESS,
    
    /**
     * 완료됨 - 교육이 종료됨
     */
    COMPLETED,
    
    /**
     * 취소됨 - 교육이 취소됨
     */
    CANCELLED
} 