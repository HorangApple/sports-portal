package com.example.course.repository;

import com.example.course.entity.CourseEnrollment;
import com.example.course.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 교육과정 수강신청 Repository
 */
@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    
    /**
     * 사용자별 수강신청 목록 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자의 수강신청 목록
     */
    List<CourseEnrollment> findByUserId(Long userId);
    
    /**
     * 교육과정 차수별 수강신청 목록 조회
     * 
     * @param sessionId 교육과정 차수 ID
     * @return 해당 차수의 수강신청 목록
     */
    List<CourseEnrollment> findBySessionId(Long sessionId);
    
    /**
     * 상태별 수강신청 목록 조회
     * 
     * @param status 수강신청 상태
     * @return 해당 상태의 수강신청 목록
     */
    List<CourseEnrollment> findByStatus(EnrollmentStatus status);
    
    /**
     * 사용자와 교육과정 차수로 수강신청 조회
     * 
     * @param userId 사용자 ID
     * @param sessionId 교육과정 차수 ID
     * @return 해당 사용자의 해당 차수 수강신청 목록
     */
    List<CourseEnrollment> findByUserIdAndSessionId(Long userId, Long sessionId);
    
    /**
     * 사용자와 교육과정 차수 및 상태로 수강신청 조회
     * 
     * @param userId 사용자 ID
     * @param sessionId 교육과정 차수 ID
     * @param status 수강신청 상태
     * @return 해당 사용자의 해당 차수 및 상태의 수강신청 목록
     */
    List<CourseEnrollment> findByUserIdAndSessionIdAndStatus(Long userId, Long sessionId, EnrollmentStatus status);
    
    /**
     * 사용자와 상태로 수강신청 조회
     * 
     * @param userId 사용자 ID
     * @param status 수강신청 상태
     * @return 해당 사용자의 해당 상태의 수강신청 목록
     */
    List<CourseEnrollment> findByUserIdAndStatus(Long userId, EnrollmentStatus status);
    
    /**
     * 사용자와 상태(문자열)로 수강신청 조회
     * 
     * @param userId 사용자 ID
     * @param status 수강신청 상태 문자열
     * @return 해당 사용자의 해당 상태의 수강신청 목록
     */
    List<CourseEnrollment> findByUserIdAndStatus(Long userId, String status);
} 