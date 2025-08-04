package com.example.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전역 예외 처리기
 * API 요청 중 발생하는 다양한 예외를 처리하고 표준화된 에러 응답을 반환합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 리소스를 찾을 수 없는 예외 처리
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
    }

    /**
     * 중복된 리소스 예외 처리
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getDescription(false));
    }

    /**
     * 인증 실패 예외 처리
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다.", request.getDescription(false));
    }

    /**
     * 권한 없음 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다.", request.getDescription(false));
    }

    /**
     * JWT 토큰 만료 예외 처리
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다.", request.getDescription(false));
    }

    /**
     * 유효하지 않은 JWT 토큰 예외 처리
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleInvalidJwtException(SignatureException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다.", request.getDescription(false));
    }

    /**
     * 요청 파라미터 유효성 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", "유효하지 않은 요청 파라미터입니다.");
        response.put("path", request.getDescription(false).replace("uri=", ""));
        
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("field", ((FieldError) error).getField());
            errorDetails.put("message", error.getDefaultMessage());
            errors.add(errorDetails);
        });
        
        response.put("errors", errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 제약 조건 위반 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", "유효성 검증에 실패했습니다.");
        response.put("path", request.getDescription(false).replace("uri=", ""));
        
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("field", violation.getPropertyPath().toString());
            errorDetails.put("message", violation.getMessage());
            errors.add(errorDetails);
        });
        
        response.put("errors", errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", request.getDescription(false));
    }

    /**
     * 표준화된 에러 응답 생성
     */
    private ResponseEntity<?> createErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("path", path.replace("uri=", ""));
        
        return new ResponseEntity<>(response, status);
    }
} 