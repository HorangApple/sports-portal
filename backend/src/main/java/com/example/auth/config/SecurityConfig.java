package com.example.auth.config;

import com.example.auth.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 기본 보안 설정 클래스 (프로파일이 지정되지 않은 경우 사용)
 * 개발 환경의 설정과 동일하게 구성하되, 프로파일 미지정 시 적용
 */
@Configuration
@EnableWebSecurity
@Order(100) // 낮은 우선순위로 설정
@Profile("default") // 프로파일이 명시적으로 지정되지 않은 경우 사용
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        UserDetailsService userDetailsService) {
        super(jwtAuthenticationFilter, userDetailsService);
    }

    /**
     * 기본 보안 필터 체인 구성
     * 명시적인 프로파일이 지정되지 않은 경우의 기본 설정
     */
    @Override
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                .requestMatchers("/api/v1/user/courses/**").hasAnyRole("USER", "ADMIN", "INSTRUCTOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 