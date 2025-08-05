package com.example.survey.entity;

/**
 * 설문지 유형
 */
public enum SurveyType {
    /**
     * 사전 설문
     */
    PRE("사전 설문"),

    /**
     * 만족도 설문
     */
    SATISFACTION("만족도 설문"),

    /**
     * 사후 설문
     */
    POST("사후 설문"),

    /**
     * 일반 설문
     */
    GENERAL("일반 설문");

    private final String description;

    SurveyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 