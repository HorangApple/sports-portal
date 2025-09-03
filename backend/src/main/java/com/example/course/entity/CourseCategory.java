package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 교육 과정 카테고리 엔티티
 * 교육 과정의 카테고리 정보 관리
 */
@Entity
@Table(name = "course_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 카테고리 코드 (고유 식별자)
     */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    /**
     * 카테고리명
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 카테고리 설명
     */
    @Column(length = 500)
    private String description;

    /**
     * 상위 카테고리 (계층 구조 지원)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CourseCategory parent;

    /**
     * 하위 카테고리 목록
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CourseCategory> children = new ArrayList<>();

    /**
     * 정렬 순서
     */
    @Column
    private Integer sortOrder;

    /**
     * 활성화 여부
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * 카테고리에 속한 교육과정 목록
     */
    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<Course> courses = new ArrayList<>();

    /**
     * 하위 카테고리 추가 메서드
     */
    public void addChild(CourseCategory child) {
        this.children.add(child);
        child.setParent(this);
    }

    /**
     * 하위 카테고리 제거 메서드
     */
    public void removeChild(CourseCategory child) {
        this.children.remove(child);
        child.setParent(null);
    }
} 