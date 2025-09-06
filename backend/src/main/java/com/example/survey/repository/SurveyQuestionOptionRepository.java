package com.example.survey.repository;

import com.example.survey.entity.SurveyQuestion;
import com.example.survey.entity.SurveyQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 설문 문항 선택지 Repository
 */
@Repository
public interface SurveyQuestionOptionRepository extends JpaRepository<SurveyQuestionOption, Long> {

    /**
     * 설문 문항에 해당하는 모든 선택지 조회
     * @param question 설문 문항
     * @return 선택지 목록
     */
    List<SurveyQuestionOption> findByQuestion(SurveyQuestion question);

    /**
     * 설문 문항에 해당하는 모든 선택지를 순서대로 조회
     * @param question 설문 문항
     * @return 순서대로 정렬된 선택지 목록
     */
    List<SurveyQuestionOption> findByQuestionOrderByDisplayOrder(SurveyQuestion question);

    /**
     * 설문 문항 ID에 해당하는 모든 선택지를 순서대로 조회
     * @param questionId 설문 문항 ID
     * @return 순서대로 정렬된 선택지 목록
     */
    List<SurveyQuestionOption> findByQuestionIdOrderByDisplayOrder(Long questionId);

    /**
     * 설문 문항에 해당하는 선택지 개수 조회
     * @param question 설문 문항
     * @return 선택지 개수
     */
    long countByQuestion(SurveyQuestion question);

    /**
     * 설문 문항 ID에 대한 최대 선택지 순서번호 조회
     * @param questionId 설문 문항 ID
     * @return 최대 순서번호, 선택지가 없는 경우 0
     */
    @Query("SELECT COALESCE(MAX(o.displayOrder), 0) FROM SurveyQuestionOption o WHERE o.question.id = :questionId")
    int findMaxDisplayOrderByQuestionId(@Param("questionId") Long questionId);

    /**
     * 설문 문항에 해당하는 모든 선택지 삭제
     * @param question 설문 문항
     */
    void deleteByQuestion(SurveyQuestion question);
    
    /**
     * 활성화된 선택지 목록 조회
     * @param question 설문 문항
     * @return 활성화된 선택지 목록
     */
    List<SurveyQuestionOption> findByQuestionAndActiveTrue(SurveyQuestion question);
} 