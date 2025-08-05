package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import com.example.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 교육과정 수강신청 엔티티
 * 사용자의 교육과정 수강 신청 정보를 관리
 */
@Entity
@Table(name = "course_enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 수강신청 상태 (PENDING, APPROVED, REJECTED, CANCELLED)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    /**
     * 신청 일시
     */
    @Column(nullable = false)
    private LocalDateTime appliedAt;

    /**
     * 승인/거절 일시
     */
    private LocalDateTime processedAt;

    /**
     * 취소 일시
     */
    private LocalDateTime cancelledAt;

    /**
     * 신청 사유
     */
    @Column(length = 500)
    private String applyReason;

    /**
     * 승인/거절 사유
     */
    @Column(length = 500)
    private String processReason;

    /**
     * 취소 사유
     */
    @Column(length = 500)
    private String cancelReason;

    /**
     * 수강생(신청자)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 교육과정 차수
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_session_id", nullable = false)
    private CourseSession session;

    /**
     * 수료 여부
     */
    private boolean completed;

    /**
     * 수료일
     */
    private LocalDateTime completedAt;

    /**
     * 출석률(%)
     */
    private Double attendanceRate;

    /**
     * 학습 완료율(%)
     */
    private Double completionRate;
} 