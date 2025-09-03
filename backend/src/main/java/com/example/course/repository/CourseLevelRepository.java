package com.example.course.repository;

import com.example.course.entity.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 교육 과정 레벨 리포지토리
 */
@Repository
public interface CourseLevelRepository extends JpaRepository<CourseLevel, Long> {
    
    /**
     * 활성화된 모든 레벨을 정렬 순서대로 조회
     */
    List<CourseLevel> findByActiveTrueOrderBySortOrderAsc();
    
    /**
     * 이름으로 레벨 조회
     */
    boolean existsByName(String name);
} 