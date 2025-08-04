package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 엔티티에 대한 데이터 접근을 처리하는 리포지토리
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자를 조회
     * @param email 조회할 사용자의 이메일
     * @return Optional로 감싼 User 객체
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이메일이 존재하는지 확인
     * @param email 확인할 이메일
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByEmail(String email);
} 