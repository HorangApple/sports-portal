package com.example.course.repository;

import com.example.course.entity.Course;
import com.example.course.entity.CourseBookmark;
import com.example.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 과정 즐겨찾기 관련 데이터 액세스를 위한 리포지토리
 */
@Repository
public interface CourseBookmarkRepository extends JpaRepository<CourseBookmark, Long> {

    /**
     * 사용자가 즐겨찾기한 과정 북마크 목록 조회
     * @param user 사용자
     * @return 북마크 목록
     */
    List<CourseBookmark> findByUser(User user);
    
    /**
     * 사용자가 즐겨찾기한 과정 북마크 목록 페이징 조회
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 북마크 페이지
     */
    Page<CourseBookmark> findByUser(User user, Pageable pageable);
    
    /**
     * 특정 사용자의 특정 과정 북마크 조회
     * @param user 사용자
     * @param course 과정
     * @return 북마크 (Optional)
     */
    Optional<CourseBookmark> findByUserAndCourse(User user, Course course);
    
    /**
     * 특정 사용자가 특정 과정을 즐겨찾기했는지 확인
     * @param user 사용자
     * @param course 과정
     * @return 즐겨찾기 여부
     */
    boolean existsByUserAndCourse(User user, Course course);
    
    /**
     * 특정 사용자의 즐겨찾기 과정 수 조회
     * @param user 사용자
     * @return 즐겨찾기 과정 수
     */
    long countByUser(User user);
    
    /**
     * 특정 과정을 즐겨찾기한 수 조회
     * @param course 과정
     * @return 즐겨찾기 수
     */
    long countByCourse(Course course);
    
    /**
     * 특정 사용자의 북마크 삭제
     * @param user 사용자
     * @param course 과정
     */
    void deleteByUserAndCourse(User user, Course course);
} 