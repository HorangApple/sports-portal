package com.example.survey.repository;

import com.example.course.entity.Course;
import com.example.survey.entity.Survey;
import com.example.survey.entity.SurveyType;
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
 * 설문 Repository
 */
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    /**
     * 교육과정에 연결된 설문 목록 조회
     *
     * @param course 교육과정
     * @return 설문 목록
     */
    List<Survey> findByCourse(Course course);

    /**
     * 설문 유형별 목록 조회
     *
     * @param type 설문 유형
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    Page<Survey> findByType(SurveyType type, Pageable pageable);

    /**
     * 활성화된 설문 목록 조회
     *
     * @param active 활성 상태
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    Page<Survey> findByActive(boolean active, Pageable pageable);

    /**
     * 제목으로 설문 검색
     *
     * @param title 설문 제목 키워드
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    Page<Survey> findByTitleContaining(String title, Pageable pageable);

    /**
     * 현재 응답 가능한 설문 목록 조회
     *
     * @param now 현재 시간
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    @Query("SELECT s FROM Survey s WHERE s.active = true AND s.startDate <= :now AND s.endDate >= :now")
    Page<Survey> findAvailableSurveys(@Param("now") LocalDateTime now, Pageable pageable);

    /**
     * 교육과정 ID로 설문 목록 조회
     *
     * @param courseId 교육과정 ID
     * @return 설문 목록
     */
    @Query("SELECT s FROM Survey s WHERE s.course.id = :courseId")
    List<Survey> findByCourseId(@Param("courseId") Long courseId);

    /**
     * 제목과 설문 유형으로 설문 검색
     *
     * @param title 설문 제목 키워드
     * @param type 설문 유형
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    Page<Survey> findByTitleContainingAndType(String title, SurveyType type, Pageable pageable);

    /**
     * 종료된 설문 목록 조회
     *
     * @param now 현재 시간
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    @Query("SELECT s FROM Survey s WHERE s.endDate < :now")
    Page<Survey> findClosedSurveys(@Param("now") LocalDateTime now, Pageable pageable);

    /**
     * 설문 제목과 유형으로 설문 조회
     *
     * @param title 설문 제목
     * @param type 설문 유형
     * @return Optional<Survey>
     */
    Optional<Survey> findByTitleAndType(String title, SurveyType type);

    /**
     * 시작 예정인 설문 목록 조회
     *
     * @param now 현재 시간
     * @param pageable 페이징 정보
     * @return 설문 페이지
     */
    @Query("SELECT s FROM Survey s WHERE s.active = true AND s.startDate > :now")
    Page<Survey> findUpcomingSurveys(@Param("now") LocalDateTime now, Pageable pageable);
} 