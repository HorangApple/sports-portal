package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 교육과정 차수/기수 엔티티
 * 동일 교육과정의 여러 차수/기수를 관리
 */
@Entity
@Table(name = "course_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSession extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 차수/기수 코드 (예: 2023-1)
     */
    @Column(nullable = false, length = 30)
    private String code;

    /**
     * 차수/기수명 (예: 2023년 1차 자바 개발자 양성과정)
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 교육 시작일
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * 교육 종료일
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * 모집 시작일시
     */
    private LocalDateTime recruitmentStartAt;

    /**
     * 모집 종료일시
     */
    private LocalDateTime recruitmentEndAt;

    /**
     * 수강 장소/위치
     */
    @Column(length = 200)
    private String location;

    /**
     * 현재 등록 인원
     */
    @Column(columnDefinition = "integer default 0")
    private Integer currentEnrollment;

    /**
     * 상태 (UPCOMING, RECRUITING, IN_PROGRESS, COMPLETED, CANCELLED)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseSessionStatus status;

    /**
     * 속한 교육과정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * 세션에 등록된 수강생 목록
     */
    @OneToMany(mappedBy = "session")
    @Builder.Default
    private List<CourseEnrollment> enrollments = new ArrayList<>();

    /**
     * 교육 일정 목록
     */
    @OneToMany(mappedBy = "courseSession", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CourseSchedule> schedules = new ArrayList<>();

    public void decrementCurrentEnrollment() {
        if (this.currentEnrollment != null && this.currentEnrollment > 0) {
            this.currentEnrollment--;
        }
    }

    public void incrementCurrentEnrollment() {
        if (this.currentEnrollment == null) {
            this.currentEnrollment = 1;
        } else {
            this.currentEnrollment++;
        }
    }
} 