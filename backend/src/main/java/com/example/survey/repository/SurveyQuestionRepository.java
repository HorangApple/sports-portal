package com.example.survey.repository;

import com.example.survey.entity.Survey;
import com.example.survey.entity.SurveyQuestion;
import com.example.survey.entity.SurveyQuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 설문 문항 Repository
 */
@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

    /**
     * 설문에 속한 문항 목록 조회 (순서대로)
     *
     * @param survey 설문
     * @return 문항 목록
     */
    List<SurveyQuestion> findBySurveyOrderByDisplayOrder(Survey survey);

    /**
     * 설문 ID로 문항 목록 조회 (순서대로)
     *
     * @param surveyId 설문 ID
     * @return 문항 목록
     */
    @Query("SELECT q FROM SurveyQuestion q WHERE q.survey.id = :surveyId ORDER BY q.displayOrder")
    List<SurveyQuestion> findBySurveyIdOrderByDisplayOrder(@Param("surveyId") Long surveyId);

    /**
     * 특정 유형의 문항 목록 조회
     *
     * @param survey 설문
     * @param type 문항 유형
     * @return 문항 목록
     */
    List<SurveyQuestion> findBySurveyAndType(Survey survey, SurveyQuestionType type);

    /**
     * 설문에 속한 필수 문항 목록 조회
     *
     * @param survey 설문
     * @return 필수 문항 목록
     */
    List<SurveyQuestion> findBySurveyAndRequiredTrue(Survey survey);

    /**
     * 설문에 속한 문항 개수 조회
     *
     * @param survey 설문
     * @return 문항 개수
     */
    long countBySurvey(Survey survey);

    /**
     * 특정 설문의 마지막 문항 순서 조회
     *
     * @param surveyId 설문 ID
     * @return 마지막 문항 순서 (없으면 0)
     */
    @Query("SELECT COALESCE(MAX(q.displayOrder), 0) FROM SurveyQuestion q WHERE q.survey.id = :surveyId")
    int findMaxDisplayOrderBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * 설문에 속한 문항 삭제
     *
     * @param survey 설문
     */
    void deleteBySurvey(Survey survey);

    /**
     * 활성화된 문항 목록 조회
     *
     * @param survey 설문
     * @return 활성화된 문항 목록
     */
    List<SurveyQuestion> findBySurveyAndActiveTrue(Survey survey);
} 