package com.example.course.service;

import com.example.common.exception.EntityNotFoundException;
import com.example.course.dto.CourseResponse;
import com.example.course.entity.Course;
import com.example.course.entity.CourseBookmark;
import com.example.course.repository.CourseBookmarkRepository;
import com.example.course.repository.CourseRepository;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 과정 즐겨찾기 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseBookmarkService {

    private final CourseBookmarkRepository bookmarkRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 즐겨찾기 과정 목록 조회
     * @param userId 사용자 ID
     * @return 즐겨찾기한 과정 목록
     */
    public List<CourseResponse> getUserBookmarkedCourses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        return bookmarkRepository.findByUser(user).stream()
                .map(bookmark -> CourseResponse.from(bookmark.getCourse()))
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 즐겨찾기 과정 목록 페이징 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 즐겨찾기한 과정 페이지
     */
    public Page<CourseResponse> getUserBookmarkedCourses(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        return bookmarkRepository.findByUser(user, pageable)
                .map(bookmark -> CourseResponse.from(bookmark.getCourse()));
    }

    /**
     * 과정 즐겨찾기 추가
     * @param userId 사용자 ID
     * @param courseId 과정 ID
     * @return 즐겨찾기 성공 여부
     */
    @Transactional
    public boolean addBookmark(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + courseId));

        // 이미 즐겨찾기한 경우 중복 저장 방지
        if (bookmarkRepository.existsByUserAndCourse(user, course)) {
            return false;
        }

        CourseBookmark bookmark = CourseBookmark.builder()
                .user(user)
                .course(course)
                .build();

        bookmarkRepository.save(bookmark);
        return true;
    }

    /**
     * 과정 즐겨찾기 삭제
     * @param userId 사용자 ID
     * @param courseId 과정 ID
     * @return 즐겨찾기 삭제 성공 여부
     */
    @Transactional
    public boolean removeBookmark(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + courseId));

        // 즐겨찾기가 존재하는 경우에만 삭제
        if (!bookmarkRepository.existsByUserAndCourse(user, course)) {
            return false;
        }

        bookmarkRepository.deleteByUserAndCourse(user, course);
        return true;
    }

    /**
     * 특정 과정이 즐겨찾기되었는지 확인
     * @param userId 사용자 ID
     * @param courseId 과정 ID
     * @return 즐겨찾기 여부
     */
    public boolean isBookmarked(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("과정을 찾을 수 없습니다. ID: " + courseId));

        return bookmarkRepository.existsByUserAndCourse(user, course);
    }

    /**
     * 사용자의 즐겨찾기 과정 수 조회
     * @param userId 사용자 ID
     * @return 즐겨찾기 과정 수
     */
    public long countUserBookmarks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        return bookmarkRepository.countByUser(user);
    }
} 