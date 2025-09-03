package com.example.course.dto;

import com.example.course.entity.CourseLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * 교육 과정 레벨 응답 DTO
 */
@Getter
@Builder
public class CourseLevelResponse {

    private Long id;
    private String name;
    private Integer sortOrder;
    private String description;
    
    /**
     * CourseLevel 엔티티를 CourseLevelResponse DTO로 변환
     *
     * @param level CourseLevel 엔티티
     * @return CourseLevelResponse DTO
     */
    public static CourseLevelResponse from(CourseLevel level) {
        return CourseLevelResponse.builder()
                .id(level.getId())
                .name(level.getName())
                .sortOrder(level.getSortOrder())
                .description(level.getDescription())
                .build();
    }
} 