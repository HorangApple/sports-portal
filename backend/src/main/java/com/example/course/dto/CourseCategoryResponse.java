package com.example.course.dto;

import com.example.course.entity.CourseCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 교육 과정 카테고리 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor
public class CourseCategoryResponse {

    /**
     * 카테고리 ID
     */
    private Long id;

    /**
     * 카테고리 코드
     */
    private String code;

    /**
     * 카테고리명
     */
    private String name;

    /**
     * 카테고리 설명
     */
    private String description;

    /**
     * 상위 카테고리 ID
     */
    private Long parentId;

    /**
     * 상위 카테고리명
     */
    private String parentName;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;

    /**
     * 활성화 여부
     */
    private boolean active;

    /**
     * 하위 카테고리 목록
     */
    private List<CourseCategoryResponse> children;

    /**
     * 생성 일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;

    @Builder
    private CourseCategoryResponse(Long id, String code, String name, String description,
                                 Long parentId, String parentName, Integer sortOrder, boolean active,
                                 List<CourseCategoryResponse> children, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.parentName = parentName;
        this.sortOrder = sortOrder;
        this.active = active;
        this.children = children;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * CourseCategory 엔티티를 CourseCategoryResponse DTO로 변환
     */
    public static CourseCategoryResponse from(CourseCategory category) {
        return CourseCategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .sortOrder(category.getSortOrder())
                .active(category.isActive())
                .children(category.getChildren() != null && !category.getChildren().isEmpty() ?
                        category.getChildren().stream()
                                .map(CourseCategoryResponse::from)
                                .collect(Collectors.toList()) : null)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
} 