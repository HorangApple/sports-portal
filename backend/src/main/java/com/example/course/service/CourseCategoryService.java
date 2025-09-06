package com.example.course.service;

import com.example.common.exception.EntityNotFoundException;
import com.example.course.dto.CourseCategoryCreateRequest;
import com.example.course.dto.CourseCategoryResponse;
import com.example.course.dto.CourseCategoryUpdateRequest;
import com.example.course.entity.CourseCategory;
import com.example.course.repository.CourseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 교육 과정 카테고리 관련 비즈니스 로직 처리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseCategoryService {

    private final CourseCategoryRepository categoryRepository;

    /**
     * 모든 활성화된 카테고리 조회
     */
    public List<CourseCategoryResponse> findAllActiveCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(CourseCategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * ID로 특정 카테고리 조회
     */
    public CourseCategoryResponse findCategoryById(Long id) {
        CourseCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + id));
        return CourseCategoryResponse.from(category);
    }

    /**
     * 상위 카테고리별 하위 카테고리 목록 조회
     */
    public List<CourseCategoryResponse> findSubcategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentIdAndActiveTrue(parentId).stream()
                .map(CourseCategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최상위 카테고리 목록 조회 (부모 카테고리가 없는 카테고리)
     */
    public List<CourseCategoryResponse> findRootCategories() {
        return categoryRepository.findByParentIsNullAndActiveTrue().stream()
                .map(CourseCategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 카테고리 생성
     */
    @Transactional
    public CourseCategoryResponse createCategory(CourseCategoryCreateRequest request) {
        CourseCategory parentCategory = null;
        if (request.getParentId() != null) {
            parentCategory = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다. ID: " + request.getParentId()));
        }

        CourseCategory category = CourseCategory.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .parent(parentCategory)
                .sortOrder(request.getSortOrder())
                .active(request.isActive())
                .build();

        CourseCategory savedCategory = categoryRepository.save(category);
        return CourseCategoryResponse.from(savedCategory);
    }

    /**
     * 기존 카테고리 정보 수정
     */
    @Transactional
    public CourseCategoryResponse updateCategory(Long id, CourseCategoryUpdateRequest request) {
        CourseCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + id));

        if (request.getParentId() != null && !request.getParentId().equals(category.getParent() != null ? category.getParent().getId() : null)) {
            CourseCategory parentCategory = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다. ID: " + request.getParentId()));
            category.setParent(parentCategory);
        } else if (request.getParentId() == null && category.getParent() != null) {
            category.setParent(null);
        }

        if (request.getName() != null) category.setName(request.getName());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getSortOrder() != null) category.setSortOrder(request.getSortOrder());
        if (request.getActive() != null) category.setActive(request.getActive());

        CourseCategory updatedCategory = categoryRepository.save(category);
        return CourseCategoryResponse.from(updatedCategory);
    }

    /**
     * 카테고리 비활성화 (논리적 삭제)
     */
    @Transactional
    public void deactivateCategory(Long id) {
        CourseCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }
} 