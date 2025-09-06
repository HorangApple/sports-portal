package com.example.course.dto;

import com.example.course.entity.CourseEnrollment;
import com.example.course.entity.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 수강신청 정보 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollmentResponse {
    
    private Long id;
    private Long userId;
    private String userName;
    private Long courseId;
    private String courseName;
    private Long sessionId;
    private String sessionName;
    private EnrollmentStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime processedAt;
    private LocalDateTime cancelledAt;
    private String applyReason;
    private String processReason;
    private String cancelReason;
    private boolean completed;
    private LocalDateTime completedAt;
    private Double attendanceRate;
    private Double completionRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 엔티티를 DTO로 변환
     * @param enrollment 수강신청 엔티티
     * @return 수강신청 응답 DTO
     */
    public static CourseEnrollmentResponse from(CourseEnrollment enrollment) {
        return CourseEnrollmentResponse.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .userName(enrollment.getUser().getName())
                .courseId(enrollment.getSession().getCourse().getId())
                .courseName(enrollment.getSession().getCourse().getName())
                .sessionId(enrollment.getSession().getId())
                .sessionName(enrollment.getSession().getName())
                .status(enrollment.getStatus())
                .appliedAt(enrollment.getAppliedAt())
                .processedAt(enrollment.getProcessedAt())
                .cancelledAt(enrollment.getCancelledAt())
                .applyReason(enrollment.getApplyReason())
                .processReason(enrollment.getProcessReason())
                .cancelReason(enrollment.getCancelReason())
                .completed(enrollment.isCompleted())
                .completedAt(enrollment.getCompletedAt())
                .attendanceRate(enrollment.getAttendanceRate())
                .completionRate(enrollment.getCompletionRate())
                .startDate(enrollment.getSession().getStartDate())
                .endDate(enrollment.getSession().getEndDate())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
} 