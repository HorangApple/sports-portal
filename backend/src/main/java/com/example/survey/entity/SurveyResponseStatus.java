package com.example.survey.entity;

/**
 * 설문 응답 상태
 */
public enum SurveyResponseStatus {
    /**
     * 진행 중
     */
    IN_PROGRESS("진행 중"),

    /**
     * 완료
     */
    COMPLETED("완료"),

    /**
     * 취소
     */
    CANCELLED("취소");

    private final String description;

    SurveyResponseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 