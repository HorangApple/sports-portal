package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 등록
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @param name 사용자 이름
     * @return 등록된 사용자 정보
     */
    @Transactional
    public User registerUser(String email, String password, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role(UserRole.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    /**
     * UserDetailsService 구현 메서드 제거 - CustomUserDetailsService에서만 구현
     */

    /**
     * 이메일로 사용자 조회
     * @param email 조회할 사용자의 이메일
     * @return 사용자 정보
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
    }
} 