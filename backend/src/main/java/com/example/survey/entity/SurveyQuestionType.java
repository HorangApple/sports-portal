package com.example.survey.entity;

/**
 * 설문 문항 유형
 */
public enum SurveyQuestionType {
    /**
     * 객관식 단일 선택
     */
    SINGLE_CHOICE("객관식 단일 선택"),
    
    /**
     * 객관식 다중 선택
     */
    MULTIPLE_CHOICE("객관식 다중 선택"),
    
    /**
     * 주관식 단답형
     */
    SHORT_ANSWER("주관식 단답형"),
    
    /**
     * 주관식 서술형
     */
    LONG_ANSWER("주관식 서술형"),
    
    /**
     * 척도형 (1~5, 1~7, 1~10 등)
     */
    SCALE("척도형"),
    
    /**
     * 날짜 선택
     */
    DATE("날짜 선택"),

    /**
     * 찬반형 (예/아니오)
     */
    YES_NO("찬반형");
    
    private final String description;
    
    SurveyQuestionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 