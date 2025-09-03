package com.example.course.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 교육 과정 정보 수정을 위한 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUpdateRequest {

    /**
     * 과정명
     */
    @Size(min = 2, max = 200, message = "과정명은 2~200자 사이여야 합니다")
    private String name;

    /**
     * 과정 설명
     */
    @Size(max = 2000, message = "과정 설명은 최대 2000자까지 가능합니다")
    private String description;

    /**
     * 학습 목표
     */
    @Size(max = 1000, message = "학습 목표는 최대 1000자까지 가능합니다")
    private String learningObjectives;

    /**
     * 총 소요 시간(분)
     */
    @Positive(message = "총 소요 시간은 양수여야 합니다")
    private Integer totalMinutes;

    /**
     * 최대 수강 인원
     */
    @Positive(message = "최대 수강 인원은 양수여야 합니다")
    private Integer maxEnrollment;

    /**
     * 최소 수강 인원
     */
    @Positive(message = "최소 수강 인원은 양수여야 합니다")
    private Integer minEnrollment;

    /**
     * 활성화 여부
     */
    private Boolean active;

    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 과정 유형 ID
     */
    private Long typeId;
} 