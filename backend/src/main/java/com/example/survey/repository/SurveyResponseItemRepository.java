package com.example.survey.repository;

import com.example.survey.entity.SurveyQuestion;
import com.example.survey.entity.SurveyQuestionOption;
import com.example.survey.entity.SurveyResponse;
import com.example.survey.entity.SurveyResponseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 설문 응답 항목 Repository
 */
@Repository
public interface SurveyResponseItemRepository extends JpaRepository<SurveyResponseItem, Long> {

    /**
     * 특정 설문 응답에 대한 모든 응답 항목 조회
     * @param response 설문 응답
     * @return 응답 항목 목록
     */
    List<SurveyResponseItem> findByResponse(SurveyResponse response);

    /**
     * 특정 설문 응답과 문항에 대한 응답 항목 조회
     * @param response 설문 응답
     * @param question 설문 문항
     * @return 응답 항목 (Optional)
     */
    Optional<SurveyResponseItem> findByResponseAndQuestion(SurveyResponse response, SurveyQuestion question);

    /**
     * 특정 설문 응답의 모든 응답 항목 삭제
     * @param response 설문 응답
     */
    void deleteByResponse(SurveyResponse response);

    /**
     * 특정 문항에 대한 모든 응답 항목 조회
     * @param question 설문 문항
     * @return 응답 항목 목록
     */
    List<SurveyResponseItem> findByQuestion(SurveyQuestion question);

    /**
     * 특정 문항에 대한 모든 응답 항목 삭제
     * @param question 설문 문항
     */
    void deleteByQuestion(SurveyQuestion question);

    /**
     * 특정 옵션을 선택한 모든 응답 항목 조회
     * @param selectedOption 선택한 옵션
     * @return 응답 항목 목록
     */
    List<SurveyResponseItem> findBySelectedOption(SurveyQuestionOption selectedOption);

    /**
     * 특정 옵션을 선택한 응답 수 조회
     * @param selectedOption 선택한 옵션
     * @return 응답 수
     */
    long countBySelectedOption(SurveyQuestionOption selectedOption);

    /**
     * 특정 설문의 각 문항별 옵션 선택 통계 조회
     * @param surveyId 설문 ID
     * @return 문항ID, 옵션ID, 선택 수의 맵
     */
    @Query("SELECT i.question.id AS questionId, i.selectedOption.id AS optionId, COUNT(i) AS count " +
           "FROM SurveyResponseItem i " +
           "WHERE i.response.survey.id = :surveyId AND i.selectedOption IS NOT NULL " +
           "GROUP BY i.question.id, i.selectedOption.id")
    List<Map<String, Object>> getOptionSelectionStatsBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * 특정 문항에 대한 옵션별 선택 통계 조회
     * @param questionId 문항 ID
     * @return 옵션ID와 선택 수의 맵
     */
    @Query("SELECT i.selectedOption.id AS optionId, COUNT(i) AS count " +
           "FROM SurveyResponseItem i " +
           "WHERE i.question.id = :questionId AND i.selectedOption IS NOT NULL " +
           "GROUP BY i.selectedOption.id")
    List<Map<String, Object>> getOptionSelectionStatsByQuestionId(@Param("questionId") Long questionId);
    
    /**
     * 특정 문항의 척도형 응답 평균 조회
     * @param questionId 문항 ID
     * @return 척도형 응답 평균
     */
    @Query("SELECT AVG(i.scaleValue) FROM SurveyResponseItem i " +
           "WHERE i.question.id = :questionId AND i.scaleValue IS NOT NULL")
    Double getAverageScaleValueByQuestionId(@Param("questionId") Long questionId);
} 