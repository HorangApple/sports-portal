package com.example.survey.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 설문 문항
 * 설문지의 문항 정보를 관리합니다.
 */
@Entity
@Table(name = "survey_question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion extends BaseTimeEntity {

    /**
     * 문항 ID
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
     * 문항 내용
     */
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    /**
     * 문항 설명
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * 문항 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private SurveyQuestionType type;

    /**
     * 필수 응답 여부
     */
    @Column(name = "required", nullable = false)
    private boolean required;

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

    /**
     * 선택지 목록 (객관식 문항인 경우)
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionOption> options = new ArrayList<>();

    /**
     * 응답 항목 목록
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyResponseItem> responseItems = new ArrayList<>();

    /**
     * 생성자
     *
     * @param survey       설문지
     * @param content      문항 내용
     * @param description  문항 설명
     * @param type         문항 유형
     * @param required     필수 응답 여부
     * @param displayOrder 표시 순서
     * @param active       활성 상태
     */
    @Builder
    public SurveyQuestion(Survey survey, String content, String description, SurveyQuestionType type,
                         boolean required, Integer displayOrder, boolean active) {
        this.survey = survey;
        this.content = content;
        this.description = description;
        this.type = type;
        this.required = required;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    /**
     * 문항 정보 업데이트
     * @param content 문항 내용
     * @param description 문항 설명
     * @param type 문항 유형
     * @param required 필수 응답 여부
     * @param displayOrder 표시 순서
     * @param active 활성 상태
     * @return 업데이트된 문항
     */
    public SurveyQuestion update(String content, String description, SurveyQuestionType type,
                               boolean required, Integer displayOrder, boolean active) {
        this.content = content;
        this.description = description;
        this.type = type;
        this.required = required;
        this.displayOrder = displayOrder;
        this.active = active;
        return this;
    }

    /**
     * 표시 순서 업데이트
     * @param displayOrder 표시 순서
     * @return 업데이트된 문항
     */
    public SurveyQuestion updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    /**
     * 활성 상태 업데이트
     * @param active 활성 상태
     * @return 업데이트된 문항
     */
    public SurveyQuestion updateActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 선택지 추가
     * @param option 선택지
     * @return 업데이트된 문항
     */
    public SurveyQuestion addOption(SurveyQuestionOption option) {
        this.options.add(option);
        return this;
    }

    /**
     * 선택지 제거
     * @param option 선택지
     * @return 업데이트된 문항
     */
    public SurveyQuestion removeOption(SurveyQuestionOption option) {
        this.options.remove(option);
        return this;
    }

    /**
     * 객관식 문항 여부 확인
     * @return 객관식 문항 여부
     */
    public boolean isChoiceQuestion() {
        return this.type == SurveyQuestionType.SINGLE_CHOICE || 
               this.type == SurveyQuestionType.MULTIPLE_CHOICE;
    }

    /**
     * 주관식 문항 여부 확인
     * @return 주관식 문항 여부
     */
    public boolean isTextQuestion() {
        return this.type == SurveyQuestionType.SHORT_ANSWER || 
               this.type == SurveyQuestionType.LONG_ANSWER;
    }

    /**
     * 척도형 문항 여부 확인
     * @return 척도형 문항 여부
     */
    public boolean isScaleQuestion() {
        return this.type == SurveyQuestionType.SCALE;
    }
} 