package com.example.course.controller;

import com.example.course.dto.CourseCreateRequest;
import com.example.course.dto.CourseResponse;
import com.example.course.dto.CourseUpdateRequest;
import com.example.course.dto.CourseCategoryResponse;
import com.example.course.dto.CourseLevelResponse;
import com.example.course.service.CourseService;
import com.example.course.service.CourseCategoryService;
import com.example.course.service.CourseLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 교육 과정 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseCategoryService categoryService;
    private final CourseLevelService levelService;

    /**
     * 모든 활성화된 교육 과정 목록 조회
     * @return 활성화된 교육 과정 목록
     */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllActiveCourses() {
        List<CourseResponse> courses = courseService.findAllActiveCourses();
        return ResponseEntity.ok(courses);
    }

    /**
     * ID로 특정 교육 과정 조회
     * @param id 교육 과정 ID
     * @return 조회된 교육 과정 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.findCourseById(id);
        return ResponseEntity.ok(course);
    }

    /**
     * 카테고리별 교육 과정 목록 조회
     * @param categoryId 카테고리 ID
     * @return 카테고리에 속한 교육 과정 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCategory(@PathVariable Long categoryId) {
        List<CourseResponse> courses = courseService.findCoursesByCategory(categoryId);
        return ResponseEntity.ok(courses);
    }

    /**
     * 새로운 교육 과정 생성 (관리자 권한 필요)
     * @param request 교육 과정 생성 요청 정보
     * @return 생성된 교육 과정 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    /**
     * 기존 교육 과정 정보 수정 (관리자 권한 필요)
     * @param id 수정할 교육 과정 ID
     * @param request 수정 요청 정보
     * @return 수정된 교육 과정 정보
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id, 
            @Valid @RequestBody CourseUpdateRequest request) {
        CourseResponse updatedCourse = courseService.updateCourse(id, request);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * 교육 과정 비활성화 (논리적 삭제) (관리자 권한 필요)
     * @param id 비활성화할 교육 과정 ID
     * @return 응답 상태 코드
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateCourse(@PathVariable Long id) {
        courseService.deactivateCourse(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 모든 활성화된 교육 과정 카테고리 목록 조회
     * @return 활성화된 교육 과정 카테고리 목록
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CourseCategoryResponse>> getAllCategories() {
        List<CourseCategoryResponse> categories = categoryService.findAllActiveCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * 모든 활성화된 교육 과정 레벨 목록 조회
     * @return 활성화된 교육 과정 레벨 목록
     */
    @GetMapping("/levels")
    public ResponseEntity<List<CourseLevelResponse>> getAllLevels() {
        List<CourseLevelResponse> levels = levelService.findAllActiveLevels();
        return ResponseEntity.ok(levels);
    }
} 