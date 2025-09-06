package com.example.survey.entity;

import com.example.common.entity.BaseTimeEntity;
import com.example.course.entity.Course;
import com.example.course.entity.CourseSession;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문지
 * 교육과정에 대한 설문 정보를 관리합니다.
 */
@Entity
@Table(name = "survey")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseTimeEntity {

    /**
     * 설문지 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 교육과정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * 설문지 제목
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 설문지 설명
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 설문지 시작일시
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * 설문지 종료일시
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    /**
     * 설문지 타입 (사전, 만족도, 사후)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private SurveyType type;

    /**
     * 활성 상태
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * 문항 목록
     */
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @Builder
    public Survey(Course course, String title, String description, LocalDateTime startDate, 
                 LocalDateTime endDate, SurveyType type, boolean active) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.active = active;
    }

    /**
     * 설문지 정보 업데이트
     * @param title 설문지 제목
     * @param description 설문지 설명
     * @param startDate 설문지 시작일시
     * @param endDate 설문지 종료일시
     * @param type 설문지 타입
     * @param active 활성 상태
     * @return 업데이트된 설문지
     */
    public Survey update(String title, String description, LocalDateTime startDate, 
                        LocalDateTime endDate, SurveyType type, boolean active) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.active = active;
        return this;
    }

    /**
     * 활성 상태 업데이트
     * @param active 활성 상태
     * @return 업데이트된 설문지
     */
    public Survey updateActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 문항 추가
     * @param question 문항
     * @return 업데이트된 설문지
     */
    public Survey addQuestion(SurveyQuestion question) {
        this.questions.add(question);
        return this;
    }

    /**
     * 문항 제거
     * @param question 문항
     * @return 업데이트된 설문지
     */
    public Survey removeQuestion(SurveyQuestion question) {
        this.questions.remove(question);
        return this;
    }

    /**
     * 설문 기간 확인
     * @param dateTime 확인할 일시
     * @return 설문 기간 내 여부
     */
    public boolean isWithinPeriod(LocalDateTime dateTime) {
        return !dateTime.isBefore(startDate) && !dateTime.isAfter(endDate);
    }

    /**
     * 설문 기간 종료 여부 확인
     * @param dateTime 확인할 일시
     * @return 설문 기간 종료 여부
     */
    public boolean isExpired(LocalDateTime dateTime) {
        return dateTime.isAfter(endDate);
    }
} 