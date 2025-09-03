package com.example.course.service;

import com.example.course.dto.CourseLevelResponse;
import com.example.course.entity.CourseLevel;
import com.example.course.repository.CourseLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 교육 과정 레벨 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseLevelService {

    private final CourseLevelRepository levelRepository;

    /**
     * 모든 활성화된 레벨 목록 조회
     */
    public List<CourseLevelResponse> findAllActiveLevels() {
        return levelRepository.findByActiveTrueOrderBySortOrderAsc().stream()
                .map(CourseLevelResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 레벨 ID로 레벨 조회
     */
    public CourseLevelResponse findLevelById(Long id) {
        CourseLevel level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레벨을 찾을 수 없습니다. ID: " + id));
        return CourseLevelResponse.from(level);
    }

    /**
     * 새로운 레벨 생성
     */
    @Transactional
    public CourseLevelResponse createLevel(String name, Integer sortOrder, String description) {
        // 이름 중복 체크
        if (levelRepository.existsByName(name)) {
            throw new RuntimeException("이미 존재하는 레벨 이름입니다: " + name);
        }

        CourseLevel level = CourseLevel.builder()
                .name(name)
                .sortOrder(sortOrder)
                .active(true)
                .description(description)
                .build();

        CourseLevel savedLevel = levelRepository.save(level);
        return CourseLevelResponse.from(savedLevel);
    }

    /**
     * 레벨 정보 업데이트
     */
    @Transactional
    public CourseLevelResponse updateLevel(Long id, String name, Integer sortOrder, String description) {
        CourseLevel level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레벨을 찾을 수 없습니다. ID: " + id));

        // 이름이 변경되었고, 변경된 이름이 이미 존재하는 경우 체크
        if (!level.getName().equals(name) && levelRepository.existsByName(name)) {
            throw new RuntimeException("이미 존재하는 레벨 이름입니다: " + name);
        }

        level.update(name, sortOrder, level.isActive(), description);
        return CourseLevelResponse.from(level);
    }

    /**
     * 레벨 활성화/비활성화
     */
    @Transactional
    public void setLevelActive(Long id, boolean active) {
        CourseLevel level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레벨을 찾을 수 없습니다. ID: " + id));
        level.setActive(active);
    }
} 