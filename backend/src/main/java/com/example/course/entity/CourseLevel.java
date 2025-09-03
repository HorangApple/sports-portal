package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 교육 과정 레벨 엔티티 클래스
 * 입문, 초급, 중급, 고급 등 과정의 난이도를 표현
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "course_levels")
public class CourseLevel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 레벨 이름 (예: 입문, 초급, 중급, 고급)
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 정렬 순서
     */
    @Column(nullable = false)
    private Integer sortOrder;

    /**
     * 활성화 여부
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * 설명
     */
    @Column(length = 500)
    private String description;

    /**
     * 생성자
     */
    @Builder
    public CourseLevel(String name, Integer sortOrder, boolean active, String description) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.active = active;
        this.description = description;
    }

    /**
     * 레벨 정보 업데이트
     */
    public void update(String name, Integer sortOrder, boolean active, String description) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.active = active;
        this.description = description;
    }

    /**
     * 레벨 활성화/비활성화
     */
    public void setActive(boolean active) {
        this.active = active;
    }
} 