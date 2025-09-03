package com.example.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 새로운 교육 과정 카테고리 생성을 위한 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCategoryCreateRequest {

    /**
     * 카테고리 코드 (고유 식별자)
     */
    @NotBlank(message = "카테고리 코드는 필수입니다")
    @Size(min = 2, max = 30, message = "카테고리 코드는 2~30자 사이여야 합니다")
    private String code;

    /**
     * 카테고리명
     */
    @NotBlank(message = "카테고리명은 필수입니다")
    @Size(min = 2, max = 100, message = "카테고리명은 2~100자 사이여야 합니다")
    private String name;

    /**
     * 카테고리 설명
     */
    @Size(max = 500, message = "카테고리 설명은 최대 500자까지 가능합니다")
    private String description;

    /**
     * 상위 카테고리 ID (최상위 카테고리의 경우 null)
     */
    private Long parentId;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;

    /**
     * 활성화 여부
     */
    private boolean active;
} 