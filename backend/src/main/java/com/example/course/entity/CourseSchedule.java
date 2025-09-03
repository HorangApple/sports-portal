package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 교육과정 일정 엔티티
 * 교육과정 차수의 일별 수업 일정을 관리
 */
@Entity
@Table(name = "course_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CourseSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 교육과정 차수
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_session_id", nullable = false)
    private CourseSession courseSession;

    /**
     * 일정 날짜
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * 시작 시간
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * 종료 시간
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * 강의실/장소
     */
    @Column(length = 100)
    private String location;

    /**
     * 강사 이름
     */
    @Column(length = 50)
    private String instructorName;

    /**
     * 일정 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    public enum ScheduleStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }
} 