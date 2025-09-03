package com.example.course.repository;

import com.example.course.entity.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 교육과정 유형 Repository
 */
@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType, Long> {
    
    /**
     * 코드로 교육과정 유형 조회
     * 
     * @param code 교육과정 유형 코드
     * @return 교육과정 유형 Optional 객체
     */
    Optional<CourseType> findByCode(String code);
    
    /**
     * 활성화된 교육과정 유형 목록 조회
     * 
     * @return 활성화된 교육과정 유형 목록
     */
    List<CourseType> findByActiveTrue();
} 