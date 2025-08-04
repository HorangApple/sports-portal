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
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * 운영 환경에서 사용하는 보안 설정
 * - 시연을 위해 보안 설정 완화
 */
@Configuration
@EnableWebSecurity
@Profile({"prod", "staging"})
public class ProdSecurityConfig extends BaseSecurityConfig {

    public ProdSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                           UserDetailsService userDetailsService) {
        super(jwtAuthenticationFilter, userDetailsService);
    }

    /**
     * 운영 환경용 보안 필터 체인 구성
     * - 시연을 위해 CSRF 보호 비활성화
     * - 대부분의 API 엔드포인트 접근 허용
     */
    @Override
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/courses/**", "/api/v1/surveys/**").permitAll() 
                .requestMatchers("/api/v1/facilities/**", "/api/v1/statistics/**").permitAll()
                .requestMatchers("/api/v1/calendar/**", "/api/v1/recommendations/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .requestMatchers("/api/v1/user/courses/**").hasAnyRole("USER", "ADMIN", "INSTRUCTOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 