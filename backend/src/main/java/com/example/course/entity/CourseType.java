package com.example.course.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 교육 과정 유형 엔티티
 * 교육 과정의 유형 정보 관리 (예: 온라인, 오프라인, 블렌디드 등)
 */
@Entity
@Table(name = "course_types")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseType extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 유형 코드 (고유 식별자)
     */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    /**
     * 유형명
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 유형 설명
     */
    @Column(length = 500)
    private String description;

    /**
     * 활성화 여부
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * 유형에 속한 교육과정 목록
     */
    @OneToMany(mappedBy = "type")
    @Builder.Default
    private List<Course> courses = new ArrayList<>();
    
    /**
     * 과정 유형 정보 업데이트
     *
     * @param code 유형 코드
     * @param name 유형명
     * @param description 유형 설명
     * @param active 활성화 여부
     */
    public void update(String code, String name, String description, boolean active) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.active = active;
    }
} 