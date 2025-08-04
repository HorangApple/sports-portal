package com.example.auth.config;

import com.example.auth.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 개발 환경에서 사용하는 보안 설정
 * - 개발 환경에서는 보안을 완화하고 H2 콘솔 접근 등 개발 편의성 제공
 */
@Configuration
@EnableWebSecurity
@Profile({"local", "dev", "test"})
public class DevSecurityConfig extends BaseSecurityConfig {

    public DevSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        super(jwtAuthenticationFilter, userDetailsService);
    }

    /**
     * 개발 환경용 보안 필터 체인 구성
     * - CSRF 보호 비활성화
     * - H2 콘솔 프레임 옵션 허용
     * - 개발용 엔드포인트 접근 허용
     */
    @Override
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // H2 콘솔 접근 허용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/api/v1/users/**", "/api/v1/courses/**", 
                    "/api/v1/surveys/**", "/api/v1/facilities/**", "/api/v1/statistics/**",
                    "/swagger-ui/**", "/api-docs/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/v1/user/courses/**").hasAnyRole("USER", "ADMIN", "INSTRUCTOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 