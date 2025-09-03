package com.example.course.service;

import com.example.course.dto.CourseResponse;
import com.example.course.dto.CourseCreateRequest;
import com.example.course.dto.CourseUpdateRequest;
import com.example.course.entity.Course;
import com.example.course.entity.CourseCategory;
import com.example.course.entity.CourseType;
import com.example.course.repository.CourseCategoryRepository;
import com.example.course.repository.CourseRepository;
import com.example.course.repository.CourseTypeRepository;
import com.example.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository categoryRepository;
    private final CourseTypeRepository typeRepository;

    /**
     * 모든 활성화된 과정 목록 조회
     */
    public List<CourseResponse> findAllActiveCourses() {
        return courseRepository.findByActiveTrue().stream()
                .map(CourseResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 과정 ID로 과정 조회
     */
    public CourseResponse findCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + id));
        return CourseResponse.from(course);
    }

    /**
     * 카테고리별 과정 목록 조회
     */
    public List<CourseResponse> findCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(CourseResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 과정 생성
     */
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        CourseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));
        
        CourseType type = typeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("과정 유형을 찾을 수 없습니다. ID: " + request.getTypeId()));
        
        Course course = Course.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .learningObjectives(request.getLearningObjectives())
                .totalMinutes(request.getTotalMinutes())
                .maxEnrollment(request.getMaxEnrollment())
                .minEnrollment(request.getMinEnrollment())
                .active(request.isActive())
                .category(category)
                .type(type)
                .build();
        
        Course savedCourse = courseRepository.save(course);
        return CourseResponse.from(savedCourse);
    }

    /**
     * 기존 과정 정보 수정
     */
    @Transactional
    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + id));
        
        if (request.getCategoryId() != null) {
            CourseCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));
            course.setCategory(category);
        }
        
        if (request.getTypeId() != null) {
            CourseType type = typeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("과정 유형을 찾을 수 없습니다. ID: " + request.getTypeId()));
            course.setType(type);
        }
        
        if (request.getName() != null) course.setName(request.getName());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getLearningObjectives() != null) course.setLearningObjectives(request.getLearningObjectives());
        if (request.getTotalMinutes() != null) course.setTotalMinutes(request.getTotalMinutes());
        if (request.getMaxEnrollment() != null) course.setMaxEnrollment(request.getMaxEnrollment());
        if (request.getMinEnrollment() != null) course.setMinEnrollment(request.getMinEnrollment());
        if (request.getActive() != null) course.setActive(request.getActive());
        
        Course updatedCourse = courseRepository.save(course);
        return CourseResponse.from(updatedCourse);
    }

    /**
     * 과정 비활성화 (논리적 삭제)
     */
    @Transactional
    public void deactivateCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + id));
        course.setActive(false);
        courseRepository.save(course);
    }
} 