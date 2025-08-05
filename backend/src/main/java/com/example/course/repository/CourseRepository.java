package com.example.course.repository;

import com.example.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 교육과정 Repository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * 코드로 교육과정 조회
     * 
     * @param code 교육과정 코드
     * @return 교육과정 Optional 객체
     */
    Optional<Course> findByCode(String code);
    
    /**
     * 활성화된 교육과정 목록 조회
     * 
     * @return 활성화된 교육과정 목록
     */
    List<Course> findByActiveTrue();
    
    /**
     * 카테고리별 교육과정 목록 조회
     * 
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리에 속한 교육과정 목록
     */
    List<Course> findByCategoryId(Long categoryId);
    
    /**
     * 교육과정 유형별 교육과정 목록 조회
     * 
     * @param typeId 교육과정 유형 ID
     * @return 해당 유형의 교육과정 목록
     */
    List<Course> findByTypeId(Long typeId);
    
    /**
     * 수강생 수 기준 인기 과정 조회 (활성화된 과정만)
     */
    List<Course> findByActiveTrueOrderByEnrollmentCountDesc();
    
    /**
     * 최신 과정 조회 (생성일 기준, 활성화된 과정만)
     */
    List<Course> findByActiveTrueOrderByCreatedAtDesc();
    
    /**
     * 특정 카테고리 목록에 속한 활성화된 과정 목록 조회
     */
    List<Course> findByCategoryIdInAndActiveTrue(List<Long> categoryIds);
} 