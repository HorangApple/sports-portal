package com.example.course.controller;

import com.example.course.dto.CourseEnrollmentResponse;
import com.example.course.service.CourseEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 수강신청 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/user/courses")
@RequiredArgsConstructor
public class CourseEnrollmentController {

    private final CourseEnrollmentService enrollmentService;

    /**
     * 현재 학습 중인 과정 목록 조회
     * @param authentication 인증 정보
     * @return 학습 중인 과정 목록
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<CourseEnrollmentResponse>> getInProgressCourses(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        List<CourseEnrollmentResponse> inProgressCourses = enrollmentService.getInProgressCourses(userId);
        return ResponseEntity.ok(inProgressCourses);
    }

    /**
     * 완료한 과정 목록 조회
     * @param authentication 인증 정보
     * @return 완료한 과정 목록
     */
    @GetMapping("/completed")
    public ResponseEntity<List<CourseEnrollmentResponse>> getCompletedCourses(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        List<CourseEnrollmentResponse> completedCourses = enrollmentService.getCompletedCourses(userId);
        return ResponseEntity.ok(completedCourses);
    }
} 