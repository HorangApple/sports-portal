package com.example.course.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 교육 과정 카테고리 정보 수정을 위한 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCategoryUpdateRequest {

    /**
     * 카테고리명
     */
    @Size(min = 2, max = 100, message = "카테고리명은 2~100자 사이여야 합니다")
    private String name;

    /**
     * 카테고리 설명
     */
    @Size(max = 500, message = "카테고리 설명은 최대 500자까지 가능합니다")
    private String description;

    /**
     * 상위 카테고리 ID (최상위 카테고리로 변경 시 null)
     */
    private Long parentId;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;

    /**
     * 활성화 여부
     */
    private Boolean active;
} 