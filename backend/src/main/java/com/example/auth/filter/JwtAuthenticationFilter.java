package com.example.auth.filter;

import com.example.auth.service.TokenService;
import com.example.auth.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰 기반 인증을 처리하는 필터
 * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출하고 검증하여
 * Spring Security의 SecurityContext에 인증 정보를 설정
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public TokenService getTokenService() {
        return tokenService;
    }

    /**
     * HTTP 요청을 필터링하여 JWT 토큰 기반 인증을 처리
     * 1. Authorization 헤더에서 JWT 토큰 추출
     * 2. 토큰의 유효성 검증 (서명 검증 및 만료 확인)
     * 3. 토큰에서 사용자 정보 추출
     * 4. SecurityContext에 인증 정보 설정
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            // Authorization 헤더가 없거나 Bearer 타입이 아닌 경우 필터 체인 계속 진행
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Bearer 접두어 제거 후 JWT 토큰 추출
            jwt = authHeader.substring(7);

            // 토큰의 유효성을 검증 (서명 검증 및 만료 확인)
            if (!tokenService.validateToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰에서 사용자 이메일 추출
            userEmail = tokenService.getEmailFromToken(jwt);

            // 사용자 이메일이 존재하고 현재 SecurityContext에 인증 정보가 없는 경우
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 사용자 정보 로드
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // 인증 토큰 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                // 요청 상세 정보 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // 토큰 처리 중 발생한 예외는 로깅하고 필터 체인 계속 진행
            logger.error("JWT 토큰 처리 중 오류 발생", e);
        }
        
        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
} 