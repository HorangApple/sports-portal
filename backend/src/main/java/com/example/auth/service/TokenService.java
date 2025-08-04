package com.example.auth.service;

import com.example.user.entity.User;

public interface TokenService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    boolean isTokenExpired(String token);
} 