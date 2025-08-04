package com.example.user.entity;

import com.example.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 사용자 엔티티 클래스
 * Spring Security의 UserDetails 인터페이스를 구현하여 인증에 사용
 * 사용자의 기본 정보와 인증 관련 정보를 관리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity implements UserDetails {

    /**
     * 사용자 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이메일 (로그인 ID로 사용)
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 사용자 비밀번호 (암호화되어 저장)
     */
    @Column(nullable = false)
    private String password;

    /**
     * 사용자 이름
     */
    @Column(nullable = false)
    private String name;

    /**
     * 사용자 역할 (권한)
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * 계정 활성화 여부
     */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * 사용자 생성자
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @param name 사용자 이름
     * @param role 사용자 역할
     */
    @Builder
    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * 사용자의 권한 목록을 반환
     * @return 사용자의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * 사용자의 로그인 ID를 반환 (이메일 사용)
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 계정이 만료되지 않았는지 확인
     * @return 계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있지 않은지 확인
     * @return 계정 잠금 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되지 않았는지 확인
     * @return 자격 증명 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되어 있는지 확인
     * @return 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 사용자 비밀번호를 업데이트
     * @param password 새로운 비밀번호
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 사용자 계정을 비활성화
     */
    public void disable() {
        this.enabled = false;
    }
} 