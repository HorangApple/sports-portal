package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 교육과정 엔티티
 * 교육과정의 기본 정보를 관리
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 과정 코드
     */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    /**
     * 과정명
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 과정 설명
     */
    @Column(length = 2000)
    private String description;

    /**
     * 과정 학습 목표
     */
    @Column(length = 1000)
    private String learningObjectives;

    /**
     * 총 교육 시간 (분 단위)
     */
    private Integer totalMinutes;

    /**
     * 활성화 여부
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * 최대 수강 인원
     */
    private Integer maxEnrollment;

    /**
     * 최소 수강 인원
     */
    private Integer minEnrollment;

    /**
     * 과정 유형
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private CourseType type;

    /**
     * 과정 카테고리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    /**
     * 교육 차수/기수 목록
     */
    @OneToMany(mappedBy = "course")
    @Builder.Default
    private List<CourseSession> sessions = new ArrayList<>();

    /**
     * 총 등록된 수강생 수
     */
    private int enrollmentCount = 0;

    /**
     * 수강생 등록 시 수강생 수 증가
     */
    public void incrementEnrollmentCount() {
        this.enrollmentCount++;
    }

    /**
     * 수강생 취소 시 수강생 수 감소
     */
    public void decrementEnrollmentCount() {
        if (this.enrollmentCount > 0) {
            this.enrollmentCount--;
        }
    }
} 