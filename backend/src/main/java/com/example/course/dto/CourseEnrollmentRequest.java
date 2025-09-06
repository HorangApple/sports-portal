package com.example.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 수강신청 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollmentRequest {
    
    /**
     * 교육과정 차수 ID
     */
    @NotNull(message = "교육과정 차수 ID는 필수입니다.")
    private Long courseId;
    
    /**
     * 신청 사유
     */
    private String applyReason;
} 