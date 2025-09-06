package com.example.course.controller;

import com.example.course.dto.CourseCategoryCreateRequest;
import com.example.course.dto.CourseCategoryResponse;
import com.example.course.dto.CourseCategoryUpdateRequest;
import com.example.course.service.CourseCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 교육 과정 카테고리 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final CourseCategoryService categoryService;

    /**
     * 모든 활성화된 카테고리 목록 조회
     * @return 활성화된 카테고리 목록
     */
    @GetMapping
    public ResponseEntity<List<CourseCategoryResponse>> getAllActiveCategories() {
        List<CourseCategoryResponse> categories = categoryService.findAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * ID로 특정 카테고리 조회
     * @param id 카테고리 ID
     * @return 조회된 카테고리 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseCategoryResponse> getCategoryById(@PathVariable Long id) {
        CourseCategoryResponse category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * 최상위 카테고리 목록 조회
     * @return 최상위 카테고리 목록
     */
    @GetMapping("/root")
    public ResponseEntity<List<CourseCategoryResponse>> getRootCategories() {
        List<CourseCategoryResponse> rootCategories = categoryService.findRootCategories();
        return ResponseEntity.ok(rootCategories);
    }

    /**
     * 상위 카테고리별 하위 카테고리 목록 조회
     * @param parentId 상위 카테고리 ID
     * @return 하위 카테고리 목록
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<CourseCategoryResponse>> getSubcategoriesByParentId(@PathVariable Long parentId) {
        List<CourseCategoryResponse> subCategories = categoryService.findSubcategoriesByParentId(parentId);
        return ResponseEntity.ok(subCategories);
    }

    /**
     * 새로운 카테고리 생성 (관리자 권한 필요)
     * @param request 카테고리 생성 요청 정보
     * @return 생성된 카테고리 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseCategoryResponse> createCategory(@Valid @RequestBody CourseCategoryCreateRequest request) {
        CourseCategoryResponse createdCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * 기존 카테고리 정보 수정 (관리자 권한 필요)
     * @param id 수정할 카테고리 ID
     * @param request 수정 요청 정보
     * @return 수정된 카테고리 정보
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CourseCategoryUpdateRequest request) {
        CourseCategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * 카테고리 비활성화 (논리적 삭제) (관리자 권한 필요)
     * @param id 비활성화할 카테고리 ID
     * @return 응답 상태 코드
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }
} 