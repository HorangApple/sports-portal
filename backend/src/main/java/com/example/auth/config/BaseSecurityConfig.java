package com.example.auth.config;

import com.example.auth.filter.JwtAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 기본 보안 설정을 정의하는 추상 클래스
 * 개발 환경과 운영 환경에서 공통으로 사용하는 설정 제공
 */
public abstract class BaseSecurityConfig {

    protected final JwtAuthenticationFilter jwtAuthenticationFilter;
    protected final UserDetailsService userDetailsService;

    protected BaseSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                               UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 환경별 보안 필터 체인 설정을 제공하는 추상 메서드
     */
    public abstract SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception;

    /**
     * 인증 관리자 빈을 생성
     * 
     * @param config AuthenticationConfiguration 객체
     * @return 구성된 AuthenticationManager
     * @throws Exception 인증 관리자 생성 중 발생할 수 있는 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 비밀번호 인코더 빈을 생성
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 