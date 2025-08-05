package com.example.course.repository;

import com.example.course.entity.CourseSession;
import com.example.course.entity.CourseSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 교육과정 차수 Repository
 */
@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    
    /**
     * 코드로 교육과정 차수 조회
     * 
     * @param code 교육과정 차수 코드
     * @return 교육과정 차수 Optional 객체
     */
    Optional<CourseSession> findByCode(String code);
    
    /**
     * 교육과정별 차수 목록 조회
     * 
     * @param courseId 교육과정 ID
     * @return 교육과정 차수 목록
     */
    List<CourseSession> findByCourseId(Long courseId);
    
    /**
     * 상태별 교육과정 차수 목록 조회
     * 
     * @param status 교육과정 차수 상태
     * @return 교육과정 차수 목록
     */
    List<CourseSession> findByStatus(CourseSessionStatus status);
    
    /**
     * 현재 모집 중인 교육과정 차수 목록 조회
     * 
     * @param currentDate 현재 날짜
     * @return 모집 중인 교육과정 차수 목록
     */
    List<CourseSession> findByStatusAndStartDateAfter(CourseSessionStatus status, LocalDate currentDate);
    
    /**
     * 현재 진행 중인 교육과정 차수 목록 조회
     * 
     * @param currentDate 현재 날짜
     * @return 진행 중인 교육과정 차수 목록
     */
    List<CourseSession> findByStatusAndStartDateBeforeAndEndDateAfter(
            CourseSessionStatus status, LocalDate startDate, LocalDate endDate);
} 