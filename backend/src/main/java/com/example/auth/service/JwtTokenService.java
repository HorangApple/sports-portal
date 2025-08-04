package com.example.auth.service;

import com.example.auth.config.JwtProperties;
import com.example.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 관련 서비스를 제공하는 클래스
 * 토큰 생성, 검증, 만료 확인 등의 기능을 구현
 */
@Service
public class JwtTokenService implements TokenService {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.access-token-expiration}")
    private long accessTokenValidity;

    @Value("${spring.jwt.refresh-token-expiration}")
    private long refreshTokenValidity;

    private Key key;
    
    private JwtProperties jwtProperties;
    
    // 기본 생성자
    public JwtTokenService() {
    }
    
    // JwtProperties를 매개변수로 받는 생성자
    public JwtTokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        if (jwtProperties != null && jwtProperties.getSecret() != null) {
            this.secret = jwtProperties.getSecret();
            this.accessTokenValidity = jwtProperties.getAccessTokenExpiration();
            this.refreshTokenValidity = jwtProperties.getRefreshTokenExpiration();
            init();
        }
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 사용자 정보를 기반으로 액세스 토큰을 생성
     * @param user 토큰을 생성할 사용자의 정보
     * @return 생성된 JWT 액세스 토큰
     */
    @Override
    public String generateAccessToken(User user) {
        return generateToken(user.getEmail(), accessTokenValidity);
    }

    /**
     * 사용자 정보를 기반으로 리프레시 토큰을 생성
     * @param user 토큰을 생성할 사용자의 정보
     * @return 생성된 JWT 리프레시 토큰
     */
    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user.getEmail(), refreshTokenValidity);
    }

    /**
     * JWT 토큰을 생성하는 내부 메서드
     * @param email 토큰을 생성할 사용자의 이메일
     * @param validityInMilliseconds 토큰 유효 시간 (밀리초)
     * @return 생성된 JWT 토큰
     */
    private String generateToken(String email, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 아니면 false
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 이메일을 추출
     * @param token JWT 토큰
     * @return 토큰에 포함된 이메일
     */
    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * JWT 토큰의 만료 여부를 확인
     * @param token 확인할 JWT 토큰
     * @return 토큰이 만료되었으면 true, 아니면 false
     */
    @Override
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * JWT 토큰에서 만료 시간을 추출
     * @param token JWT 토큰
     * @return 토큰의 만료 시간
     */
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
} 