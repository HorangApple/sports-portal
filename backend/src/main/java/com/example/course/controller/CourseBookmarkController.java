package com.example.course.controller;

import com.example.course.dto.CourseResponse;
import com.example.course.service.CourseBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 과정 즐겨찾기 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class CourseBookmarkController {

    private final CourseBookmarkService bookmarkService;

    /**
     * 현재 사용자의 즐겨찾기 과정 목록 조회
     * @param authentication 인증 정보
     * @return 즐겨찾기한 과정 목록
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getUserBookmarkedCourses(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        List<CourseResponse> bookmarkedCourses = bookmarkService.getUserBookmarkedCourses(userId);
        return ResponseEntity.ok(bookmarkedCourses);
    }

    /**
     * 현재 사용자의 즐겨찾기 과정 목록 페이징 조회
     * @param authentication 인증 정보
     * @param pageable 페이징 정보
     * @return 즐겨찾기한 과정 페이지
     */
    @GetMapping("/courses/paged")
    public ResponseEntity<Page<CourseResponse>> getUserBookmarkedCoursesPaged(
            Authentication authentication,
            Pageable pageable) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        Page<CourseResponse> bookmarkedCourses = bookmarkService.getUserBookmarkedCourses(userId, pageable);
        return ResponseEntity.ok(bookmarkedCourses);
    }

    /**
     * 과정 즐겨찾기 추가
     * @param authentication 인증 정보
     * @param courseId 과정 ID
     * @return 즐겨찾기 추가 결과
     */
    @PostMapping("/courses/{courseId}")
    public ResponseEntity<Map<String, Object>> addBookmark(
            Authentication authentication,
            @PathVariable Long courseId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        boolean added = bookmarkService.addBookmark(userId, courseId);
        
        if (added) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "과정이 즐겨찾기에 추가되었습니다.",
                "courseId", courseId
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "이미 즐겨찾기에 추가된 과정입니다.",
                "courseId", courseId
            ));
        }
    }

    /**
     * 과정 즐겨찾기 삭제
     * @param authentication 인증 정보
     * @param courseId 과정 ID
     * @return 즐겨찾기 삭제 결과
     */
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Map<String, Object>> removeBookmark(
            Authentication authentication,
            @PathVariable Long courseId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        boolean removed = bookmarkService.removeBookmark(userId, courseId);
        
        if (removed) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "과정이 즐겨찾기에서 제거되었습니다.",
                "courseId", courseId
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "즐겨찾기에 없는 과정입니다.",
                "courseId", courseId
            ));
        }
    }

    /**
     * 과정 즐겨찾기 상태 확인
     * @param authentication 인증 정보
     * @param courseId 과정 ID
     * @return 즐겨찾기 여부
     */
    @GetMapping("/courses/{courseId}/status")
    public ResponseEntity<Map<String, Object>> checkBookmarkStatus(
            Authentication authentication,
            @PathVariable Long courseId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        boolean isBookmarked = bookmarkService.isBookmarked(userId, courseId);
        
        return ResponseEntity.ok(Map.of(
            "isBookmarked", isBookmarked,
            "courseId", courseId
        ));
    }

    /**
     * 사용자의 즐겨찾기 과정 수 조회
     * @param authentication 인증 정보
     * @return 즐겨찾기 과정 수
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countUserBookmarks(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        long bookmarkCount = bookmarkService.countUserBookmarks(userId);
        
        return ResponseEntity.ok(Map.of(
            "count", bookmarkCount
        ));
    }
} 