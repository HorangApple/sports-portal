package com.example.survey.repository;

import com.example.course.entity.CourseEnrollment;
import com.example.survey.entity.Survey;
import com.example.survey.entity.SurveyResponse;
import com.example.survey.entity.SurveyResponseStatus;
import com.example.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 설문 응답 Repository
 */
@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    /**
     * 설문에 대한 모든 응답 조회
     *
     * @param survey 설문
     * @return 응답 목록
     */
    List<SurveyResponse> findBySurvey(Survey survey);

    /**
     * 설문에 대한 모든 응답 조회 (페이징)
     *
     * @param survey 설문
     * @param pageable 페이징 정보
     * @return 응답 페이지
     */
    Page<SurveyResponse> findBySurvey(Survey survey, Pageable pageable);

    /**
     * 사용자가 제출한 설문 응답 목록 조회
     *
     * @param user 사용자
     * @return 응답 목록
     */
    List<SurveyResponse> findByUser(User user);

    /**
     * 사용자가 제출한 설문 응답 목록 조회 (페이징)
     *
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 응답 페이지
     */
    Page<SurveyResponse> findByUser(User user, Pageable pageable);

    /**
     * 특정 기간에 완료된 설문 응답 목록 조회
     *
     * @param startDateTime 시작 일시
     * @param endDateTime 종료 일시
     * @param pageable 페이징 정보
     * @return 응답 페이지
     */
    Page<SurveyResponse> findByCompletedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    /**
     * 수강신청에 대한 설문 응답 조회
     *
     * @param enrollment 수강신청
     * @param survey 설문
     * @return 설문 응답 (Optional)
     */
    Optional<SurveyResponse> findByEnrollmentAndSurvey(CourseEnrollment enrollment, Survey survey);

    /**
     * 사용자가 특정 설문에 응답했는지 확인
     *
     * @param user 사용자
     * @param survey 설문
     * @return 응답 (Optional)
     */
    Optional<SurveyResponse> findByUserAndSurvey(User user, Survey survey);

    /**
     * 설문 ID에 대한 완료된 응답 개수 조회
     *
     * @param surveyId 설문 ID
     * @return 응답 개수
     */
    @Query("SELECT COUNT(r) FROM SurveyResponse r WHERE r.survey.id = :surveyId AND r.status = 'COMPLETED'")
    long countCompletedResponsesBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * 설문 ID와 사용자 ID로 완료된 응답 조회
     *
     * @param surveyId 설문 ID
     * @param userId 사용자 ID
     * @return 응답 (Optional)
     */
    @Query("SELECT r FROM SurveyResponse r WHERE r.survey.id = :surveyId AND r.user.id = :userId AND r.status = 'COMPLETED'")
    Optional<SurveyResponse> findCompletedResponseBySurveyIdAndUserId(@Param("surveyId") Long surveyId, @Param("userId") Long userId);

    /**
     * 상태별 설문 응답 목록 조회
     *
     * @param status 응답 상태
     * @param pageable 페이징 정보
     * @return 응답 페이지
     */
    Page<SurveyResponse> findByStatus(SurveyResponseStatus status, Pageable pageable);

    /**
     * 사용자와 상태별 설문 응답 목록 조회
     *
     * @param user 사용자
     * @param status 응답 상태
     * @return 응답 목록
     */
    List<SurveyResponse> findByUserAndStatus(User user, SurveyResponseStatus status);
} 