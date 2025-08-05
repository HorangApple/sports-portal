package com.example.course.controller;

import com.example.course.dto.CourseEnrollmentRequest;
import com.example.course.dto.CourseEnrollmentResponse;
import com.example.course.service.CourseEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 과정 수강 신청
     * @param authentication 인증 정보
     * @param request 수강신청 요청 정보
     * @return 생성된 수강신청 정보
     */
    @PostMapping("/enroll")
    public ResponseEntity<CourseEnrollmentResponse> enrollCourse(
            Authentication authentication,
            @Valid @RequestBody CourseEnrollmentRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());

        CourseEnrollmentResponse enrollment = enrollmentService.enrollCourse(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * 수강 신청 취소
     * @param authentication 인증 정보
     * @param enrollmentId 수강신청 ID
     * @param reason 취소 사유
     * @return 취소 결과
     */
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Map<String, Object>> cancelEnrollment(
            Authentication authentication,
            @PathVariable Long enrollmentId,
            @RequestParam(required = false) String reason) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());

        CourseEnrollmentResponse canceledEnrollment = enrollmentService.cancelEnrollment(userId, enrollmentId, reason);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "수강 신청이 취소되었습니다.",
            "enrollment", canceledEnrollment
        ));
    }

    /**
     * 수강 신청 승인 (관리자용)
     * @param enrollmentId 수강신청 ID
     * @param reason 승인 사유
     * @return 승인 결과
     */
    @PostMapping("/{enrollmentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseEnrollmentResponse> approveEnrollment(
            @PathVariable Long enrollmentId,
            @RequestParam(required = false) String reason) {
        CourseEnrollmentResponse approvedEnrollment = enrollmentService.approveEnrollment(enrollmentId, reason);
        return ResponseEntity.ok(approvedEnrollment);
    }

    /**
     * 수강 신청 거절 (관리자용)
     * @param enrollmentId 수강신청 ID
     * @param reason 거절 사유
     * @return 거절 결과
     */
    @PostMapping("/{enrollmentId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseEnrollmentResponse> rejectEnrollment(
            @PathVariable Long enrollmentId,
            @RequestParam(required = false) String reason) {
        CourseEnrollmentResponse rejectedEnrollment = enrollmentService.rejectEnrollment(enrollmentId, reason);
        return ResponseEntity.ok(rejectedEnrollment);
    }

    /**
     * 수강 완료 처리 (관리자용)
     * @param enrollmentId 수강신청 ID
     * @param attendanceRate 출석률
     * @param completionRate 이수율
     * @return 완료 처리 결과
     */
    @PostMapping("/{enrollmentId}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseEnrollmentResponse> completeCourse(
            @PathVariable Long enrollmentId,
            @RequestParam(defaultValue = "100.0") Double attendanceRate,
            @RequestParam(defaultValue = "100.0") Double completionRate) {
        CourseEnrollmentResponse completedEnrollment = enrollmentService.completeCourse(enrollmentId, attendanceRate, completionRate);
        return ResponseEntity.ok(completedEnrollment);
    }
} 