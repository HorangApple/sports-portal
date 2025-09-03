package com.example.course.repository;

import com.example.course.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 교육과정 카테고리 Repository
 */
@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    
    /**
     * 코드로 교육과정 카테고리 조회
     * 
     * @param code 교육과정 카테고리 코드
     * @return 교육과정 카테고리 Optional 객체
     */
    Optional<CourseCategory> findByCode(String code);
    
    /**
     * 활성화된 교육과정 카테고리 목록 조회
     * 
     * @return 활성화된 교육과정 카테고리 목록
     */
    List<CourseCategory> findByActiveTrue();
    
    /**
     * 상위 카테고리가 없는(최상위) 카테고리 목록 조회
     * 
     * @return 최상위 카테고리 목록
     */
    List<CourseCategory> findByParentIsNull();
    
    /**
     * 상위 카테고리별 하위 카테고리 목록 조회
     * 
     * @param parentId 상위 카테고리 ID
     * @return 하위 카테고리 목록
     */
    List<CourseCategory> findByParentId(Long parentId);
    
    /**
     * 특정 상위 카테고리에 속한 활성화된 하위 카테고리 목록 조회
     */
    List<CourseCategory> findByParentIdAndActiveTrue(Long parentId);
    
    /**
     * 최상위 카테고리 조회 (상위 카테고리가 없는 카테고리)
     */
    List<CourseCategory> findByParentIsNullAndActiveTrue();
    
    /**
     * 카테고리 코드로 활성화된 카테고리 조회
     */
    CourseCategory findByCodeAndActiveTrue(String code);
} 