package com.example.survey.entity;

import com.example.common.entity.BaseTimeEntity;
import com.example.course.entity.CourseEnrollment;
import com.example.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문 응답
 * 사용자의 설문 응답 정보를 관리합니다.
 */
@Entity
@Table(name = "survey_response")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResponse extends BaseTimeEntity {

    /**
     * 응답 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 설문지
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    /**
     * 응답자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 관련 수강신청
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private CourseEnrollment enrollment;

    /**
     * 응답 완료 일시
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * 응답 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private SurveyResponseStatus status;

    /**
     * 응답 항목 목록
     */
    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyResponseItem> items = new ArrayList<>();

    @Builder
    public SurveyResponse(Survey survey, User user, CourseEnrollment enrollment, SurveyResponseStatus status) {
        this.survey = survey;
        this.user = user;
        this.enrollment = enrollment;
        this.status = status;
    }

    /**
     * 응답 완료 처리
     * @return 완료 처리된 응답
     */
    public SurveyResponse complete() {
        this.status = SurveyResponseStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        return this;
    }

    /**
     * 응답 취소 처리
     * @return 취소 처리된 응답
     */
    public SurveyResponse cancel() {
        this.status = SurveyResponseStatus.CANCELLED;
        return this;
    }

    /**
     * 응답 항목 추가
     * @param item 응답 항목
     * @return 업데이트된 응답
     */
    public SurveyResponse addItem(SurveyResponseItem item) {
        this.items.add(item);
        return this;
    }

    /**
     * 응답 항목 제거
     * @param item 응답 항목
     * @return 업데이트된 응답
     */
    public SurveyResponse removeItem(SurveyResponseItem item) {
        this.items.remove(item);
        return this;
    }

    /**
     * 응답 상태 확인
     * @return 응답 완료 여부
     */
    public boolean isCompleted() {
        return this.status == SurveyResponseStatus.COMPLETED;
    }

    /**
     * 응답 취소 확인
     * @return 응답 취소 여부
     */
    public boolean isCancelled() {
        return this.status == SurveyResponseStatus.CANCELLED;
    }
} 