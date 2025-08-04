package com.example.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성시간, 수정시간을 자동으로 관리하는 기본 엔티티 클래스
 * 이 클래스를 상속받은 엔티티는 생성시간과 수정시간이 자동으로 기록됨
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /**
     * 엔티티 생성 시간
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 수정 시간
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
} 