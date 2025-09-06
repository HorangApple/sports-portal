package com.example.survey.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설문 문항 선택지
 * 객관식 문항의 선택지 정보를 관리합니다.
 */
@Entity
@Table(name = "survey_question_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestionOption extends BaseTimeEntity {

    /**
     * 선택지 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 설문 문항
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    /**
     * 선택지 내용
     */
    @Column(name = "content", nullable = false, length = 500)
    private String content;

    /**
     * 표시 순서
     */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /**
     * 활성 상태
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    @Builder
    public SurveyQuestionOption(SurveyQuestion question, String content, Integer displayOrder, boolean active) {
        this.question = question;
        this.content = content;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    /**
     * 선택지 정보 업데이트
     * @param content 선택지 내용
     * @param displayOrder 표시 순서
     * @param active 활성 상태
     * @return 업데이트된 선택지
     */
    public SurveyQuestionOption update(String content, Integer displayOrder, boolean active) {
        this.content = content;
        this.displayOrder = displayOrder;
        this.active = active;
        return this;
    }

    /**
     * 표시 순서 업데이트
     * @param displayOrder 표시 순서
     * @return 업데이트된 선택지
     */
    public SurveyQuestionOption updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    /**
     * 활성 상태 업데이트
     * @param active 활성 상태
     * @return 업데이트된 선택지
     */
    public SurveyQuestionOption updateActive(boolean active) {
        this.active = active;
        return this;
    }
} 