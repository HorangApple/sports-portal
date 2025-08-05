package com.example.survey.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설문 응답 항목
 * 문항별 응답 내용을 관리합니다.
 */
@Entity
@Table(name = "survey_response_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResponseItem extends BaseTimeEntity {

    /**
     * 응답 항목 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 설문 응답
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private SurveyResponse response;

    /**
     * 설문 문항
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    /**
     * 선택한 옵션 (객관식)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private SurveyQuestionOption selectedOption;

    /**
     * 응답 내용 (주관식)
     */
    @Column(name = "text_answer", length = 2000)
    private String textAnswer;

    /**
     * 척도 응답 값 (1~5, 1~7, 1~10 등)
     */
    @Column(name = "scale_value")
    private Integer scaleValue;

    @Builder
    public SurveyResponseItem(SurveyResponse response, SurveyQuestion question, 
                             SurveyQuestionOption selectedOption, String textAnswer, Integer scaleValue) {
        this.response = response;
        this.question = question;
        this.selectedOption = selectedOption;
        this.textAnswer = textAnswer;
        this.scaleValue = scaleValue;
    }

    /**
     * 응답 내용 업데이트 (객관식)
     * @param selectedOption 선택한 옵션
     * @return 업데이트된 응답 항목
     */
    public SurveyResponseItem updateSelectedOption(SurveyQuestionOption selectedOption) {
        this.selectedOption = selectedOption;
        return this;
    }

    /**
     * 응답 내용 업데이트 (주관식)
     * @param textAnswer 응답 내용
     * @return 업데이트된 응답 항목
     */
    public SurveyResponseItem updateTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
        return this;
    }

    /**
     * 응답 내용 업데이트 (척도형)
     * @param scaleValue 척도 값
     * @return 업데이트된 응답 항목
     */
    public SurveyResponseItem updateScaleValue(Integer scaleValue) {
        this.scaleValue = scaleValue;
        return this;
    }
} 