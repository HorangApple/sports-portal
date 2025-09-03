package com.example.course.dto;

import com.example.course.entity.Course;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 교육 과정 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor
public class CourseResponse {
    /**
     * 과정 고유 ID
     */
    private Long id;

    /**
     * 과정 코드
     */
    private String code;

    /**
     * 과정명
     */
    private String name;

    /**
     * 과정 설명
     */
    private String description;

    /**
     * 학습 목표
     */
    private String learningObjectives;

    /**
     * 총 소요 시간(분)
     */
    private Integer totalMinutes;

    /**
     * 최대 수강 인원
     */
    private Integer maxEnrollment;

    /**
     * 최소 수강 인원
     */
    private Integer minEnrollment;

    /**
     * 활성화 여부
     */
    private boolean active;

    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 카테고리명
     */
    private String categoryName;

    /**
     * 과정 유형 ID
     */
    private Long typeId;

    /**
     * 과정 유형명
     */
    private String typeName;

    /**
     * 생성 일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;

    @Builder
    private CourseResponse(Long id, String code, String name, String description,
                          String learningObjectives, Integer totalMinutes,
                          Integer maxEnrollment, Integer minEnrollment, boolean active,
                          Long categoryId, String categoryName, Long typeId, String typeName,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.learningObjectives = learningObjectives;
        this.totalMinutes = totalMinutes;
        this.maxEnrollment = maxEnrollment;
        this.minEnrollment = minEnrollment;
        this.active = active;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Course 엔티티를 CourseResponse DTO로 변환
     */
    public static CourseResponse from(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .description(course.getDescription())
                .learningObjectives(course.getLearningObjectives())
                .totalMinutes(course.getTotalMinutes())
                .maxEnrollment(course.getMaxEnrollment())
                .minEnrollment(course.getMinEnrollment())
                .active(course.isActive())
                .categoryId(course.getCategory() != null ? course.getCategory().getId() : null)
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)
                .typeId(course.getType() != null ? course.getType().getId() : null)
                .typeName(course.getType() != null ? course.getType().getName() : null)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
} 